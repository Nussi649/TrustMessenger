package backend;

import android.content.res.Resources;

import java.util.List;

import backend.be.ChatBE;
import backend.be.ContactBE;

/**
 * Created by ich on 07.10.2017.
 */

public class Model {

    public PublicKey publicKey;
    public PrivateKey privateKey;
    public String username;
    public String password;
    public String DATABASE_NAME;

    public List<ContactBE> contacts;
    public List<ChatBE> chats;

    public ChatBE curChat;
    public ContactBE curContact;
}
