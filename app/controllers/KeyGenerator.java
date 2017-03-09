package controllers;

import models.Election;
import models.KeyPair;

import java.math.BigInteger;

public class KeyGenerator {
    public KeyPair eKeyGen(BigInteger generator, BigInteger prime) {
        BigInteger privateKey = new BigInteger("12345678901234567890");

        // public key calculation for Server
        BigInteger publicKey = generator.modPow(privateKey, prime);
        return new KeyPair(privateKey, publicKey);
    }

    public KeyPair sKeyGen(Election election) {
        BigInteger privateKey = new BigInteger(64, election.getSecureRandom());
        BigInteger publicKey = election.getGenerator().modPow(privateKey, election.getPrime());

        return new KeyPair(privateKey, publicKey);
    }
}
