package backend;

import java.math.BigInteger;
import java.util.Random;

import Util.Util;

/**
 * Created by ich on 10.10.2017.
 */

public class PublicKey extends Key {

    public PublicKey(BigInteger key, BigInteger modul) {
        value = key;
        this.modul = modul;
    }

    public String encrypt(String message) {
        BigInteger mes = Util.stringToBigInt(message);
        BigInteger mesEnc = mes.modPow(getValue(), getModul());
        return Util.bigIntToString(mesEnc);
    }
}
