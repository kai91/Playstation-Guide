package robustgametools.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import robustgametools.model.Game;
import robustgametools.model.Platform;
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

    /**
     * Initialized a new array of games and parsed the info.
     * @param gameJson - Information in JSON retrieved from server
     * @return
     */
    public ArrayList<Game> parseGames(String gameJson) {
        return parseGames(gameJson, new ArrayList<Game>());
    }

    /**
     * Parsed info and add into existing game array
     * @param gameJson - Information in JSON retrieved from server
     * @param gameArray - Existing array of games to be appended to
     * @return
     */
    public ArrayList<Game> parseGames(String gameJson, ArrayList<Game> gameArray) {
        JsonObject jsonObject = new JsonParser().parse(gameJson).getAsJsonObject();
        JsonArray array = jsonObject.get("trophyTitles").getAsJsonArray();
        for (int i = array.size()-1; i >= 0 ; i--) {
            JsonObject jsGame = array.get(i).getAsJsonObject();
            Game game = new Game();
            game.setNpCommunicationId(jsGame.get("npCommunicationId").getAsString());
            game.setTitle(jsGame.get("trophyTitleName").getAsString());
            game.setTrophyIconUrl(jsGame.get("trophyTitleIconUrl").getAsString());
            game.setHasDlc(jsGame.get("hasTrophyGroups").getAsBoolean());

            String platformName = jsGame.get("trophyTitlePlatform").getAsString();
            Platform platform = Platform.valueOf(platformName.toUpperCase());
            game.setPlatform(platform);

            JsonObject trophiesInfo = jsGame.get("comparedUser").getAsJsonObject();
            game.setBronze(trophiesInfo.get("bronze").getAsInt());
            game.setSilver(trophiesInfo.get("silver").getAsInt());
            game.setGold(trophiesInfo.get("gold").getAsInt());
            game.setPlatinum(trophiesInfo.get("platinum").getAsInt());
        }
        return gameArray;
    }

    /**
     *
     * @param userProfile - User profile in JSON from server
     * @return
     */
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

        JsonObject earnedTrophies = trophySummary.
                get("earnedTrophies").getAsJsonObject();
        profile.setPlatinum(earnedTrophies.get("platinum").getAsInt());
        profile.setGold(earnedTrophies.get("gold").getAsInt());
        profile.setSilver(earnedTrophies.get("silver").getAsInt());
        profile.setBronze(earnedTrophies.get("bronze").getAsInt());
        return profile;
    }
}
