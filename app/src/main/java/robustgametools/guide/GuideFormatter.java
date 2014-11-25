package robustgametools.guide;

import android.content.Context;
import android.graphics.Typeface;
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
public class GuideFormatter {

    private static GuideFormatter mFormatter = null;
    private static Context mContext;
    private static ArrayList<View> views;
    private boolean mIsOffline;

    private static final String mImageSeparatorStart = "{{{{";
    private static final String mImageSeparatorStEnd = "}}}}";
    private static final String mTextBoldStart = "{{{";
    private static final String mTextBoldEnd = "}}}";
    private static final String mLinkStart = "{{";
    private static final String mLinkEnd = "}}";
    private static final String mSymbolStart = "\\{";
    private static final String mSymbolEnd = "\\}";

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
            mIsOffline = false;
            formatImage(rawGuide);
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

    private void formatImage(String rawGuide) {
        int index = rawGuide.indexOf(mImageSeparatorStart);
        if (index  == -1) {
            // no image in guide, proceed to extract text
            formatText(rawGuide);
        } else {
            int start = index;
            formatText(rawGuide.substring(0, start));
            String guide = rawGuide.replace(mImageSeparatorStart, "");
            int end = guide.indexOf(mImageSeparatorStEnd);
            guide = guide.replace(mImageSeparatorStEnd, "");
            String url = guide.substring(start, end);
            formatImage(guide.substring(end, guide.length()));
        }
    }

    private void formatText(String rawString) {
        SpannableStringBuilder builder = new SpannableStringBuilder(rawString);
        formatBold(builder, rawString);
        TextView text = new TextView(mContext);
        text.setText(builder);
        views.add(text);
    }

    private void formatBold(SpannableStringBuilder spannable, String string) {
        int start = string.indexOf(mTextBoldStart);
        while(start != -1) {
            string = string.replaceFirst(Pattern.quote(mTextBoldStart), "");
            int end = string.indexOf(mTextBoldEnd);
            string = string.replaceFirst(Pattern.quote(mTextBoldEnd), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + mTextBoldEnd.length() * 2, bold);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
            start = string.indexOf(mTextBoldStart);
        }
    }

    private void extractButtons(String rawGuide) {
    }




}
