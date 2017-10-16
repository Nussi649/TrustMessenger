package backend;

import java.math.BigInteger;
import java.util.Random;

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
        BigInteger factor1 = BigInteger.probablePrime(512, random);
        BigInteger factor2 = BigInteger.probablePrime(512, random);
        BigInteger modulN = factor1.multiply(factor2);
        BigInteger phiN = factor1.subtract(BigInteger.ONE).multiply(factor2.subtract(BigInteger.ONE));
        BigInteger e;
        do {
            e = new BigInteger(512, random);
        } while (phiN.subtract(e).signum() == 1 && phiN.remainder(e) != BigInteger.ZERO);
        BigInteger d = e.modInverse(phiN);

        // instantiate keys
        PrivateKey re = new PrivateKey(e,modulN);
        re.setPublicKey(new PublicKey(d, modulN));
        return re;
    }

    public String decrypt(String message) {
        // TODO: implement logic (RSA)
        return "";
    }

    public String sign(String message) {
        return decrypt(message);
    }

    public void setPublicKey(PublicKey pubKey) {
        this.publicKey = pubKey;
    }
}
