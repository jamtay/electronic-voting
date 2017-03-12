package controllers;

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

    public Optional<Election> setup() {
        BigInteger generator = new BigInteger("2");
        Random secureRandom = new SecureRandom();
        BigInteger prime = BigInteger.probablePrime(64, secureRandom);
        KeyGenerator keyGenerator = new KeyGenerator();
        KeyPair keyPair = keyGenerator.serverKeyGen(generator, prime);

        return Optional.of(new Election(prime, generator, secureRandom, keyPair, true, false));
    }

    public void addNewVoter(Voter voter) {
        this.registeredVoters.add(voter);
    }

    public Optional<Voter> getVoterById(String id) {
        return this.registeredVoters.stream().filter(voter -> voter.getId().equals(id)).findAny();
    }

}
