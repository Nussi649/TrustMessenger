package backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piddnbuddn.we.trustmessenger.R;

import backend.be.ContactBE;

/**
 * Created by ich on 17.10.2017.
 */

public class CListSegment extends LinearLayout {
    Context context;
    ContactBE contact;

   public CListSegment(Context context, ContactBE contact) {
       super(context);
       this.context = context;
       this.contact = contact;
       populateUI();
   }

   private void populateUI() {
       LayoutInflater.from(context).inflate(R.layout.contact_list_segment, this);
       TextView name = (TextView)findViewById(R.id.clist_segment_username);
       name.setText(contact.getName());
   }
}
