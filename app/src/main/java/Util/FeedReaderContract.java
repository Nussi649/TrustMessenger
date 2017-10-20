package Util;

import android.provider.BaseColumns;

/**
 * Created by ich on 18.10.2017.
 */

public final class FeedReaderContract {
    private FeedReaderContract() { }

    public static class FeedEntryContacts implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KEY = "pub_key";
        public static final String COLUMN_MODUL = "modul";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME + " VARCHAR," +
                COLUMN_KEY + " VARCHAR," +
                COLUMN_MODUL + " VARCHAR)";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }

    public static class FeedEntryChats implements BaseColumns {
        public static final String TABLE_NAME = "chats";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PARTNER = "partner";
        public static final String COLUMN_DESTRUCTION_TIMER = "timer";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_PARTNER + " VARCHAR," +
                COLUMN_DESTRUCTION_TIMER + " INTEGER)";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }

    public static class FeedEntryMessages implements BaseColumns {
        public static final String TABLE_NAME = "messages";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_IO = "in_out";
        public static final String COLUMN_PARTNER = "partner";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_TIMESTAMP = "time";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_IO + " VARCHAR," +
                COLUMN_PARTNER + " INTEGER," +
                COLUMN_CONTENT + " VARCHAR," +
                COLUMN_TIMESTAMP + " VARCHAR)";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }

    public static class FeedEntrySequence implements BaseColumns {
        public static final String TABLE_NAME = "sequences";
        public static final String COLUMN_TABLE = "tablen";
        public static final String COLUMN_VALUE = "seq_val";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_TABLE + " VARCHAR PRIMARY KEY," +
                COLUMN_VALUE + " INTEGER)";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }
}
