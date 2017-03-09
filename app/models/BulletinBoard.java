package models;

import java.util.ArrayList;
import java.util.List;

public class BulletinBoard {

    private List<Ballot> bulletinBoard = new ArrayList<>();

    public void appendBallot(Ballot ballot) {
        bulletinBoard.add(ballot);
    }

    public List<Ballot> getBulletinBoard() {
        return bulletinBoard;
    }
}
