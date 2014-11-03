package robustgametools.util;

/**
 * Log class to help in logging
 * info while debugging and so on.
 */
public class Log {

    public static final boolean enableDebug     = true;

    public static void i(String tag, String msg) {
        if(enableDebug) {
            android.util.Log.i(tag,msg);
        }
    }
}
