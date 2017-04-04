package controllers;

import models.Election;
import models.KeyPair;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {

    /**
     * Generate keys for server
     * @param generator
     * @param prime
     * @return
     */
    public KeyPair serverKeyGen(BigInteger generator, BigInteger prime) {
        BigInteger privateKey = new BigInteger("12345678901234567890");

        // public key calculation for Server
        BigInteger publicKey = generator.modPow(privateKey, prime);
        return new KeyPair(privateKey, publicKey);
    }

    /**
     * Generate ElGamal encryption keys for voters
     * @param election election details
     * @return Voter encryption keyPair
     */
    public KeyPair eKeyGen(Election election) {
        BigInteger privateKey = new BigInteger(64, election.getSecureRandom());
        BigInteger publicKey = election.getGenerator().modPow(privateKey, election.getPrime());

        return new KeyPair(privateKey, publicKey);
    }

    /**
     * Generate RSA signing keys for voters
     * @return Voter signing keyPair
     */
    public java.security.KeyPair sKeyGen() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        return kpg.genKeyPair();
    }
}
