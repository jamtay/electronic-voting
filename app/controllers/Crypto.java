package controllers;

import models.Ballot;
import models.Election;
import models.Voter;

import java.io.UnsupportedEncodingException;

import java.math.BigInteger;
import java.security.*;

public class Crypto {

    public static Ballot encrypt(BigInteger plaintext, Voter voter, Election election) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        BigInteger beta = plaintext.multiply(election.getServerKeyPair().getPublicKey().modPow(
                voter.getEncryptionKeyPair().getPrivateKey(), election.getPrime())).mod(election.getPrime());
        BigInteger alpha = election.getGenerator().modPow(voter.getEncryptionKeyPair().getPrivateKey(), election.getPrime());

        byte[] signature = getSignature(voter);
        return new Ballot(alpha, beta, signature);
    }

    public static BigInteger decrypt(BigInteger alphaTally, BigInteger betaTally, Election election) {

        BigInteger alphaInverseX = alphaTally.modPow(election.getServerKeyPair().getPrivateKey(), election.getPrime()).modInverse(election.getPrime());
        return alphaInverseX.multiply(betaTally).mod(election.getPrime());
    }

    public static boolean verifyVote(Voter voter, BallotBox ballotBox) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        byte[] voterId = voter.getId().getBytes("UTF8");
        Signature sig = Signature.getInstance("MD5WithRSA");
        sig.initVerify(voter.getSigningKeyPair().getPublic());
        sig.update(voterId);

        Signature sigClone = Signature.getInstance("MD5WithRSA");
        sigClone.initVerify(voter.getSigningKeyPair().getPublic());
        sigClone.update(voterId);

        boolean voteExistsBB1 = ballotBox.getBulletinBoard1().getBulletinBoard().stream().anyMatch(ballot -> {
            try {
                return sig.verify(ballot.getSignature());
            } catch (SignatureException e) {
                return false;
            }
        });

        boolean voteExistsBB2 = ballotBox.getBulletinBoard2().getBulletinBoard().stream().anyMatch(ballot -> {
            try {
                return sigClone.verify(ballot.getSignature());
            } catch (SignatureException e) {
                return false;
            }
        });

        return voteExistsBB1 && voteExistsBB2;
    }

    private static byte[] getSignature(Voter voter) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] data = voter.getId().getBytes("UTF8");
        Signature sig = Signature.getInstance("MD5WithRSA");
        sig.initSign(voter.getSigningKeyPair().getPrivate());
        sig.update(data);
        return sig.sign();
    }
}
