package models;

public class Voter {

    private String id;
    private KeyPair keyPair;

    public Voter(String id, KeyPair keyPair) {
        this.id = id;
        this.keyPair = keyPair;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public String getId() {
        return id;
    }
}
