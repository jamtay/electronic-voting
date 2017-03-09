package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.tally;

import java.math.BigInteger;
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
        registrar.registerVoter(email, election, admin);
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
                Ballot ballot = vote("1", voter);
                Ballot negativeBallot = vote("0", voter);

                if (vote.getVoteChoice().equals("0")) {
                    System.out.println("VOTE BB1 SUCCESS");
                    // Vote for BB1
                    ballotBox.appendBB1(ballot);
                    ballotBox.appendBB2(negativeBallot);
                } else {
                    System.out.println("VOTE BB2 Success");
                    // else vote for BB2
                    ballotBox.appendBB2(ballot);
                    ballotBox.appendBB1(negativeBallot);
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
        String displayValue1 = String.valueOf(tally1);
        String displayValue2 = String.valueOf(tally2);
        return ok(tally.render("Home", Secured.isLoggedIn(ctx()), Secured.getUserInfo(ctx()), displayValue1, displayValue2));
    }

    private static Ballot vote(String vote, Voter voter) {
        BigInteger voteChoice = new BigInteger(vote);
        BigInteger plaintext = election.getGenerator().modPow(voteChoice, election.getPrime());

        Ballot ballot = Crypto.encrypt(plaintext, voter, election);
        return ballot;
    }
}
