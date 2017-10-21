package backend.be;

import java.util.Date;

/**
 * Created by ich on 16.10.2017.
 */

public class IncMessageBE extends MessageBE {
    public ContactBE sender;

    public IncMessageBE(String content, Date time, ContactBE sender) {
        this.content = content;
        this.timeSent = time;
        this.sender = sender;
    }

    @Override
    public ContactBE getPartner() {
        return sender;
    }
}
