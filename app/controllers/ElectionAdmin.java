package controllers;

import models.BulletinBoard;
import models.Election;
import models.KeyPair;
import models.Voter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ElectionAdmin {

    private List<Voter> registeredVoters;

    public ElectionAdmin() {
        this.registeredVoters = new ArrayList<>();
    }

    public Election setup() {
        BigInteger generator = new BigInteger("2");
        Random secureRandom = new SecureRandom();
        BigInteger prime = BigInteger.probablePrime(64, secureRandom);
        KeyGenerator keyGenerator = new KeyGenerator();
        KeyPair keyPair = keyGenerator.eKeyGen(generator, prime);

        return new Election(prime, generator, secureRandom, keyPair);
    }

    public void addNewVoter(Voter voter) {
        this.registeredVoters.add(voter);
    }

    public Optional<Voter> getVoterById(String id) {
        return this.registeredVoters.stream().filter(voter -> voter.getId().equals(id)).findAny();
    }

}
