package backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piddnbuddn.we.trustmessenger.R;

import backend.be.ChatBE;
import backend.be.MessageBE;

/**
 * Created by ich on 15.10.2017.
 */

public class ChatSegment extends LinearLayout {

    Context context;
    ChatBE content;
    MessageBE preview;

    public ChatSegment(Context context, ChatBE chat, MessageBE preview) {
        super(context);
        this.context = context;
        content = chat;
        this.preview = preview;
        populateUI();
    }

    private void populateUI() {
        LayoutInflater.from(context).inflate(R.layout.overview_chat_segment, this);
        ImageView portrait = (ImageView)findViewById(R.id.chatPortrait);
        TextView headerText = (TextView)findViewById(R.id.headerTextView);
        TextView bodyText = (TextView)findViewById(R.id.bodyTextView);
        this.setBackgroundColor(100010);
        bodyText.setText(preview.content);
        headerText.setText(content.name);
    }
}
