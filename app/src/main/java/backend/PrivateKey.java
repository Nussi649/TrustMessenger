package backend;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by ich on 10.10.2017.
 */

public class PrivateKey extends Key {

    public PrivateKey(BigInteger key) {
        value = key;
    }
    public static PrivateKey generateRandomKey() {
        Random random = new Random();
        // TODO: generate random key
        BigInteger factor1 = BigInteger.probablePrime(512, random);
        BigInteger factor2 = BigInteger.probablePrime(512, random);
        return new PrivateKey(factor1.multiply(factor2));
    }

    public String decrypt(String message) {
        // TODO: implement logic (RSA)
        return "";
    }

    public String sign(String message) {
        return decrypt(message);
    }
}
