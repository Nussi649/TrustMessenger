package backend;

import java.util.Random;

/**
 * Created by ich on 10.10.2017.
 */

public class PublicKey extends Key {

    public PublicKey(long key) {
        value = key;
    }

    public static PublicKey calculateFromPrivateKey(PrivateKey privKey) {
        PublicKey re = new PublicKey(10);
        // TODO: implement logic
        return re;
    }

    public String encrypt(String message) {
        // TODO: implement logic (RSA)
        return "";
    }
}
