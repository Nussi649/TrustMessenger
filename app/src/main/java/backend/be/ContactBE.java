package backend.be;

import backend.PrivateKey;
import backend.PublicKey;

/**
 * Created by ich on 15.10.2017.
 */

public class ContactBE {

    int id;
    String name;
    PublicKey publicKey;

    public static ContactBE getDummyContact() {
        return new ContactBE("Testdummy", PrivateKey.generateRandomKey().publicKey, -12);
    }

    public ContactBE() { }

    public ContactBE(String name) {
        this.name = name;
    }

    public ContactBE(String name, PublicKey pubKey) {
        this.name = name;
        this.publicKey = pubKey;
    }

    public ContactBE(String name, PublicKey pubKey, int id) {
        this.name = name;
        this.publicKey = pubKey;
        this.id = id;
    }

    public void setPublicKey(PublicKey newPubKey) {
        publicKey = newPubKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
