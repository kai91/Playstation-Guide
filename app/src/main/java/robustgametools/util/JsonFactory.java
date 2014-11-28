package robustgametools.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import robustgametools.model.Game;
import robustgametools.model.Platform;
import robustgametools.model.Profile;
import robustgametools.model.Trophy;
import robustgametools.model.TrophyLevel;

/**
 * JsonFactory utility class to convert Json into
 * class object and vice versa. Also used for
 * parsing purposes
 *
 * @author kai
 */
public class JsonFactory {

    private static JsonFactory mJsonFactory = null;

    // To prevent instantiation
    private JsonFactory() {};

    // Singleton design pattern
    public static JsonFactory getInstance() {
        if (mJsonFactory == null) {
            mJsonFactory = new JsonFactory();
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
        int size = array.size();
        for (int i = 0; i < size ; i++) {
            JsonObject jsGame = array.get(i).getAsJsonObject();
            Game game = new Game();
            game.setNpCommunicationId(jsGame.get("npCommunicationId").getAsString());
            game.setTitle(jsGame.get("trophyTitleName").getAsString());
            game.setTrophyIconUrl(jsGame.get("trophyTitleIconUrl").getAsString());
            game.setHasDlc(jsGame.get("hasTrophyGroups").getAsBoolean());

            String platformName = jsGame.get("trophyTitlePlatfrom").getAsString();
            ArrayList<Platform> platforms = new ArrayList<Platform>();
            String[] platformString = platformName.split(",");
            for (int j = 0; j < platformString.length; j++) {
                Platform platform = Platform.valueOf(platformString[j].toUpperCase());
                platforms.add(platform);
            }
            game.setPlatform(platforms);


            JsonObject comparedUser = jsGame.get("comparedUser").getAsJsonObject();
            game.setProgress(comparedUser.get("progress").getAsInt());
            JsonObject trophiesInfo = comparedUser.get("earnedTrophies").getAsJsonObject();
            game.setBronze(trophiesInfo.get("bronze").getAsInt());
            game.setSilver(trophiesInfo.get("silver").getAsInt());
            game.setGold(trophiesInfo.get("gold").getAsInt());
            game.setPlatinum(trophiesInfo.get("platinum").getAsInt());
            gameArray.add(game);
        }
        return gameArray;
    }

    /**
     * Parse the game json and return the total game
     * count that the player has
     * @param gameJson
     * @return
     */
    public int parseGameCount(String gameJson) {
        JsonObject jsonObject = new JsonParser().parse(gameJson).getAsJsonObject();
        int count = jsonObject.get("totalResults").getAsInt();
        return count;
    }

    public String appendJson(String data, String newData) {
        JsonObject object = new JsonParser().parse(data).getAsJsonObject();
        JsonObject newObject = new JsonParser().parse(newData).getAsJsonObject();
        JsonArray array = object.get("trophyTitles").getAsJsonArray();
        JsonArray newArray = newObject.get("trophyTitles").getAsJsonArray();
        array.addAll(newArray);
        object.add("trophyTitles", array);
        return new Gson().toJson(object);
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

    public ArrayList<Trophy> parseTrophyList(String json) {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        JsonArray trophyList = object.get("trophies").getAsJsonArray();
        ArrayList<Trophy> trophies = new ArrayList<Trophy>();

        int length = trophyList.size();
        for (int i = 0; i < length; i++) {
            JsonObject trophyJson = trophyList.get(i).getAsJsonObject();
            Trophy trophy = new Trophy();
            trophies.add(trophy);

            boolean isHidden = trophyJson.get("trophyHidden").getAsBoolean();
            trophy.setHidden(isHidden);

            boolean isEarned;
            if (trophyJson.get("comparedUser") == null) {
                isEarned = false;
            } else {
                JsonObject userJson = trophyJson.get("comparedUser").getAsJsonObject();
                isEarned = userJson.get("earned").getAsBoolean();
            }

            trophy.setIsEarned(isEarned);

            if (isHidden) continue;

            String trophyType = trophyJson.get("trophyType").getAsString();
            TrophyLevel level = TrophyLevel.valueOf(trophyType.toUpperCase());
            trophy.setTrophyLevel(level);
            trophy.setTitle(trophyJson.get("trophyName").getAsString());
            trophy.setDescription(trophyJson.get("trophyDetail").getAsString());
            trophy.setIconUrl(trophyJson.get("trophyIconUrl").getAsString());
        }
        return  trophies;
    }
}
