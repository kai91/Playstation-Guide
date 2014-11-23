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
            longInfo(tag,msg);
        }
    }

    public static void i(String msg) {
        if (Playstation.sDebug) {
            longInfo(TAG, msg);
        }
    }

    public static void longInfo(String tag, String str) {
        if(str.length() > 4000) {
            android.util.Log.i(tag, str.substring(0, 4000));
            longInfo(tag, str.substring(4000));
        } else
            android.util.Log.i(tag, str);
    }
}
