package robustgametools.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import robustgametools.model.Guide;
import robustgametools.model.TrophyGuide;

/**
 * TrophyGuideAdapter to display
 * user's trophy info alongside
 * the guide's info
 */
public class TrophyGuideAdapter extends BaseAdapter {

    private TrophyGuide mTrophyGuide;
    private ArrayList<Guide> guides;
    private  Context mContext;

    public TrophyGuideAdapter(Context context, TrophyGuide trophyGuide) {
        mContext = context;
        mTrophyGuide = trophyGuide;
        guides = mTrophyGuide.getGuides();
    }

    @Override
    public int getCount() {
        // +1 for the roadmap section
        return guides.size() + 1;
    }

    @Override
    public String getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
