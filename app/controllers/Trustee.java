package controllers;

import models.Ballot;
import models.BulletinBoard;
import models.Election;

import java.math.BigInteger;
import java.util.stream.Stream;

public class Trustee {

    /**
     * Tally the encrypted votes and decrypt the final tally for a candidate
     * @param publicBulletinBoard
     * @param election
     * @return Decrypted election tally for a candidate
     */
    public BigInteger tally(BulletinBoard publicBulletinBoard, Election election) {
        Stream<BigInteger> encryptedAlphas = publicBulletinBoard.getBulletinBoard().stream().map(Ballot::getAlpha);
        BigInteger alphaTally = encryptedAlphas.reduce(BigInteger.ONE, BigInteger::multiply);

        Stream<BigInteger> encryptedBetas = publicBulletinBoard.getBulletinBoard().stream().map(Ballot::getBeta);
        BigInteger betaTally = encryptedBetas.reduce(BigInteger.ONE, BigInteger::multiply);
        return Crypto.decrypt(alphaTally, betaTally, election);
    }
}
