package backend;

import java.math.BigInteger;

/**
 * Created by ich on 10.10.2017.
 */

public abstract class Key {
    protected BigInteger value;
    protected BigInteger modul;

    public BigInteger getValue() {
        return value;
    }

    public BigInteger getModul() {
        return modul;
    }
}
