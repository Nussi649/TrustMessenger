package backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piddnbuddn.we.trustmessenger.R;

import backend.be.IncMessageBE;
import backend.be.MessageBE;
import backend.be.OutMessageBE;

/**
 * Created by ich on 21.10.2017.
 */

public class MessageSegment extends LinearLayout {
    MessageBE message;
    Context context;
    java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat(Const.TIME_FORMAT);

    public MessageSegment(Context context, MessageBE msg) {
        super(context);
        this.context = context;
        message = msg;
        populateUI();
    }

    private void populateUI() {
        if (message instanceof IncMessageBE) {
            LayoutInflater.from(context).inflate(R.layout.conversation_message_segment_in, this);
            TextView status = (TextView)findViewById(R.id.message_segment_status);
            status.setText(R.string.message_segment_incoming_status);

        } else if (message instanceof OutMessageBE) {
            LayoutInflater.from(context).inflate(R.layout.conversation_message_segment_out, this);
            TextView status = (TextView)findViewById(R.id.message_segment_status);
            status.setText(((OutMessageBE) message).status.toString());
        }
        TextView content = (TextView)findViewById(R.id.message_segment_content);
        TextView time = (TextView)findViewById(R.id.message_segment_time);

        content.setText(message.content);
        time.setText(timeFormat.format(message.timeSent));
    }
}
