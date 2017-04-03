package controllers;

import models.Ballot;
import models.BulletinBoard;

public class BallotBox {
    //Bulletinboards to store users votes for each candidate
    private BulletinBoard bulletinBoard1, bulletinBoard2;

    public BallotBox() {
        this.bulletinBoard1 = new BulletinBoard();
        this.bulletinBoard2 = new BulletinBoard();
    }

    public BulletinBoard getBulletinBoard1() {
        return bulletinBoard1;
    }

    public BulletinBoard getBulletinBoard2() {
        return bulletinBoard2;
    }

    public void appendBB1(Ballot ballot) {
        this.bulletinBoard1.appendBallot(ballot);
    }

    public void appendBB2(Ballot ballot) {
        this.bulletinBoard2.appendBallot(ballot);
    }
}
