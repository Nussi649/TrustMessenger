package backend.be;

import java.util.Date;

/**
 * Created by ich on 16.10.2017.
 */

public abstract class MessageBE {
    public String content;
    public Date timeSent;
    public int chatID;

    public abstract ContactBE getPartner();

    public int getChatID() {
        return chatID;
    }
}
