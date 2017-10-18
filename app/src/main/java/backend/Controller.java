package backend;

import android.content.Context;
import android.content.res.Resources;

import com.piddnbuddn.we.trustmessenger.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import backend.be.ChatBE;
import backend.be.ContactBE;

import static Util.Util.sha256;

/**
 * Created by ich on 07.10.2017.
 */

public class Controller {

    public static Controller instance;
    Model model;

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

    public void setResources(Resources res) {
        model.resources = res;
    }

    public String getString(int ResID) {
        return model.resources.getString(ResID);
    }

    public boolean setPassword(String newPassword, Context context) {
        model.password = sha256(newPassword);
        return writeInternal(context, Const.FILENAME_PASSWORD, newPassword);
    }

    public void loadContactList() {
        model.contacts = new ArrayList<>();
        model.contacts.add(new ContactBE(getString(R.string.buddy_1)));
        model.contacts.add(new ContactBE(getString(R.string.buddy_2)));
        model.contacts.add(new ContactBE(getString(R.string.buddy_3)));
        model.contacts.add(new ContactBE(getString(R.string.buddy_4)));
    }

    public void loadChatList() {
        model.chats = new ArrayList<>();
        model.chats.add(new ChatBE(model.contacts.get(0)));
        model.chats.add(new ChatBE(model.contacts.get(1)));
        model.chats.add(new ChatBE(model.contacts.get(2)));
        model.chats.add(new ChatBE(model.contacts.get(3)));
    }

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

    public void setCurChat(ChatBE chat){
        model.curChat = chat;
    }

    public void setCurContact(ContactBE contact) {
        model.curContact = contact;
    }
}
