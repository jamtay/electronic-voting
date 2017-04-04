package models;

import java.math.BigInteger;
import java.util.Random;

public class Election {
    private BigInteger prime, generator;
    private Random secureRandom;
    private KeyPair serverKeyPair;
    private boolean started, ended;

    public Election(BigInteger prime, BigInteger generator, Random secureRandom, KeyPair serverKeyPair, boolean started, boolean ended) {
        this.prime = prime;
        this.generator = generator;
        this.secureRandom = secureRandom;
        this.serverKeyPair = serverKeyPair;
        this.started = started;
        this.ended = ended;
    }

    public BigInteger getPrime() {
        return prime;
    }

    public BigInteger getGenerator() {
        return generator;
    }

    public Random getSecureRandom() {
        return secureRandom;
    }

    public KeyPair getServerKeyPair() {
        return serverKeyPair;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }
}
