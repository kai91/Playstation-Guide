package robustgametools.util;

import robustgametools.playstation.Playstation;

/**
 * Log class to help in logging
 * info while debugging and so on.
 */
public class Log {

    private static String TAG = "PLAYSTATION";

    public static void i(String tag, String msg) {
        if(Playstation.sDebug) {
            android.util.Log.i(tag,msg);
        }
    }

    public static void i(String msg) {
        if (Playstation.sDebug) {
            android.util.Log.i(TAG, msg);
        }
    }
}
