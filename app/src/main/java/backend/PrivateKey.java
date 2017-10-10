package backend;

import java.util.Random;

/**
 * Created by ich on 10.10.2017.
 */

public class PrivateKey extends Key {

    public PrivateKey(long key) {
        value = key;
    }
    public static PrivateKey generateRandomKey() {
        Random random = new Random();
        // TODO: generate random key
        long newKey = random.nextLong();
        return new PrivateKey(newKey);
    }

    public String decrypt(String message) {
        // TODO: implement logic (RSA)
        return "";
    }

    public String sign(String message) {
        return decrypt(message);
    }
}
