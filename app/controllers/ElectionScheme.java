package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.tally;
import views.html.index;
import views.html.pending;
import views.html.register;
import views.html.vote;
import views.html.progress;
import views.html.verifyvote;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Optional;

public class ElectionScheme extends Controller {

    private static Optional<Election> election = Optional.empty();
    private static ElectionAdmin admin;
    private static BallotBox ballotBox;
    private static Registrar registrar;
    private static Trustee trustee;

    /**
     * Setup the election when an admin user navigates to /setup
     * @return A setup election
     */
    @Security.Authenticated(Secured.class)
    public static Result setup() {
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            admin = new ElectionAdmin();
            election = admin.setup();
            ballotBox = new BallotBox();
            registrar = new Registrar();
            trustee = new Trustee();
            flash("success", "Election setup");
            return redirect(routes.ElectionScheme.electionInProgress());
        } else {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }

    }

    public static Optional<Election> getElection() {
        return election;
    }

    public static ElectionAdmin getElectionAdmin() {
        return admin;
    }

    /**
     * If no election is ready then display to the user that no election has been setup
     */
    @Security.Authenticated(Secured.class)
    public static Result pending() {
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }
        return ok(pending.render("Pending", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
    }

    /**
     * An unregistered user is registering for an election that has been setup
     */
    @Security.Authenticated(Secured.class)
    public static Result register() {
        // Should not access this page if an admin user
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }
        return ok(register.render("Register", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));

    }

    /**
     * Post method for when user clicks to reigister for an election
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result postRegister() {
        // Should not be able to register if user is an admin user
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }

        String email = session("email");
        try {
            if (election.isPresent()) {
                Voter voter = registrar.registerVoter(email, election.get(), admin);
            } else {
                // If no election is present
                flash("error", "No Election setup");
                return redirect(routes.Application.index());
            }
        } catch (NoSuchAlgorithmException e) {
            // This error should never occur
            flash("error", "Invalid registration");
            return redirect(routes.ElectionScheme.register());
        }
        flash("success", "Successfully registered");
        return redirect(routes.ElectionScheme.vote());

    }


    /**
     * Get method to diaplay the voting booth
     * @return The voting booth page
     */
    @Security.Authenticated(Secured.class)
    public static Result vote() {
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }
        Form<Vote> formData = Form.form(Vote.class).bindFromRequest();
        formData.discardErrors();
        return ok(vote.render("Voting Booth", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, Candidates.getCandidateNames()));

    }

    /**
     * Post method to post the users voting choice
     */
    @Security.Authenticated(Secured.class)
    public static Result postVote() {
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            // Cannot vote if you are an admin user
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }

        // Get form data from voting booth
        // Voters choice from the radio buttons
        Form<Vote> formData = Form.form(Vote.class).bindFromRequest();
        if (formData.hasErrors()) {
            flash("error", "Error in voting booth selection");
            return badRequest(views.html.vote.render("Voting Booth", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, Candidates.getCandidateNames()));
        } else {
            Vote votersChoice = new Vote();
            // Get voters choice from form
            votersChoice.candidates = formData.get().candidates;

            // Get users email from the session
            String email = session("email");
            Optional<Voter> voterById = admin.getVoterById(email);

            if (voterById.isPresent() && election.isPresent()) {
                Voter voter = voterById.get();

                try {
                    // Create a ballot for intended candidate
                    // And a negative ballot for candidate that has not been choosen
                    Ballot ballot = vote("1", voter);
                    Ballot negativeBallot = vote("0", voter);

                    if (votersChoice.candidates.equals("Joe Bloggs")) {
                        ballotBox.appendBB1(ballot);
                        ballotBox.appendBB2(negativeBallot);
                    } else if (votersChoice.candidates.equals("Jane Doe")){
                        ballotBox.appendBB2(ballot);
                        ballotBox.appendBB1(negativeBallot);
                    } else {
                        flash("error", "invalid vote");
                        return badRequest(views.html.vote.render("Voting Booth", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, Candidates.getCandidateNames()));
                    }
                    voter.setVoted(true);
                } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | UnsupportedEncodingException e) {
                    flash("error", "invalid vote");
                    return badRequest(views.html.vote.render("Voting Booth", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData, Candidates.getCandidateNames()));
                }
                flash("success", "Successfully voted");
                return redirect(routes.ElectionScheme.electionInProgress());

            } else {
                flash("error", "Attempting to access incorrect page");
                return redirect(routes.Application.index());
            }

        }

    }

    /**
     * When the election has been ended tally the results
     * @return Election results page
     */
    @Security.Authenticated(Secured.class)
    public static Result tallyResults() {

        if (!election.isPresent()) {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        } else {
            BulletinBoard bulletinBoard1 = ballotBox.getBulletinBoard1();
            BulletinBoard bulletinBoard2 = ballotBox.getBulletinBoard2();
            Election currentElection = election.get();
            BigInteger tally1 = trustee.tally(bulletinBoard1, currentElection);
            BigInteger tally2 = trustee.tally(bulletinBoard2, currentElection);
            currentElection.setEnded(true);
            String displayValue1 = String.valueOf(tally1);
            String displayValue2 = String.valueOf(tally2);
            return ok(tally.render("Tally", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), displayValue1, displayValue2));
        }
    }

    /**
     * Verify the users vote is contained in the bulletin board
     */
    @Security.Authenticated(Secured.class)
    public static Result verifyVote() {
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }

        String email = session("email");
        Optional<Voter> voterById = admin.getVoterById(email);

        try {
            if (voterById.isPresent()) {
                if (Crypto.verifyVote(voterById.get(), ballotBox)) {
                    // If the vote has been found then return a success page
                    return ok(verifyvote.render("Verify vote", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
                } else
                    return ok("Not found");
            } else {
                return ok("Not found");
            }
        } catch (UnsupportedEncodingException | SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            flash("Error", "Whilst verifying");
            return badRequest("Error");
        }
    }

    private static Ballot vote(String vote, Voter voter) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        BigInteger voteChoice = new BigInteger(vote);

        // This is safe as election presence is checked above
        BigInteger plaintext = election.get().getGenerator().modPow(voteChoice, election.get().getPrime());
        return Crypto.encrypt(plaintext, voter, election.get());
    }

    @Security.Authenticated(Secured.class)
    public static Result electionInProgress() {
        return ok(progress.render("Progress", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
    }

    @Security.Authenticated(Secured.class)
    public static Result restartElection() {
        if (Secured.getUserInfo(ctx()).getType().equals("admin")) {
            return redirect(routes.ElectionScheme.setup());
        } else {
            flash("error", "Attempting to access incorrect page");
            return redirect(routes.Application.index());
        }
    }
}
