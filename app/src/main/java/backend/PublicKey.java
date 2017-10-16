package backend;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by ich on 10.10.2017.
 */

public class PublicKey extends Key {

    public PublicKey(BigInteger key, BigInteger modul) {
        value = key;
        this.modul = modul;
    }

    public String encrypt(String message) {
        // TODO: implement logic (RSA)
        return "";
    }
}
