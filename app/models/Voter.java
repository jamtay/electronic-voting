package models;

public class Voter {

    private String id;
    private KeyPair encryptionKeyPair;
    private java.security.KeyPair signingKeyPair;
    private boolean registered, voted;

    public Voter(String id, KeyPair encryptionKeyPair, java.security.KeyPair signingKeyPair, boolean registered, boolean voted) {
        this.id = id;
        this.encryptionKeyPair = encryptionKeyPair;
        this.signingKeyPair = signingKeyPair;
        this.registered = registered;
        this.voted = voted;
    }

    public KeyPair getEncryptionKeyPair() {
        return encryptionKeyPair;
    }

    public java.security.KeyPair getSigningKeyPair() {
        return signingKeyPair;
    }

    public String getId() {
        return id;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }
}
