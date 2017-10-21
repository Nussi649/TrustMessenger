package backend.be;

import java.util.Date;

/**
 * Created by ich on 16.10.2017.
 */

public class OutMessageBE extends MessageBE {
    public ContactBE recipient;

    public OutMessageBE(String content, Date time, ContactBE recipient) {
        this.content = content;
        this.timeSent = time;
        this.recipient = recipient;
    }

    @Override
    public ContactBE getPartner() {
        return recipient;
    }
}
