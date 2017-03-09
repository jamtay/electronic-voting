package models;

import java.math.BigInteger;

public class KeyPair {
    private BigInteger privateKey;
    private BigInteger publicKey;

    public KeyPair(BigInteger privateKey, BigInteger publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    // TODO: Secure this so its not a getter
    // Could be done by each class has its own private key variable that needs it.  E.g voter and admin/election
    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }
}
