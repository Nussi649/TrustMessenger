package backend.be;

/**
 * Created by ich on 15.10.2017.
 */

public class ChatBE {
    public ContactBE partner;
    public int id;
    public String name;
    public int destructionTimer;

    public ChatBE() {}

    public ChatBE(int id) {this.id = id;}

    public ChatBE(String name) {
        this.name = name;
    }

    public ChatBE(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChatBE(int id, ContactBE partner) {
        this.partner = partner;
        this.id = id;
        this.name = partner.name;
    }

    public void setTimer(int timer) {
        destructionTimer = timer;
    }
}
