package backend;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public void setPassword(String newPassword, Context context) {
        model.password = sha256(newPassword);
        writeInternal(context, Const.FILENAME_PASSWORD, newPassword);
    }

    public void writeInternal(Context context, String file, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
}
