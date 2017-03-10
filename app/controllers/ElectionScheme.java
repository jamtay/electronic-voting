package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.tally;
import views.html.index;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Optional;

public class ElectionScheme extends Controller {

    private static Election election;
    private static ElectionAdmin admin;
    private static BallotBox ballotBox;
    private static Registrar registrar;
    private static Trustee trustee;

    public static Result setup() {
        admin = new ElectionAdmin();
        election = admin.setup();
        ballotBox = new BallotBox();
        registrar = new Registrar();
        trustee = new Trustee();
        flash("success", "Election setup");
        return redirect(routes.Application.home());
    }

    public static Result register() {
        String email = session("email");
        try {
            Voter voter = registrar.registerVoter(email, election, admin);
        } catch (NoSuchAlgorithmException e) {
            // This error should never occur
            flash("error", "Invalid reg");
        }
        flash("success", "Registered");
        return redirect(routes.Application.home());
    }

    public static Result vote() {
        Form<Vote> formData = Form.form(Vote.class).bindFromRequest();
        if (formData.hasErrors()) {
            return badRequest(views.html.voter.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), formData));
        } else {
            Vote vote = formData.get();
            String email = session("email");
            Optional<Voter> voterById = admin.getVoterById(email);
            voterById.ifPresent(voter -> {
                try {
                    Ballot ballot = vote("1", voter);
                    Ballot negativeBallot = vote("0", voter);

                    if (vote.getVoteChoice().equals("0")) {
                        // Vote for BB1
                        ballotBox.appendBB1(ballot);
                        ballotBox.appendBB2(negativeBallot);
                    } else {
                        // else vote for BB2
                        ballotBox.appendBB2(ballot);
                        ballotBox.appendBB1(negativeBallot);
                    }
                    voter.setVoted(true);
                } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | UnsupportedEncodingException e) {
                    // This should never happen
                    flash("error", "invalid vote");
                }


            });
            flash("success", "Voted");
            return redirect(routes.Application.home());

        }

    }

    public static Result tallyResults() {
        BulletinBoard bulletinBoard1 = ballotBox.getBulletinBoard1();
        BulletinBoard bulletinBoard2 = ballotBox.getBulletinBoard2();
        BigInteger tally1 = trustee.tally(bulletinBoard1, election);
        BigInteger tally2 = trustee.tally(bulletinBoard2, election);
        election.setEnded(true);
        String displayValue1 = String.valueOf(tally1);
        String displayValue2 = String.valueOf(tally2);
        return ok(tally.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), displayValue1, displayValue2));
    }

    // TODO: NOT working
    public static Result verifyVote() {
        String email = session("email");
        Optional<Voter> voterById = admin.getVoterById(email);

        try {
            if (Crypto.verifyVote(voterById.get(), ballotBox)) {
                flash("Success");
                return ok(index.render("E-Voting", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx())));
            } else
                return ok("Not found");
        } catch (UnsupportedEncodingException | SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            flash("Error", "Whilst verifying");
            return badRequest("Error");
        }


    }

    private static Ballot vote(String vote, Voter voter) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        BigInteger voteChoice = new BigInteger(vote);
        BigInteger plaintext = election.getGenerator().modPow(voteChoice, election.getPrime());

        return Crypto.encrypt(plaintext, voter, election);
    }
}
