package backend.be;

/**
 * Created by ich on 15.10.2017.
 */

public class ChatBE {
    public ContactBE partner;
    public String name;

    public ChatBE() {}

    public ChatBE(String name) {
        this.name = name;
    }

    public ChatBE(ContactBE partner) {
        this.partner = partner;
        this.name = partner.name;
    }
}
