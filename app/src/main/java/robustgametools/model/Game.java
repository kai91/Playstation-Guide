package robustgametools.model;

import java.util.ArrayList;

/**
 * A Game class to represents the model of a game.
 */
public class Game {

    private String mTitle;
    private Platform mPlatform;
    private ArrayList<Trophy> mTrophies;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Platform getPlatform() {
        return mPlatform;
    }

    public void setPlatform(Platform mPlatform) {
        this.mPlatform = mPlatform;
    }

    public ArrayList<Trophy> getTrophies() {
        return mTrophies;
    }

    public void setTrophies(ArrayList<Trophy> mTrophies) {
        this.mTrophies = mTrophies;
    }
}
