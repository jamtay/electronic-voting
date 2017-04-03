package controllers;

import models.Election;
import models.KeyPair;
import models.Voter;

import java.security.NoSuchAlgorithmException;

public class Registrar {

    /**
     * Create encryption and signing key pair for voter
     * @param id
     * @param election
     * @param admin
     * @return registered voter
     * @throws NoSuchAlgorithmException
     */
    public Voter registerVoter(String id, Election election, ElectionAdmin admin) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = new KeyGenerator();
        KeyPair votersKeyPair = keyGenerator.eKeyGen(election);
        java.security.KeyPair signingKeyPair = keyGenerator.sKeyGen();

        Voter voter = new Voter(id, votersKeyPair, signingKeyPair, true, false);
        admin.addNewVoter(voter);
        return voter;

    }
}
