package robustgametools.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import robustgametools.model.Game;
import robustgametools.model.Profile;

/**
 * JsonFactory utility class to convert Json into
 * class object and vice versa. Also used for
 * parsing purposes
 *
 * @author kai
 */
public class JsonFactory {

    private static JsonFactory mJsonFactory = null;
    private static Gson mGson;

    // To prevent instantiation
    private JsonFactory() {};

    // Singleton design pattern
    public static JsonFactory getInstance() {
        if (mJsonFactory == null) {
            mJsonFactory = new JsonFactory();
            mGson = new Gson();
        }
        return mJsonFactory;
    }

    public ArrayList<Game> getGames(String gameJson) {
        return null;
    }

    public Profile parseUserProfile(String userProfile) {
        Profile profile = new Profile();
        JsonObject jsonObject = new JsonParser().parse(userProfile).getAsJsonObject();

        profile.setOnlineId(jsonObject.get("onlineId").getAsString());
        profile.setAvatarUrl(jsonObject.get("avatarUrl").getAsString());

        int plus = (jsonObject.get("plus").getAsInt());
        if (plus == 0) {
            profile.setPlus(false);
        } else {
            profile.setPlus(true);
        }

        JsonObject trophySummary = jsonObject.get("trophySummary").getAsJsonObject();
        profile.setLevel(trophySummary.get("level").getAsInt());
        profile.setProgress(trophySummary.get("progress").getAsInt());
        profile.setPlatinum(trophySummary.get("platinum").getAsInt());
        profile.setGold(trophySummary.get("gold").getAsInt());
        profile.setSilver(trophySummary.get("silver").getAsInt());
        profile.setBronze(trophySummary.get("bronze").getAsInt());
        return profile;
    }
}
