package robustgametools.util;

/**
 * Log class to help in logging
 * info while debugging and so on.
 */
public class Log {

    private static String TAG = "PLAYSTATION";

    public static final boolean enableDebug     = true;

    public static void i(String tag, String msg) {
        if(enableDebug) {
            android.util.Log.i(tag,msg);
        }
    }

    public static void i(String msg) {
        if (enableDebug) {
            android.util.Log.i(TAG, msg);
        }
    }
}
