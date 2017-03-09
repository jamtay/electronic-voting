package controllers;

import models.Ballot;
import models.Election;
import models.Voter;

import java.math.BigInteger;

public class Crypto {
    public static Ballot encrypt(BigInteger plaintext, Voter voter, Election election) {

        BigInteger beta = plaintext.multiply(election.getServerKeyPair().getPublicKey().modPow(
                voter.getKeyPair().getPrivateKey(), election.getPrime())).mod(election.getPrime());
        BigInteger alpha = election.getGenerator().modPow(voter.getKeyPair().getPrivateKey(), election.getPrime());
        return new Ballot(alpha, beta);
    }

    public static BigInteger decrypt(BigInteger alphaTally, BigInteger betaTally, Election election) {

        BigInteger alphaInverseX = alphaTally.modPow(election.getServerKeyPair().getPrivateKey(), election.getPrime()).modInverse(election.getPrime());
        return alphaInverseX.multiply(betaTally).mod(election.getPrime());
    }
}
