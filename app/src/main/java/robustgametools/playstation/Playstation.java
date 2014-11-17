package robustgametools.playstation;

import android.content.Context;

import java.util.ArrayList;

import robustgametools.model.Game;
import robustgametools.model.Profile;
import robustgametools.util.JsonFactory;
import robustgametools.util.Log;
import robustgametools.util.Storage;

/**
 * A Playstation class to enforce singleton design
 * on some of the classes like Profile and to prevent
 * them from getting destroyed throughout activity's
 * lifecycle
 */
public class Playstation {

    private static Playstation playstation =  null;
    private static Profile profile;
    private static JsonFactory jsonFactory;
    private static Storage storage;
    private static boolean updated;

    private Playstation() {}

    public static Playstation getInstance(Context context) {
        if (playstation == null) {
            playstation = new Playstation();
            jsonFactory = JsonFactory.getInstance();
            storage = Storage.getInstance(context);
            updated = false;
        }
        return playstation;
    }

    public Profile getProfile() {
        if (profile == null) {
            String data = storage.readUserData();
            profile = jsonFactory.parseUserProfile(data);
            String gameData = storage.readGameData();
            ArrayList<Game> recentGames = jsonFactory.parseGames(gameData);
            profile.setGames(recentGames);
            int gameCount = jsonFactory.parseGameCount(gameData);
            profile.setGameCount(gameCount);
        }
        return profile;
    }

    public void persistProfile(String data) {
        storage.persistUserData(data);
    }

    public void updated() {
        updated = true;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void destroy() {
        profile = null;
    }
}
