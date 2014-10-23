package robustgametools.model;

/**
 * Profile to store the information about user's account.
 *
 */
public class Profile {

    private String mOnlineId, mAvatarUrl;
    private int mLevel, mProgress, mPlatinum, mGold, mSilver, mBronze;
    private boolean mPlus;

    public String getOnlineId() {
        return mOnlineId;
    }

    public void setOnlineId(String onlineId) {
        this.mOnlineId = onlineId;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public int getPlatinum() {
        return mPlatinum;
    }

    public void setPlatinum(int platinum) {
        this.mPlatinum = platinum;
    }

    public int getGold() {
        return mGold;
    }

    public void setGold(int gold) {
        this.mGold = gold;
    }

    public int getSilver() {
        return mSilver;
    }

    public void setSilver(int silver) {
        this.mSilver = silver;
    }

    public int getBronze() {
        return mBronze;
    }

    public void setBronze(int bronze) {
        this.mBronze = bronze;
    }

    public boolean isPlus() {
        return mPlus;
    }

    public void setPlus(boolean plus) {
        this.mPlus = plus;
    }
}
