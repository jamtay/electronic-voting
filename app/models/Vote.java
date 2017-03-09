package models;

import play.data.validation.Constraints;


public class Vote {

    @Constraints.Required
    protected String voteChoice;

    public String getVoteChoice() {
        return voteChoice;
    }

    public void setVoteChoice(String voteChoice) {
        this.voteChoice = voteChoice;
    }
}
