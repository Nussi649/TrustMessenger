package backend;

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

    public List<ContactBE> contacts;
    public List<ChatBE> chats;

    public ChatBE curChat;
}
