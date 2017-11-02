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

    public static String secondsToString(int seconds) {
        String re = "";
        int value = seconds;
        if (value >= 60) {
            int rest = value % 60;
            if (rest != 0) {
                re = rest + "s" + re;
            }
            value = (value - rest) / 60;
        } else {
            return value + "s";
        }
        if (value >= 60) {
            int rest = value % 60;
            if (rest != 0) {
                re = rest + "m" + re;
            }
            value = (value - rest) / 60;
        } else {
            return value + "m" + re;
        }
        if (value >= 24) {
            int rest = value % 24;
            if (rest != 0) {
                re = rest + "h" + re;
            }
            value = (value - rest) / 24;
        } else {
            return value + "h" + re;
        }
        return value + "d" + re;
    }
}
