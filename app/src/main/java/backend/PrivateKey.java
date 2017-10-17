package backend;

import java.math.BigInteger;
import java.util.Random;

import Util.Util;

import static Util.Util.stringToBigInt;

/**
 * Created by ich on 10.10.2017.
 */

public class PrivateKey extends Key {

    public PublicKey publicKey;

    public PrivateKey(BigInteger key, BigInteger modul) {
        value = key;
        this.modul = modul;
    }

    public static PrivateKey generateRandomKey() {
        Random random = new Random();
        // generate needed numbers
        BigInteger factor1 = BigInteger.probablePrime(Const.KEY_BIT_LENGTH, random);
        BigInteger factor2 = BigInteger.probablePrime(Const.KEY_BIT_LENGTH, random);
        BigInteger modulN = factor1.multiply(factor2);
        BigInteger phiN = factor1.subtract(BigInteger.ONE).multiply(factor2.subtract(BigInteger.ONE));
        BigInteger e;
        do {
            e = new BigInteger(Const.KEY_BIT_LENGTH, random);
        } while (phiN.subtract(e).signum() == 1 && phiN.remainder(e) != BigInteger.ZERO);
        BigInteger d = e.modInverse(phiN);

        // instantiate keys
        PrivateKey re = new PrivateKey(e,modulN);
        re.setPublicKey(new PublicKey(d, modulN));
        return re;
    }

    public static PrivateKey generateSpecificKey(String decryptionKey) {
        Random random = new Random();
        // generate needed numbers
        BigInteger factor1 = BigInteger.probablePrime(Const.KEY_BIT_LENGTH, random);
        BigInteger factor2 = BigInteger.probablePrime(Const.KEY_BIT_LENGTH, random);
        BigInteger modulN = factor1.multiply(factor2);
        BigInteger phiN = factor1.subtract(BigInteger.ONE).multiply(factor2.subtract(BigInteger.ONE));
        BigInteger e = Util.stringToBigInt(decryptionKey);
        BigInteger d = e.modInverse(phiN);

        PrivateKey priv = new PrivateKey(e, modulN);
        priv.setPublicKey(new PublicKey(d, modulN));
        return priv;
    }

    public String decrypt(String message) {
        BigInteger mes = Util.stringToBigInt(message);
        BigInteger mesDec = mes.modPow(getValue(), getModul());
        return Util.bigIntToString(mesDec);
    }

    public String sign(String message) {
        return decrypt(message);
    }

    public void setPublicKey(PublicKey pubKey) {
        this.publicKey = pubKey;
    }
}
