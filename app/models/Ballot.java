package models;

import java.math.BigInteger;

public class Ballot {
    private BigInteger alpha;
    private BigInteger beta;

    public Ballot(BigInteger alpha, BigInteger beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    public BigInteger getAlpha() {
        return alpha;
    }

    public BigInteger getBeta() {
        return beta;
    }
}
