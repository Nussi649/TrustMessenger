package Util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ich on 14.10.2017.
 */

public class Util {

    public static String sha256(String s) {
        try {
            // Create SHA-256 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static BigInteger stringToBigInt(String s) {
        byte[] bytes = s.getBytes();
        return new BigInteger(bytes);
    }

    public static String bigIntToString(BigInteger b) {
        byte[] bytes = b.toByteArray();
        return new String(bytes);
    }
}
