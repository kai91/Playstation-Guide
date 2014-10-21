package robustgametools.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * SimpleStorage is a util class used to write and read
 * file from and into the Android. This class will
 * use Android internal mStorage.
 */
public class SimpleStorage {

    private static SimpleStorage mStorage = null;
    private static Context mContext = null;

    /**
     * Returns SimpleStorage for mStorage handling
     * @return
     */
    public static SimpleStorage getStorage(Context context) {
        if (mStorage == null) {
            mContext = context;
            mStorage = new SimpleStorage();
        }
        return mStorage;
    }

    public boolean createDirectory(String name) {
        File dir = mContext.getDir(name, Context.MODE_PRIVATE);
        if (dir.exists()) {
            return true;
        }
        return false;
    }

    public boolean createFile(String name, String content) {
        try {
            byte[] bytes = content.getBytes();

            FileOutputStream fos = mContext.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(bytes);
            fos.close();
            return true;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create private file on internal storage", e);
        }
    }

    public String readFile(String name) {
        try {
            FileInputStream stream = mContext.openFileInput(name);
            byte[] out = new byte[stream.available()];
            stream.read(out);
            String outputString = new String(out);
            return outputString;
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to create private file on internal storage", e);
        }
    }

}
