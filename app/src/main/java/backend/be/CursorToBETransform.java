package backend.be;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import Util.Util;
import backend.Const;
import backend.Controller;
import backend.PublicKey;

/**
 * Created by ich on 18.10.2017.
 */

public abstract class CursorToBETransform {

    public static List<ContactBE> transformToContactList(Cursor cursor) {
        if (cursor.moveToFirst()) {
            List<ContactBE> re = new ArrayList<>();
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String sKey = cursor.getString(2);
                String sModul = cursor.getString(3);
                ContactBE con = new ContactBE(name, new PublicKey(Util.stringToBigInt(sKey), Util.stringToBigInt(sModul)), id);
                re.add(con);
            } while (cursor.moveToNext());
            return re;
        }
        return null;
    }

    public static List<ChatBE> transformToChatList(Cursor cursor, Controller con) {
        if (cursor.moveToFirst()) {
            List<ChatBE> re = new ArrayList<>();
            do {
                // 0 id, 1 partner, 2 timer
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int timer = cursor.getInt(2);
                ContactBE partner = con.getContactByName(name);
                if (partner == null) {
                    partner = ContactBE.getDummyContact();
                }
                ChatBE chat = new ChatBE(id, partner);
                chat.setTimer(timer);
                re.add(chat);

            } while (cursor.moveToNext());
            return re;
        }
        return null;
    }

    public static MessageBE transformToSingleMessage(Cursor cursor, Controller con) {
        if (cursor.moveToFirst()) {
            // 0 id, 1 inOut, 2 partner, 3 content, 4 timestamp
            int id = cursor.getInt(0);
            String inOut = cursor.getString(1);
            int chatId = cursor.getInt(2);
            String content = cursor.getString(3);
            Date time;
            try {
                time = new SimpleDateFormat(Const.DATETIME_FORMAT).parse(cursor.getString(4));
            } catch (ParseException pe) {
                pe.printStackTrace();
                time = new Date();
            }
            if (inOut.equals("i")) {
                return new IncMessageBE(content, time, con.getContactById(chatId));
            } else if (inOut.equals("o")) {
                return new OutMessageBE(content, time, con.getContactById(chatId));
            }
        }
        return null;
    }

    public static List<MessageBE> transformToMessageList(Cursor cursor, Controller con) {
        List<MessageBE> re = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String inOut = cursor.getString(1);
                int chatId = cursor.getInt(2);
                String content = cursor.getString(3);
                Date time;
                try {
                    time = new SimpleDateFormat(Const.DATETIME_FORMAT).parse(cursor.getString(4));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                    time = new Date();
                }
                if (inOut.equals("i")) {
                    re.add(new IncMessageBE(content, time, con.getContactById(chatId)));
                } else if (inOut.equals("o")) {
                    re.add(new OutMessageBE(content, time, con.getContactById(chatId)));
                }
            } while (cursor.moveToNext());
        }
        return null;
    }

    public static int transformToSequenceValue(Cursor cursor) {
        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }
        return -1;
    }
}
