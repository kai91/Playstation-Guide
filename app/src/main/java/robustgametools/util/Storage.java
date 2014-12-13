package robustgametools.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Storage is a util class used to write and read
 * file from and into the Android. This class will
 * use Android Internal Storage.
 */
public class Storage {

    private static Storage mStorage = new Storage();
    private static Context mContext = null;

    private static final String mActiveUser = "activeUser.json";
    private static final String mActiveGame = "activeGame.json";
    private static final String mJsonDir = "json";
    private static final String mGuideDir = "guide";

    protected Storage() {
        // Exists only to defeat instantiation.
    }

    public static Storage getInstance(Context context) {
        if (mStorage != null) {
            mContext = context.getApplicationContext();
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
        return createFile(path, fileName, data.getBytes());
    }

    private boolean createFile(String path, String fileName, byte[] data) {
        try {
            createDir(path);
            File root = mContext.getFilesDir();
            File newDir = new File(root, path);
            File output = new File(newDir, fileName);
            output.createNewFile();
            FileOutputStream stream = new FileOutputStream(output);
            stream.write(data);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create", e);
        }
        return true;
    }

    private boolean deleteDir(String path, String fileName) {
        File root = mContext.getFilesDir();
        File dir = new File(root, path);
        File file = new File(dir, fileName);
        if (file.exists() && !file.isDirectory()) {
            return file.delete();
        } else if (file.isDirectory()) {
            String[] list = file.list();
            for (int i = list.length-1; i >= 0; i--) {
                deleteDir(path + File.separator + fileName, list[i]);
            }
            return file.delete();
        }
        return false;
    }

    private String readFile(String path, String fileName) {
        final FileInputStream stream;
        try {
            File root = mContext.getFilesDir();
            File newDir = new File(root.getAbsolutePath() + '/' + path);
            File output = new File(newDir, fileName);
            stream = new FileInputStream(output);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file to input stream", e);
        }
    }

    public boolean persistUserData(String data) {
        return createFile("", mActiveUser, data);
    }

    public boolean persistRecentGames(String data) {
        return createFile("", mActiveGame, data);
    }

    public String readUserData() {
        return readFile("", mActiveUser);
    }

    public String readGameData() {
        return readFile("", mActiveGame);
    }

    public boolean persistGuideData(String title, String data) {
        return createFile(mGuideDir + File.separator + title, title, data);
    }

    public boolean persistGuideImage(String url, String title, byte[] data) {
        String[] split = url.split("/"); // get file name
        String fileName = split[split.length-1];
        return createFile(mGuideDir + File.separator + title, fileName, data);
    }

    public String convertUrlToOfflineUri(String title, String url) {
        String[] split = url.split("/");
        String fileName = split[split.length-1];
        String path = mContext.getFilesDir().getAbsolutePath() + File.separator
                + mGuideDir + File.separator + title + File.separator + fileName;
        return  path;
    }

    public String readGuide(String name) {
        return readFile(mGuideDir + File.separator + name, name);
    }

    public boolean deleteGuide(String name) {
        return deleteDir(mGuideDir, name);
    }

    public ArrayList<String> getGuideList() {
        File root = mContext.getFilesDir();
        File dir = new File(root, mGuideDir);
        String[] guides = dir.list();
        ArrayList<String> list = new ArrayList<>();
        if (guides != null) {
            list = new ArrayList<>(Arrays.asList(guides));
        }
        return list;
    }

    public void deleteUserData() {
        deleteDir("", mActiveUser);
        deleteDir("", mActiveGame);
    }

    public boolean userDataExists() {
        File root = mContext.getFilesDir();
        File userData = new File(root, mActiveUser);
        File userGame = new File(root, mActiveGame);
        return userData.exists() && userGame.exists();
    }
}
