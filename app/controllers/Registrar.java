package controllers;

import models.Election;
import models.KeyPair;
import models.Voter;

public class Registrar {
    public Voter registerVoter(String id, Election election, ElectionAdmin admin) {
        KeyGenerator keyGenerator = new KeyGenerator();
        KeyPair votersKeyPair = keyGenerator.sKeyGen(election);

        Voter voter = new Voter(id, votersKeyPair);
        admin.addNewVoter(voter);
        return voter;

    }
}
