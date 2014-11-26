package robustgametools.guide;

import android.content.Context;
import android.graphics.Typeface;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * A GuideFormatter class to
 * return the right view to be
 * displayed on the screen
 */
public class GuideFactory {

    private static GuideFactory mFormatter = null;
    private static Context mContext;
    private static ArrayList<View> views;
    private boolean mIsOffline;

    private static final String mImageRegex = "[D]";
    private static final String mTextBoldRegex = "[T]";
    private static final String mSymbolRegex = "[+]";
    private static final String mPlatinumRegex = "[P]";
    private static final String mGoldRegex = "[G]";
    private static final String mSilverRegex = "[S]";
    private static final String mBronzeRegex = "[B]";


    private GuideFactory() {};

    public static GuideFactory getInstance(Context context) {
        if (mFormatter == null) {
            mFormatter = new GuideFactory();
            mContext = context;
        }
        return mFormatter;
    }

    public GuideFactory format(String rawGuide) {
        // checks for empty string with just spaces
        if (rawGuide.trim().length() != 0) {
            views = new ArrayList<View>();
            mIsOffline = false;
            formatImage(rawGuide);
        }

        return this;
    }

    public GuideFactory isOffline() {
        mIsOffline = true;
        return this;
    }

    public void into(LinearLayout containerLayout) {
        for (int i = 0; i < views.size(); i++) {
            containerLayout.addView(views.get(i));
        }
    }

    private void formatImage(String rawGuide) {
        int index = rawGuide.indexOf(mImageRegex);
        if (index  == -1) {
            // no image in guide, proceed to extract text
            formatText(rawGuide);
        } else {
            int start = index;
            formatText(rawGuide.substring(0, start));
            String guide = rawGuide.replace(mImageRegex, "");
            int end = guide.indexOf(mImageRegex);
            guide = guide.replace(mImageRegex, "");
            String url = guide.substring(start, end);
            formatImage(guide.substring(end, guide.length()));
        }
    }

    private void formatText(String rawString) {
        SpannableStringBuilder builder = new SpannableStringBuilder(rawString);
        rawString = formatRegex(builder, rawString, mTextBoldRegex, null);
        //rawString = formatRegex(builder, rawString, mBronzeRegex, null);
        TextView text = new TextView(mContext);
        text.setText(builder);
        views.add(text);
    }

    private String formatRegex(SpannableStringBuilder spannable,
                               String string, String regex,
                               ArrayList<ParcelableSpan> spans) {
        int start = string.indexOf(regex);
        while(start != -1) {
            string = string.replaceFirst(Pattern.quote(regex), "");
            int end = string.indexOf(regex);
            string = string.replaceFirst(Pattern.quote(regex), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + regex.length() * 2, bold);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
            start = string.indexOf(regex);
        }
        return string;
    }

    private void extractButtons(String rawGuide) {
    }




}
