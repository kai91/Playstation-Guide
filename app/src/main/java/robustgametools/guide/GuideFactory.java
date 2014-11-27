package robustgametools.guide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
    private static boolean mIsOffline;

    private static final String mImageRegex = "[D]";
    private static final String mTextBoldRegex = "[T]";
    private static final String mSymbolRegex = "[+]";
    private static final String mPlatinumRegex = "[P]";
    private static final String mGoldRegex = "[G]";
    private static final String mSilverRegex = "[S]";
    private static final String mBronzeRegex = "[B]";

    private static final int mBronze = Color.parseColor("#cd7f32");
    private static final int mSilver = Color.parseColor("#808080");
    private static final int mGold = Color.parseColor("#E2B227");
    private static final int mPlatinum = Color.parseColor("#a5b6ec");

    private GuideFactory() {
    }

    ;

    public static GuideFactory getInstance(Context context) {
        if (mFormatter == null) {
            mFormatter = new GuideFactory();
            mContext = context;
        }
        return mFormatter;
    }

    public GuideFactory format(String rawGuide) {
        // checks for empty string with just spaces
        rawGuide = rawGuide.trim();
        if (rawGuide.length() != 0) {
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            views.get(i).setLayoutParams(params);
            containerLayout.addView(views.get(i));
        }
    }

    private void formatImage(String rawGuide) {
        int index = rawGuide.indexOf(mImageRegex);
        if (index == -1) {
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

        if (rawString.trim().length() == 0) {
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(rawString);
        rawString = formatBold(builder, rawString, mTextBoldRegex);
        rawString = formatBronze(builder, rawString);
        rawString = formatSilver(builder, rawString);
        rawString = formatGold(builder, rawString);
        rawString = formatPlatinum(builder, rawString);
        TextView text = new TextView(mContext);
        text.setText(builder);
        views.add(text);
    }

    private String formatBold(SpannableStringBuilder spannable,
                              String string, String regex) {
        int start = string.indexOf(regex);
        while (start != -1) {
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

    private String formatBronze(SpannableStringBuilder spannable,
                                  String string) {
        int start = string.indexOf(mBronzeRegex);
        while (start != -1) {
            string = string.replaceFirst(Pattern.quote(mBronzeRegex), "");
            int end = string.indexOf(mBronzeRegex);
            string = string.replaceFirst(Pattern.quote(mBronzeRegex), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + mBronzeRegex.length() * 2, bold);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
            spannable.setSpan(new ForegroundColorSpan(mBronze), start, end, 0);
            start = string.indexOf(mBronzeRegex);
        }
        return string;
    }

    private String formatSilver(SpannableStringBuilder spannable,
                              String string) {
        int start = string.indexOf(mSilverRegex);
        while (start != -1) {
            string = string.replaceFirst(Pattern.quote(mSilverRegex), "");
            int end = string.indexOf(mSilverRegex);
            string = string.replaceFirst(Pattern.quote(mSilverRegex), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + mSilverRegex.length() * 2, bold);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
            spannable.setSpan(new ForegroundColorSpan(mSilver), start, end, 0);
            start = string.indexOf(mSilverRegex);
        }
        return string;
    }

    private String formatGold(SpannableStringBuilder spannable,
                                String string) {
        int start = string.indexOf(mGoldRegex);
        while (start != -1) {
            string = string.replaceFirst(Pattern.quote(mGoldRegex), "");
            int end = string.indexOf(mGoldRegex);
            string = string.replaceFirst(Pattern.quote(mGoldRegex), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + mGoldRegex.length() * 2, bold);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
            spannable.setSpan(new ForegroundColorSpan(mGold), start, end, 0);
            start = string.indexOf(mGoldRegex);
        }
        return string;
    }

    private String formatPlatinum(SpannableStringBuilder spannable,
                              String string) {
        int start = string.indexOf(mPlatinumRegex);
        while (start != -1) {
            string = string.replaceFirst(Pattern.quote(mPlatinumRegex), "");
            int end = string.indexOf(mPlatinumRegex);
            string = string.replaceFirst(Pattern.quote(mPlatinumRegex), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + mPlatinumRegex.length() * 2, bold);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);
            spannable.setSpan(new ForegroundColorSpan(mPlatinum), start, end, 0);
            start = string.indexOf(mPlatinumRegex);
        }
        return string;
    }

}
