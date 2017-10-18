package backend;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.piddnbuddn.we.trustmessenger.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Util.Util;
import Util.FeedReaderContract;
import backend.be.ChatBE;
import backend.be.ContactBE;
import backend.be.CursorToBETransform;

import static Util.Util.sha256;

/**
 * Created by ich on 07.10.2017.
 */

public class Controller {

    public static Controller instance;
    Model model;
    SQLiteDatabase db;

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
        model.resources = res;
    }

    public String getString(int ResID) {
        return model.resources.getString(ResID);
    }
    // endregion

    // region Password/Username
    public void getAccountInfo(Context context) {
        String username = readInternal(context, Const.FILENAME_USERNAME);
        String password = readInternal(context, Const.FILENAME_PASSWORD);
        if (username != "") {
            model.username = username;
        }
        if (password != "") {
            model.password = password;
        }
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
    // endregion

    // region Database
    public void setDb(SQLiteDatabase database) {
        db = database;
        if (DatabaseUtils.queryNumEntries(db, "sequences") == 0) {
            initiateSequenceValues();
            //setContacts();
        }
    }

    private void initiateSequenceValues() {
        boolean result = true;
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("table","contacts");
        values.put("seq_val", 0);
        result &= (db.insert("sequences", null, values) != -1);
        values = new ContentValues();
        values.put("table","messages");
        values.put("seq_val", 0);
        result &= (db.insert("sequences", null, values) != -1);
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    private int getSequenceValue(String table) {
        String sql = "SELECT seq_val FROM sequences WHERE " + FeedReaderContract.FeedEntrySequence.COLUMN_TABLE + "='" + table + "'";
        db.beginTransaction();
        int seqVal = CursorToBETransform.transformToSequenceValue(db.rawQuery(sql, null)) + 1;
        ContentValues values = new ContentValues();
        values.put("seq_val", seqVal);
        String[] selectionArgs = {table};
        if (db.update("sequences", values, "table='?'", selectionArgs) == 1) {

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

    private void cheat() {
        db.beginTransaction();
        db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_CREATE_ENTRIES);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void setContacts() {
        //cheat();
        boolean result = true;
        ContactBE terry = new ContactBE(getString(R.string.buddy_1), PrivateKey.generateRandomKey().publicKey);
        ContactBE john = new ContactBE(getString(R.string.buddy_2), PrivateKey.generateRandomKey().publicKey);
        ContactBE lara = new ContactBE(getString(R.string.buddy_3), PrivateKey.generateRandomKey().publicKey);
        ContactBE stacey = new ContactBE(getString(R.string.buddy_4), PrivateKey.generateRandomKey().publicKey);

        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("id",getSequenceValue("contacts"));
        values.put("name", terry.getName());
        values.put("pub_key", Util.bigIntToString(terry.getPublicKey().getValue()));
        values.put("modul", Util.bigIntToString(terry.getPublicKey().getModul()));
        result &= (db.insert("contacts", null, values) != -1);
        values = new ContentValues();
        values.put("id",getSequenceValue("contacts"));
        values.put("name", john.getName());
        values.put("pub_key", Util.bigIntToString(john.getPublicKey().getValue()));
        values.put("modul", Util.bigIntToString(john.getPublicKey().getModul()));
        result &= (db.insert("contacts", null, values) != -1);
        values = new ContentValues();
        values.put("id",getSequenceValue("contacts"));
        values.put("name", lara.getName());
        values.put("pub_key", Util.bigIntToString(lara.getPublicKey().getValue()));
        values.put("modul", Util.bigIntToString(lara.getPublicKey().getModul()));
        result &= (db.insert("contacts", null, values) != -1);
        values = new ContentValues();
        values.put("id",getSequenceValue("contacts"));
        values.put("name", stacey.getName());
        values.put("pub_key", Util.bigIntToString(stacey.getPublicKey().getValue()));
        values.put("modul", Util.bigIntToString(stacey.getPublicKey().getModul()));
        result &= (db.insert("contacts", null, values) != -1);
        if (result) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();

    }
    // endregion

    // region load things
    public void loadContactList() {
        model.contacts = new ArrayList<>();
        model.contacts.add(new ContactBE(getString(R.string.buddy_1)));
        model.contacts.add(new ContactBE(getString(R.string.buddy_2)));
        model.contacts.add(new ContactBE(getString(R.string.buddy_3)));
        model.contacts.add(new ContactBE(getString(R.string.buddy_4)));
//        loadContactsFromDB();
    }

    public void loadChatList() {
        model.chats = new ArrayList<>();
        model.chats.add(new ChatBE(model.contacts.get(0)));
        model.chats.add(new ChatBE(model.contacts.get(1)));
        model.chats.add(new ChatBE(model.contacts.get(2)));
        model.chats.add(new ChatBE(model.contacts.get(3)));
    }
    // endregion

    public void setCurChat(ChatBE chat){
        model.curChat = chat;
    }

    public void setCurContact(ContactBE contact) {
        model.curContact = contact;
    }
}
