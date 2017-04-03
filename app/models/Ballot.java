package models;

import java.math.BigInteger;

/**
 * A ballot containing servers public key, encrypted vote and signature of user's id
 */
public class Ballot {
    private BigInteger alpha;
    private BigInteger beta;
    private byte[] signature;

    public Ballot(BigInteger alpha, BigInteger beta, byte[] signature) {
        this.alpha = alpha;
        this.beta = beta;
        this.signature = signature;
    }

    public BigInteger getAlpha() {
        return alpha;
    }

    public BigInteger getBeta() {
        return beta;
    }

    public byte[] getSignature() {
        return signature;
    }
}
