package backend;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.piddnbuddn.we.trustmessenger.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import Util.Util;
import Util.FeedReaderContract;
import backend.be.ChatBE;
import backend.be.ContactBE;
import backend.be.CursorToBETransform;
import backend.be.IncMessageBE;
import backend.be.MessageBE;
import backend.be.OutMessageBE;

import static Util.Util.sha256;

/**
 * Created by ich on 07.10.2017.
 */

public class Controller {

    public static Controller instance;
    private Resources resources;
    Model model;
    SQLiteDatabase db;
    SimpleDateFormat sdf = new SimpleDateFormat(Const.DATETIME_FORMAT);
    HttpURLConnection httpClient = null;

    private Controller() {

    }

    private void initController() {
        model = new Model();
    }

    public static void createInstance() {
        instance = new Controller();
        instance.initController();
    }

    public Model getModel() {
        return model;
    }

    public void resetModel() {
        model = new Model();
    }

    // region Input/Output
    public boolean writeInternal(Context context, String file, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteInternal(Context context, String file) {
        File dir = context.getFilesDir();
        return new File(dir, file).delete();
    }

    public String readInternal(Context context, String file) {
        try {
            FileInputStream fis = context.openFileInput(file);
            return readInputStream(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return "";
    }

    public String readInputStream(FileInputStream fis) {
        try {
            StringBuffer fileContent = new StringBuffer("");
            byte[] buffer = new byte[1024];
            int n = 0;
            while ((n = fis.read(buffer)) != -1)
            {
                fileContent.append(new String(buffer, 0, n));
            }
            return fileContent.toString();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return "";
    }
    // endregion

    // region Resources
    public void setResources(Resources res) {
        resources = res;
    }

    private String getString(int ResID) {
        return resources.getString(ResID);
    }
    // endregion

    // region Password/Username
    public void setUsername(String username) {
        model.username = username;
    }

    public boolean checkPasswordUsage() {
        if (model.password == null) {
            return true;
        } else if (model.password == "") {
            return true;
        }
        return false;
    }

    public boolean setPassword(String newPassword, Context context) {
        model.password = sha256(newPassword);
        return writeInternal(context, Const.FILENAME_PASSWORD, newPassword);
    }

    public boolean deleteKey(String username, Context context) {
        // SEND TO SERVER THAT KEY WILL NO LONGER BE USED

        // DELETE DATABASE
        deleteInternal(context, username + Const.DATABASE_NAME_SUFFIX);
        return false;
    }
    // endregion

    // region Database
    public void setDb(SQLiteDatabase database) {
        db = database;
        resetDatabase();
        if (DatabaseUtils.queryNumEntries(db, "sequences") == 0) {
            initiateSequenceValues();
            setContacts();
            loadContactsFromDB();
            setChats();
            loadChatsFromDB();
            setMessages();
        }
    }

    private void initiateSequenceValues() {
        boolean result = true;
        db.beginTransaction();
        String sql1 = "INSERT INTO sequences(seq_val,tablen) VALUES (0,'contacts')";
        String sql2 = "INSERT INTO sequences(seq_val,tablen) VALUES (0,'messages')";
        String sql3 = "INSERT INTO sequences(seq_val,tablen) VALUES (0,'chats')";
        try {
            db.execSQL(sql1);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        try {
            db.execSQL(sql2);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        try {
            db.execSQL(sql3);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    private int getSequenceValue(String table) {
        String sql = "SELECT seq_val FROM sequences WHERE " + FeedReaderContract.FeedEntrySequence.COLUMN_TABLE + "='" + table + "'";
        db.beginTransaction();
        int seqVal = CursorToBETransform.transformToSequenceValue(db.rawQuery(sql, null));
        seqVal++;
        boolean result = true;
        try {
            db.execSQL("UPDATE sequences SET seq_val='" + seqVal + "' WHERE " + FeedReaderContract.FeedEntrySequence.COLUMN_TABLE + "='" + table + "'");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return seqVal;
    }

    public void loadContactsFromDB() {
        String sql = "SELECT * FROM contacts";
        db.beginTransaction();
        Cursor cursor = db.rawQuery(sql, null);
        model.contacts = CursorToBETransform.transformToContactList(cursor);
        if (model.contacts != null) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    public void loadChatsFromDB() {
        String sql = "SELECT * FROM chats";
        db.beginTransaction();
        Cursor cursor = db.rawQuery(sql, null);
        model.chats = CursorToBETransform.transformToChatList(cursor, this);
        if (model.chats != null) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    public ChatBE saveNewChatToDB(ContactBE contact) {
        String table_name = FeedReaderContract.FeedEntryChats.TABLE_NAME;
        int seqVal = getSequenceValue(table_name);
        ChatBE newChat = new ChatBE(seqVal, contact);
        newChat.setTimer(Const.DEFAULT_DESTRUCTION_TIMER);
        String sql = "INSERT INTO " + table_name + " (id, partner, timer) VALUES (" +
                seqVal + ",'" +
                newChat.name + "'," +
                newChat.destructionTimer + ")";
        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return null;
        }
        db.endTransaction();
        model.chats.add(newChat);
        return newChat;
    }

    public MessageBE loadFirstMessageOfChatFromDB(int id) {
        String sql = "SELECT * FROM messages WHERE partner=" + id + " ORDER BY id DESC";
        db.beginTransaction();
        Cursor cursor = db.rawQuery(sql, null);
        MessageBE message = CursorToBETransform.transformToSingleMessage(cursor, this);
        if (message != null) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return message;
    }

    public List<MessageBE> loadMessagesOfChatFromDB(int id) {
        String sql = "SELECT * FROM messages WHERE partner=" + id;
        db.beginTransaction();
        Cursor cursor = db.rawQuery(sql, null);
        List<MessageBE> re = CursorToBETransform.transformToMessageList(cursor, this);
        if (re != null) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return re;
    }

    public ContactBE getContactFromDB(String username) {
        String sql = "SELECT FROM " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntryContacts.COLUMN_NAME + "='" +
                username + "'";
        Cursor cursor = db.rawQuery(sql, null);
        return CursorToBETransform.transformToContact(cursor);
    }

    public ContactBE getContactFromDB(int id) {
        String sql = "SELECT FROM " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntryContacts.COLUMN_ID + "=" +
                id;
        Cursor cursor = db.rawQuery(sql, null);
        return CursorToBETransform.transformToContact(cursor);
    }

    public boolean saveContactToDB(ContactBE newContact) {
        if (getContactFromDB(newContact.getName()) != null) {
            return false;
        }
        boolean result = true;
        String sql = "INSERT INTO " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + "(id,name,pub_key,modul) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryContacts.TABLE_NAME) + " , '" +
                newContact.getName() + "' , '" +
                Util.bigIntToString(newContact.getPublicKey().getValue()) + "' , '" +
                Util.bigIntToString(newContact.getPublicKey().getModul()) + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        return result;
    }

    private void resetDatabase() {
        db.execSQL(FeedReaderContract.FeedEntryContacts.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryMessages.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryChats.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryContacts.SQL_CREATE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryMessages.SQL_CREATE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_CREATE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryChats.SQL_CREATE_ENTRIES);
    }

    private void cheat() {
        String sql2 = "SELECT * FROM sequences";
        Cursor cursor = db.rawQuery(sql2, null);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(1);
        }
    }

    // region populate database
    public void setContacts() {
        boolean result = true;
        ContactBE terry = new ContactBE(getString(R.string.buddy_1), PrivateKey.generateRandomKey().publicKey);
        ContactBE john = new ContactBE(getString(R.string.buddy_2), PrivateKey.generateRandomKey().publicKey);
        ContactBE lara = new ContactBE(getString(R.string.buddy_3), PrivateKey.generateRandomKey().publicKey);
        ContactBE stacey = new ContactBE(getString(R.string.buddy_4), PrivateKey.generateRandomKey().publicKey);

        db.beginTransaction();
        String value = Const.DEFAULT_KEY_VALUE;
        String modul = Const.DEFAULT_KEY_MODUL;
        String sql = "INSERT INTO " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + "(id,name,pub_key,modul) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryContacts.TABLE_NAME) + " , '" +
                terry.getName() + "' , '" +
                value + "' , '" +
                modul + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        sql = "INSERT INTO " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + "(id,name,pub_key,modul) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryContacts.TABLE_NAME) + ",'" +
                john.getName() + "','" +
                value + "','" +
                modul + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        sql = "INSERT INTO " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + "(id,name,pub_key,modul) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryContacts.TABLE_NAME) + ",'" +
                lara.getName() + "','" +
                value + "','" +
                modul + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        sql = "INSERT INTO " + FeedReaderContract.FeedEntryContacts.TABLE_NAME + "(id,name,pub_key,modul) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryContacts.TABLE_NAME) + ",'" +
                stacey.getName() + "','" +
                value + "','" +
                modul + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    public void setChats() {
        boolean result = true;
        ChatBE terry = new ChatBE(0, getContactByName("Terry"));
        ChatBE john = new ChatBE(1, getContactByName("John"));
        ChatBE lara = new ChatBE(2, getContactByName("Lara"));
        ChatBE stacey = new ChatBE(3, getContactByName("Stacey"));

        db.beginTransaction();
        String sql = "INSERT INTO " + FeedReaderContract.FeedEntryChats.TABLE_NAME + "(id,partner,timer) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryChats.TABLE_NAME) + ",'" +
                terry.name + "','" +
                Const.DEFAULT_DESTRUCTION_TIMER + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        sql = "INSERT INTO " + FeedReaderContract.FeedEntryChats.TABLE_NAME + "(id,partner,timer) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryChats.TABLE_NAME) + ",'" +
                john.name + "','" +
                Const.DEFAULT_DESTRUCTION_TIMER + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        sql = "INSERT INTO " + FeedReaderContract.FeedEntryChats.TABLE_NAME + "(id,partner,timer) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryChats.TABLE_NAME) + ",'" +
                lara.name + "','" +
                Const.DEFAULT_DESTRUCTION_TIMER + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        sql = "INSERT INTO " + FeedReaderContract.FeedEntryChats.TABLE_NAME + "(id,partner,timer) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryChats.TABLE_NAME) + ",'" +
                stacey.name + "','" +
                Const.DEFAULT_DESTRUCTION_TIMER + "')";
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    public void setMessages() {
        boolean result = true;
        List<MessageBE> msgs = new ArrayList<>();
        msgs.add(new IncMessageBE("Hey there!", new Date(117,9,20,23,24,05), getContactByName(getString(R.string.buddy_1))));
        msgs.add(new OutMessageBE("Whats up?", new Date(117,9,20,23,24,15), getContactByName(getString(R.string.buddy_1))));
        msgs.add(new IncMessageBE("Nothing man, good to hear from you!", new Date(117,9,20,23,24,05), getContactByName(getString(R.string.buddy_1))));
        msgs.add(new OutMessageBE("ne", new Date(2017,10,21,01,41,45), getContactByName(getString(R.string.buddy_2))));
        msgs.add(new OutMessageBE("noch net", new Date(2017,10,01,23,41,50), getContactByName(getString(R.string.buddy_2))));
        msgs.add(new OutMessageBE("aber halt so ^^", new Date(2017,10,01,41,24,55), getContactByName(getString(R.string.buddy_2))));
        msgs.add(new IncMessageBE("jo", new Date(2017,10,21,01,42,02), getContactByName(getString(R.string.buddy_2))));
        msgs.add(new OutMessageBE("jo ich hab ein problem, zu dem mir gerade keine lösung einfällt", new Date(2017,10,21,01,36,05), getContactByName(getString(R.string.buddy_3))));
        msgs.add(new IncMessageBE("suh", new Date(2017,10,21,01,36,06), getContactByName(getString(R.string.buddy_3))));
        msgs.add(new OutMessageBE("die public keys sind strings", new Date(2017,10,21,01,36,07), getContactByName(getString(R.string.buddy_3))));
        msgs.add(new IncMessageBE("ja bestimmt", new Date(2017,10,20,11,48,07), getContactByName(getString(R.string.buddy_4))));
        msgs.add(new IncMessageBE("aslloooo", new Date(2017,10,20,14,01,07), getContactByName(getString(R.string.buddy_4))));
        msgs.add(new IncMessageBE("ich hab jetzt ma zeit nach nem server zu schauen", new Date(2017,10,20,14,02,07), getContactByName(getString(R.string.buddy_4))));
        msgs.add(new IncMessageBE("1€ pro monat aber 24 monate laufzeit", new Date(2017,10,20,14,02,27), getContactByName(getString(R.string.buddy_4))));
        db.beginTransaction();
        for (MessageBE msg : msgs) {
            String sql = "INSERT INTO messages (id, in_out, partner, content, time) VALUES (" +
                    getSequenceValue(FeedReaderContract.FeedEntryMessages.TABLE_NAME) + ",'" +
                    (msg instanceof IncMessageBE ? "i" : "o") + "'," +
                    getChatByName(msg.getPartner().getName()).id + ",'" +
                    msg.content + "','" +
                    sdf.format(msg.timeSent) + "')";
            try {
                db.execSQL(sql);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                result = false;
            }
        }
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }
    // endregion

    public boolean copyDbToExternal() {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = Util.FeedReaderBackgroundDBHelper.DATABASE_PATH + getModel().DATABASE_NAME;
                String backupDBPath = getModel().DATABASE_NAME;
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // endregion

    // region load things
    public void loadContactList() {
        loadContactsFromDB();
    }

    public void loadChatList() {
        loadChatsFromDB();
    }

    public List<String> loadLocalNames(Context context) {
        String[] internal = readInternal(context, Const.FILENAME_USERNAMES).split("\n");
        return new ArrayList<>(Arrays.asList(internal));
    }
    // endregion

    // region Server-Requests
    public void setUserServer(final PublicKey pub, final String signedName) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String answer = setUserServerWork(pub, signedName);
                handleSetUserResponse(answer);
            }
        });
        thread.run();
    }
        // needs to be run in separate thread!
    public ContactBE getUserServer(final String name) {
        String answer = getUserServerWork(name);
        if (answer != null) {
            switch (answer) {
                case Const.ANSWER_CODE_USER:
                    try {
                        JSONObject jsonResponse = new JSONObject(answer);
                    } catch (JSONException jsone) {
                        jsone.printStackTrace();
                    }
                    break;
                case Const.ANSWER_CODE_USERNAME_NA:
                    return null;
                default:
                    return null;
            }
        }
        return null;
    }

    public List<MessageBE> getMessagesServer(String name) {
        // First request code with name
        // then sign code to get messages
        return null;
    }

    public boolean sendMessageServer(String signedMessage, ContactBE recipient) {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put(Const.KEY_SEND_SIGNED_MESSAGE, signedMessage);
            postDataParams.put(Const.KEY_SEND_RECIPIENT, recipient.getName());
            postDataParams.put(Const.KEY_SEND_SENDER, getModel().username);
            URL url = new URL(Const.SERVER_URI);
            httpClient = (HttpURLConnection) url.openConnection();
            httpClient.setRequestMethod(Const.PROTOCOL_POST);
        } catch (JSONException jsone) {
            jsone.printStackTrace();
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        httpClient.setDoInput(true);
        httpClient.setDoOutput(true);
        httpClient.setReadTimeout(15000);
        httpClient.setConnectTimeout(15000);
        OutputStream outputPost;
        BufferedWriter writer;
        int responseCode;
        try {
            outputPost = new BufferedOutputStream(httpClient.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(outputPost, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            outputPost.close();
            responseCode = httpClient.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                // here sb.toString() contains the answer
                if (sb.toString().equals(Const.ANSWER_CODE_SUCCESS)) {
                    return true;
                }
            } else {
                Log.e("response code", new String("" + responseCode));
                return false;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    // endregion

    // region Server-Request Working Threads

    private String setUserServerWork(PublicKey pkey, String signedName) {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put(Const.KEY_PUBLIC_VALUE, Util.bigIntToString(pkey.getValue()));
            postDataParams.put(Const.KEY_PUBLIC_MODUL, Util.bigIntToString(pkey.getModul()));
            postDataParams.put(Const.KEY_SIGNED_USERNAME, signedName);
            URL url = new URL(Const.SERVER_URI);
            httpClient = (HttpURLConnection) url.openConnection();
            httpClient.setRequestMethod(Const.PROTOCOL_POST);
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (JSONException jsone) {
            jsone.printStackTrace();
            return null;
        }
        httpClient.setDoInput(true);
        httpClient.setDoOutput(true);
        httpClient.setReadTimeout(15000);
        httpClient.setConnectTimeout(15000);
        OutputStream outputPost;
        BufferedWriter writer;
        int responseCode;
        try {
            outputPost = new BufferedOutputStream(httpClient.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(outputPost, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            outputPost.close();
            responseCode = httpClient.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                // here sb.toString() contains the answer
                return sb.toString();
            } else {
                Log.e("response code", new String("" + responseCode));
                return null;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getUserServerWork(String username) {
        JSONObject postDataParams = new JSONObject();
        try {
            postDataParams.put(Const.KEY_REQUEST_USERNAME, username);
            URL url = new URL(Const.SERVER_URI);
            httpClient = (HttpURLConnection) url.openConnection();
            httpClient.setRequestMethod(Const.PROTOCOL_POST);
        } catch (MalformedURLException murle) {
            murle.printStackTrace();
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (JSONException jsone) {
            jsone.printStackTrace();
            return null;
        }
        httpClient.setDoInput(true);
        httpClient.setDoOutput(true);
        httpClient.setReadTimeout(15000);
        httpClient.setConnectTimeout(15000);
        OutputStream outputPost;
        BufferedWriter writer;
        int responseCode;
        try {
            outputPost = new BufferedOutputStream(httpClient.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(outputPost, "UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            outputPost.close();
            responseCode = httpClient.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                // here sb.toString() contains the answer
                return sb.toString();
            } else {
                Log.e("response code", new String("" + responseCode));
                return null;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMessagesServerWork() {
        return "";
    }

    // endregion

    // region send/set Stuff
    public void sendMessage(MessageBE msg) {

    }

    public void saveMessageOfChat(MessageBE msg, ChatBE chat) {
        String sql = "INSERT INTO messages (id,in_out,partner,content,time) VALUES (" +
                getSequenceValue(FeedReaderContract.FeedEntryMessages.TABLE_NAME) + ",'" +
                (msg instanceof IncMessageBE ? "i" : "o") + "'," +
                chat.id + ",'" +
                msg.content + "','" +
                sdf.format(msg.timeSent) + "')";
        boolean result = true;
        db.beginTransaction();
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            result = false;
        }
        if (result)
        {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }
    // endregion

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    private void handleSetUserResponse(String response) {
        switch (response) {
            case Const.ANSWER_CODE_SUCCESS:
                break;
            case Const.ANSWER_CODE_USERNAME_NA:
                break;
            default:
                break;
        }
    }

    // region get Stuff
    public ContactBE getContactByName(String name) {
        for (ContactBE c : model.contacts) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    public ContactBE getContactById(int id) {
        for (ContactBE c : model.contacts) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public ChatBE getChatByName(String name) {
        for (ChatBE c : model.chats) {
            if (c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }

    public ChatBE getChatById(int id) {
        for (ChatBE c : model.chats) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }
    // endregion

    // region set Stuff
    public void setCurChat(ChatBE chat){
        model.curChat = chat;
    }

    public void setCurContact(ContactBE contact) {
        model.curContact = contact;
    }

    public boolean addNewContact(ContactBE contact) {
        if (getContactByName(contact.getName()) == null) {
            if (saveContactToDB(contact)) {
                model.contacts.add(contact);
                return true;
            }
        }
        return false;
    }
    // endregion

}
