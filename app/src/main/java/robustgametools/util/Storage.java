package robustgametools.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Storage is a util class used to write and read
 * file from and into the Android. This class will
 * use Android internal mStorage.
 */
public class Storage {

    private static Storage mStorage = new Storage();
    private static Context mContext = null;
    private static String rootDir = null;

    private static String profileDir = "profile/";
    private static  String jsonDir = "json/";

    protected Storage() {
        // Exists only to defeat instantiation.
    }

    public static Storage getInstance(Context context) {
        if (mStorage != null) {
            mContext = context;

            return mStorage;
        } else throw new NullPointerException();
    }

    public void createDir(String file) {
        File root = mContext.getFilesDir();
        File newDir = new File(root, file);
        if (!newDir.exists()) {
            newDir.mkdirs();
        }
    }

    private boolean createFile(String path, String fileName, String data) {
        try {
            byte[] content = data.getBytes();
            createDir(path);
            File root = mContext.getFilesDir();
            File newDir = new File(root, path);
            File output = new File(newDir, fileName);
            output.createNewFile();
            FileOutputStream stream = new FileOutputStream(output);
            stream.write(content);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create", e);
        }
        return true;
    }

    private String readFile(String path, String fileName) {
        final FileInputStream stream;
        try {
            File root = mContext.getFilesDir();
            //File newDir = new File(root, path);
            File newDir = new File(root.getAbsolutePath() + '/' + path);
            File output = new File(newDir, fileName);
            stream = new FileInputStream(output);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file to input sream", e);
        }
    }

    public boolean persistUserData(String username, String data) {
        return createFile(profileDir, username, data);
    }

    public String readUserData(String username) {
        return readFile(profileDir, username);
    }

}
