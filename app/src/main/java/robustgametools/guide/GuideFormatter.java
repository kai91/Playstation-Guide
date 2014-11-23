package robustgametools.guide;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import robustgametools.model.TrophyGuide;

/**
 * A GuideFormatter class to
 * return the right view to be
 * displayed on the screen
 */
public class GuideFormatter {

    private static GuideFormatter mFormatter = null;
    private static Context mContext;
    private static ArrayList<View> views;

    private GuideFormatter() {};

    public static GuideFormatter getInstance(Context context) {
        if (mFormatter == null) {
            mFormatter = new GuideFormatter();
            mContext = context;
        }
        return mFormatter;
    }

    public GuideFormatter format(String rawGuide) {
        views = new ArrayList<View>();
        TextView text = new TextView(mContext);
        text.setText(rawGuide);
        views.add(text);
        return this;
    }

    public void into(LinearLayout containerLayout) {
        for (int i = 0; i < views.size(); i++) {
            containerLayout.addView(views.get(i));
        }
    }

    //TODO
    private ImageView getImage(TrophyGuide guide) {
        return null;
    }

    private TextView getText(TrophyGuide guide) {
        return null;
    }

    private SpannableString getButtonSymbols(TrophyGuide guide) {
        return null;
    }


}
