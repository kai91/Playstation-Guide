package robustgametools.model;

/**
 * A Trophy class to represents the model of a trophy/achievement.
 */
public class Trophy {

    private String mTitle;
    private String mIconUrl;
    private String mDescription;
    private boolean mIsEarned;
    private TrophyLevel mTrophyLevel;
    private  boolean mIsHidden;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public boolean isEarned() {
        return mIsEarned;
    }

    public void setIsEarned(boolean isEarned) {
        this.mIsEarned = isEarned;
    }

    public TrophyLevel getTrophyLevel() {
        return mTrophyLevel;
    }

    public void setTrophyLevel(TrophyLevel trophyLevel) {
        this.mTrophyLevel = trophyLevel;
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    public void setHidden(boolean hide) {
        mIsHidden = hide;
    }


}
