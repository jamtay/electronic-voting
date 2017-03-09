package models;

import java.math.BigInteger;
import java.util.Random;

public class Election {
    private BigInteger prime, generator;
    private Random secureRandom;
    private KeyPair serverKeyPair;

    public Election(BigInteger prime, BigInteger generator, Random secureRandom, KeyPair serverKeyPair) {
        this.prime = prime;
        this.generator = generator;
        this.secureRandom = secureRandom;
        this.serverKeyPair = serverKeyPair;
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
}
