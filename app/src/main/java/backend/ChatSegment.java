package backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piddnbuddn.we.trustmessenger.R;

import backend.be.ChatBE;

/**
 * Created by ich on 15.10.2017.
 */

public class ChatSegment extends LinearLayout {

    Context context;
    ChatBE content;

    public ChatSegment(Context context, ChatBE chat) {
        super(context);
        this.context = context;
        content = chat;
        populateUI();
    }

    private void populateUI() {
        LayoutInflater.from(context).inflate(R.layout.overview_chat_segment, this);
        ImageView portrait = (ImageView)findViewById(R.id.chatPortrait);
        TextView headerText = (TextView)findViewById(R.id.headerTextView);
        TextView bodyText = (TextView)findViewById(R.id.bodyTextView);
        this.setBackgroundColor(100010);

        headerText.setText(content.name);
    }
}
