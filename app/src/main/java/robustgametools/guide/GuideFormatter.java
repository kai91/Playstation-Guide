package robustgametools.guide;

import android.content.Context;
import android.view.View;
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
    private static boolean mIsOffline;

    private static final String mImageSeparatorStart = "{{{{";
    private static final String mImageSeparatorStEnd = "}}}}";
    private static final String mTextBoldStart = "{{{";
    private static final String mTextBoldEnd = "}}}";
    private static final String mLinkStart = "{{";
    private static final String mLinkEnd = "}}";
    private static final String mSymbolStart = "{";
    private static final String mSymbolEnd = "}";

    private GuideFormatter() {};

    public static GuideFormatter getInstance(Context context) {
        if (mFormatter == null) {
            mFormatter = new GuideFormatter();
            mContext = context;
        }
        return mFormatter;
    }

    public GuideFormatter format(String rawGuide) {
        // checks for empty string with just spaces
        if (rawGuide.trim().length() != 0) {
            views = new ArrayList<View>();
            TextView text = new TextView(mContext);
            mIsOffline = false;
            text.setText(rawGuide);
            views.add(text);
        }

        return this;
    }

    public GuideFormatter isOffline() {
        mIsOffline = true;
        return this;
    }

    public void into(LinearLayout containerLayout) {
        for (int i = 0; i < views.size(); i++) {
            containerLayout.addView(views.get(i));
        }
    }

    private void extractImage(String rawGuide) {
        int index = rawGuide.indexOf(mImageSeparatorStart);
        if (index  == -1) {
            // no image in guide, return
            return;
        } else {
            int start = index;
            format(rawGuide.substring(0, start));
            String guide = rawGuide.replace(mImageSeparatorStart, "");
            int end = guide.indexOf(mImageSeparatorStEnd);
            guide = guide.replace(mImageSeparatorStEnd, "");
            String url = guide.substring(start, end);
            format(guide.substring(end, guide.length()));
        }
    }

    private void extractText(String rawGuide) {
    }

    private void extractButtons(String rawGuide) {
    }


}
