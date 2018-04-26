package Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

import backend.Const;

/**
 * Created by David on 17.11.2017.
 */

public class FeedReaderSpecificDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_PATH = "/data/user/0/com.piddnbuddn.we.trustmessenger/databases/";
    public String DATABASE_NAME;
    public static final String DATABASE_EXTERNAL_PATH = Environment.getExternalStorageDirectory() + File.separator + "/DataBase/" + File.separator + "TrustMSG.db";

    public SQLiteDatabase db;

    public FeedReaderSpecificDBHelper(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
        DATABASE_NAME = name;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FeedReaderContract.FeedEntryMessages.SQL_CREATE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_CREATE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryChats.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL(FeedReaderContract.FeedEntryMessages.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_DELETE_ENTRIES);
        db.execSQL(FeedReaderContract.FeedEntryChats.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void createDataBase() {
        db = this.getWritableDatabase();
    }

    // returns true if DB with given name exists, false otherwise
    public static boolean checkDataBase(String name) {
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DATABASE_PATH + name;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return checkDB != null;
    }


    public void openDataBase(String name) throws SQLiteException {
        if (checkDataBase(name)) {
            String myPath = DATABASE_PATH + name;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
            String[] args = {FeedReaderContract.FeedEntryMessages.TABLE_NAME};
            if (!db.rawQuery(sql,args).moveToFirst()) {
                db.execSQL(FeedReaderContract.FeedEntryMessages.SQL_CREATE_ENTRIES);
            }
            args[0] = FeedReaderContract.FeedEntrySequence.TABLE_NAME;
            if (!db.rawQuery(sql,args).moveToFirst()) {
                db.execSQL(FeedReaderContract.FeedEntrySequence.SQL_CREATE_ENTRIES);
            }
            args[0] = FeedReaderContract.FeedEntryChats.TABLE_NAME;
            if (!db.rawQuery(sql,args).moveToFirst()) {
                db.execSQL(FeedReaderContract.FeedEntryChats.SQL_CREATE_ENTRIES);
            }
        } else {
            createDataBase();
        }
    }
}
