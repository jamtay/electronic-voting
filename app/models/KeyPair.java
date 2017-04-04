package models;

import java.math.BigInteger;

public class KeyPair {
    private BigInteger privateKey;
    private BigInteger publicKey;

    public KeyPair(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }
}
