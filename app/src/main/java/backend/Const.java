package backend;

/**
 * Created by ich on 07.10.2017.
 */

public class Const {
    public static final String CREDITS = "Piddn&Buddn Co.";

    // FILENAMES
    public static final String FILENAME_USERNAMES = "user";
    public static final String FILENAME_PASSWORD = "credentials";
    public static final String FILENAME_PRIVATE_KEY = "privateKey";


    // PERMISSION CODES
    public static final int REQUEST_READ_CONTACTS = 1;
    public static final int REQUEST_INTERNET = 2;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 3;

    // KEY LENGTH
    public static final int KEY_BIT_LENGTH = 512;

    // CHATS
    public static final int DEFAULT_DESTRUCTION_TIMER = 60 * 60 * 24;

    // TESTING PURPOSES
    public static final String DEFAULT_KEY_VALUE = "mvsijdlkjnxkbjxdjknrdgkxdnbcvn";
    public static final String DEFAULT_KEY_MODUL = "asfjsdflskjnfleiufslduvixlkjnxdrgysfdsfysefy";

    // FORMATS
    public static final String DATETIME_FORMAT = "dd.MM.yyyy HH:mm:ss";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String TIME_FORMAT = "hh:mm a";

    // CONNECTIVITY
    public static final String SERVER_URI = "http://trustm.delta-networks.de";
    public static final String PROTOCOL_POST = "POST";
    public static final String KEY_PUBLIC_VALUE = "PKeyValue";
    public static final String KEY_PUBLIC_MODUL = "PKeyModul";
    public static final String KEY_SIGNED_USERNAME = "signedUserName";
    public static final String KEY_REQUEST_USERNAME = "reqUser";
    public static final String KEY_SEND_SIGNED_MESSAGE = "signedMsg";
    public static final String KEY_SEND_RECIPIENT = "target";
    public static final String KEY_SEND_SENDER = "sender";
    public static final String ANSWER_CODE_SUCCESS = "1";
    public static final String ANSWER_CODE_USERNAME_NA = "userNA";
    public static final String ANSWER_CODE_USER = "user";

    // DATABASE STUFF
    public static final String DATABASE_NAME_SUFFIX = "-db.db";
    public static final String DATABASE_BACKGROUND_NAME = "bg-db.db";
}
