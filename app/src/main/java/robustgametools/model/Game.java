package robustgametools.model;

import java.util.ArrayList;

/**
 * A Game class to represents the model of a game.
 */
public class Game {

    private String mTitle;
    private ArrayList<Platform> mPlatform;
    private ArrayList<Trophy> mTrophies;
    private boolean mHasDlc;
    private String mNpCommunicationId;
    private String mTrophyIconUrl;
    private int mBronze, mSilver, mGold, mPlatinum;
    private int mProgress;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ArrayList<Platform> getPlatform() {
        return mPlatform;
    }

    public void setPlatform(ArrayList<Platform> mPlatform) {
        this.mPlatform = mPlatform;
    }

    public ArrayList<Trophy> getTrophies() {
        return mTrophies;
    }

    public void setTrophies(ArrayList<Trophy> mTrophies) {
        this.mTrophies = mTrophies;
    }

    public boolean hasDlc() {
        return mHasDlc;
    }

    public void setHasDlc(boolean mHasDlc) {
        this.mHasDlc = mHasDlc;
    }

    public String getNpCommunicationId() {
        return mNpCommunicationId;
    }

    public void setNpCommunicationId(String mNpCommunicationId) {
        this.mNpCommunicationId = mNpCommunicationId;
    }

    public String getTrophyIconUrl() {
        return mTrophyIconUrl;
    }

    public void setTrophyIconUrl(String mTrophyTitleIconUrl) {
        this.mTrophyIconUrl = mTrophyTitleIconUrl;
    }

    public int getBronze() {
        return mBronze;
    }

    public void setBronze(int mBronze) {
        this.mBronze = mBronze;
    }

    public int getSilver() {
        return mSilver;
    }

    public void setSilver(int mSilver) {
        this.mSilver = mSilver;
    }

    public int getGold() {
        return mGold;
    }

    public void setGold(int mGold) {
        this.mGold = mGold;
    }

    public int getPlatinum() {
        return mPlatinum;
    }

    public void setPlatinum(int mPlatinum) {
        this.mPlatinum = mPlatinum;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress;
    }
}
