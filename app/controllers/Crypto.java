package controllers;

import models.Ballot;
import models.Election;
import models.Voter;

import java.io.UnsupportedEncodingException;

import java.math.BigInteger;
import java.security.*;

public class Crypto {

    /**
     * Encrypts the users vote choice using ElGamal encryption
     * @param plaintext plaintext of the vote
     * @param voter The voter who cast the vote
     * @param election Details of the election
     * @return The encrypted ballot
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static Ballot encrypt(BigInteger plaintext, Voter voter, Election election) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        BigInteger beta = plaintext.multiply(election.getServerKeyPair().getPublicKey().modPow(
                voter.getEncryptionKeyPair().getPrivateKey(), election.getPrime())).mod(election.getPrime());
        BigInteger alpha = election.getGenerator().modPow(voter.getEncryptionKeyPair().getPrivateKey(), election.getPrime());

        byte[] signature = getSignature(voter);
        return new Ballot(alpha, beta, signature);
    }

    /**
     * Decrypt the vote to return the election tally
     * @param alphaTally
     * @param betaTally
     * @param election
     * @return The decrypted plaintext
     */
    public static BigInteger decrypt(BigInteger alphaTally, BigInteger betaTally, Election election) {

        BigInteger alphaInverseX = alphaTally.modPow(election.getServerKeyPair().getPrivateKey(), election.getPrime()).modInverse(election.getPrime());
        return alphaInverseX.multiply(betaTally).mod(election.getPrime());
    }

    /**
     * Check the bulletin board to verify if a ballot contains the voters signature
     * @param voter
     * @param ballotBox
     * @return True/False of if the bulletin board contains a certain users vote
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyVote(Voter voter, BallotBox ballotBox) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        byte[] voterId = voter.getId().getBytes("UTF8");
        Signature sig = Signature.getInstance("MD5WithRSA");
        sig.initVerify(voter.getSigningKeyPair().getPublic());
        sig.update(voterId);

        //Clone the signature so that the original is not changed when checking bulletin board 1
        Signature sigClone = Signature.getInstance("MD5WithRSA");
        sigClone.initVerify(voter.getSigningKeyPair().getPublic());
        sigClone.update(voterId);

        // Check Bulletin board1 for the users signature
        boolean voteExistsBB1 = ballotBox.getBulletinBoard1().getBulletinBoard().stream().anyMatch(ballot -> {
            try {
                return sig.verify(ballot.getSignature());
            } catch (SignatureException e) {
                return false;
            }
        });

        // Check Bulletin board2 for the users signature
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
