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
                // sequence value INT for ID
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                // name as String
                COLUMN_NAME + " VARCHAR," +
                // key value as String
                COLUMN_KEY + " VARCHAR," +
                // key modul as String
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
                // sequence value INT for ID
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                // name of partner as String
                COLUMN_PARTNER + " VARCHAR," +
                // duration of self destruction timer in seconds
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
                // sequence value INT for ID
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                // "i" for incoming msg, "o" for outgoing msg
                COLUMN_IO + " VARCHAR," +
                // ID of chat
                COLUMN_PARTNER + " INTEGER," +
                // Content as String
                COLUMN_CONTENT + " VARCHAR," +
                // Timestamp as String
                COLUMN_TIMESTAMP + " VARCHAR)";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }

    public static class FeedEntrySequence implements BaseColumns {
        public static final String TABLE_NAME = "sequences";
        public static final String COLUMN_TABLE = "tablen";
        public static final String COLUMN_VALUE = "seq_val";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + "(" +
                // table name as primary key
                COLUMN_TABLE + " VARCHAR PRIMARY KEY," +
                // current sequence value as INT
                COLUMN_VALUE + " INTEGER)";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " +
                TABLE_NAME;
    }
}
