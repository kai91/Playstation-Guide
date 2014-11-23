package robustgametools.model;

import com.google.gson.Gson;

import java.util.ArrayList;

import robustgametools.util.Log;

/**
 * Trophy guide class to store information about a trophy guide.
 *
 * @author kai
 */
public class TrophyGuide {

    private String title, roadmap;
    private int guideId, revision;
    private ArrayList<Types> platforms;
    private ArrayList<Guide> guides;

    // Not for used in runtime, this is
    // a function to automate the making of a
    // partially complete guide.
    public static TrophyGuide parseFromTrophyList(ArrayList<Trophy> trophies) {
        TrophyGuide trophyGuide = new TrophyGuide();
        ArrayList<Guide> newEntries = new ArrayList<Guide>();
        trophyGuide.setRevision(1);
        trophyGuide.setGuideId(1);
        trophyGuide.setPlatforms(new ArrayList<Types>());

        for (int i = 0; i < trophies.size(); i++) {
            Trophy trophy = trophies.get(i);
            Guide guide = new Guide();
            guide.title = trophy.getTitle();
            guide.description = trophy.getDescription();
            guide.type = trophy.getTrophyLevel().toString();
            guide.url = trophy.getIconUrl();
            newEntries.add(guide);
        }

        trophyGuide.setGuides(newEntries);
        Log.i(new Gson().toJson(trophyGuide));

        return trophyGuide;
    }

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public ArrayList<Types> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<Types> platforms) {
        this.platforms = platforms;
    }

    public ArrayList<Guide> getGuides() {
        return guides;
    }

    public void setGuides(ArrayList<Guide> guides) {
        this.guides = guides;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoadmap() {
        return roadmap;
    }

    public void setRoadmap(String roadmap) {
        this.roadmap = roadmap;
    }
}