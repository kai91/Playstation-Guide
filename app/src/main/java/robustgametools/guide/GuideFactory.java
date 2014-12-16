package robustgametools.guide;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.regex.Pattern;

import robustgametools.model.Guide;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;
import robustgametools.util.Log;
import robustgametools.util.SpoilerTextView;
import robustgametools.util.Storage;
import robustgametools.util.TrophyColor;

/**
 * A GuideFormatter class to
 * return the right view to be
 * displayed on the screen
 */
public class GuideFactory {

    private static GuideFactory mFormatter = null;
    private static Context mContext;
    private static ArrayList<View> views;
    private static Storage mStorage;
    private static boolean mIsOffline;
    private static String mTitle;

    private static final String mImageRegex = "[D]";
    private static final String mTextBoldRegex = "[T]";
    private static final String mSymbolRegex = "[+]";
    private static final String mPlatinumRegex = "[P]";
    private static final String mGoldRegex = "[G]";
    private static final String mSilverRegex = "[S]";
    private static final String mBronzeRegex = "[B]";
    private static final String mSpoilerRegex = "[Q]";



    private static final int mGreen = Color.parseColor("#2db680");
    private static final int mBlue = Color.parseColor("#5f7ab9");
    private static final int mPurple = Color.parseColor("#c67eb1");
    private static final int mRed = Color.parseColor("#e76c78");

    private GuideFactory() {}

    public static GuideFactory getInstance(Context context) {
        if (mFormatter == null) {
            mFormatter = new GuideFactory();
            mContext = context;
        }
        return mFormatter;
    }

    public static GuideFactory getInstance(Context context, String title) {
        mTitle = title;
        return getInstance(context);
    }

    public GuideFactory format(String rawGuide) {
        // checks for empty string with just spaces
        rawGuide = rawGuide.trim();
        if (rawGuide.length() != 0) {
            if (views == null) {
                views = new ArrayList<>();
            } else {
                views.clear();
            }
            if (mStorage == null) {
                mStorage = Storage.getInstance(mContext);
            }
            Log.i("Boolean: " +mIsOffline);
            formatImage(rawGuide);
        }

        return this;
    }

    public GuideFactory isOffline(boolean status) {
        mIsOffline = status;
        return this;
    }

    public void into(LinearLayout containerLayout) {
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            if (view instanceof ImageView) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER;
                view.setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    view.setLayoutParams(params);
                }
                containerLayout.addView(view);
        }
    }

    private void formatImage(String rawGuide) {
        int start = rawGuide.indexOf(mImageRegex);
        if (start == -1) {
            // no image in guide, proceed to extract text
            formatSpoiler(rawGuide);
        } else {
            formatSpoiler(rawGuide.substring(0, start));
            rawGuide = rawGuide.replaceFirst(Pattern.quote(mImageRegex), "");
            int end = rawGuide.indexOf(mImageRegex);
            rawGuide = rawGuide.replaceFirst(Pattern.quote(mImageRegex), "");
            String url = rawGuide.substring(start, end);
            ImageView imageView = new ImageView(mContext);
            if (mIsOffline) {
                url = mStorage.convertUrlToOfflineUri(mTitle, url);
                Picasso.with(mContext).load("file://" + url).resizeDimen(R.dimen.guide_image,
                        R.dimen.guide_image).centerInside().into(imageView);
            } else {
                Picasso.with(mContext).load(url).resizeDimen(R.dimen.guide_image,
                        R.dimen.guide_image).centerInside().into(imageView);
            }
            views.add(imageView);
            formatImage(rawGuide.substring(end, rawGuide.length()));
        }
    }

    private void formatSpoiler(String rawGuide) {
        int start = rawGuide.indexOf(mSpoilerRegex);
        if (start == -1) {
            formatText(rawGuide);
        } else {
            formatText(rawGuide.substring(0, start).trim());
            rawGuide = rawGuide.replaceFirst(Pattern.quote(mSpoilerRegex), "");
            int end = rawGuide.indexOf(mSpoilerRegex);
            rawGuide = rawGuide.replaceFirst(Pattern.quote(mSpoilerRegex), "");
            String rawSpoiler = rawGuide.substring(start, end);
            SpoilerTextView spoiler = new SpoilerTextView(mContext);
            spoiler.setSpoiler(placeHolderFormat(rawSpoiler));
            spoiler.setTextColor(Color.BLACK);
            views.add(spoiler);
            formatSpoiler(rawGuide.substring(end, rawGuide.length()));
        }

    }

    // This should be refactored after this, as this is redundant code, see formatText() below
    private SpannableStringBuilder placeHolderFormat(String rawString) {
        SpannableStringBuilder builder = new SpannableStringBuilder(rawString);
        rawString = formatBold(builder, rawString, mTextBoldRegex);
        rawString = formatBronze(builder, rawString);
        rawString = formatSilver(builder, rawString);
        rawString = formatGold(builder, rawString);
        rawString = formatPlatinum(builder, rawString);
        formatButton(builder, rawString);
        return builder;
    }

    private void formatText(String rawString) {

        if (rawString.trim().length() == 0) {
            return;
        }

        SpannableStringBuilder builder = new SpannableStringBuilder(rawString);
        builder.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, rawString.length(), 0);
        rawString = formatBold(builder, rawString, mTextBoldRegex);
        rawString = formatBronze(builder, rawString);
        rawString = formatSilver(builder, rawString);
        rawString = formatGold(builder, rawString);
        rawString = formatPlatinum(builder, rawString);
        formatButton(builder, rawString);
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
            spannable.setSpan(new ForegroundColorSpan(TrophyColor.BRONZE), start, end, 0);
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
            spannable.setSpan(new ForegroundColorSpan(TrophyColor.SILVER), start, end, 0);
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
            spannable.setSpan(new ForegroundColorSpan(TrophyColor.GOLD), start, end, 0);
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
            spannable.setSpan(new ForegroundColorSpan(TrophyColor.PLATINUM), start, end, 0);
            start = string.indexOf(mPlatinumRegex);
        }
        return string;
    }

    private String formatButton(SpannableStringBuilder spannable,
                                  String string) {
        int start = string.indexOf(mSymbolRegex);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "playstation.TTF");
        while (start != -1) {
            string = string.replaceFirst(Pattern.quote(mSymbolRegex), "");
            int end = string.indexOf(mSymbolRegex);
            string = string.replaceFirst(Pattern.quote(mSymbolRegex), "");
            String bold = string.substring(start, end);
            spannable.replace(start, end + mSymbolRegex.length() * 2, bold);
            spannable.setSpan(new PlaystationTypefaceSpan("", font), start, end, 0);
            spannable.setSpan(new RelativeSizeSpan(1.4f), start, end, 0);
            for (int i = start; i < end; i++) {
                char button = spannable.charAt(i);
                if (button == 's' || button == 'S') {
                    spannable.setSpan(new ForegroundColorSpan(mPurple), i, i+1, 0);
                } else if (button == 'c' || button == 'C') {
                    spannable.setSpan(new ForegroundColorSpan(mRed), i, i+1, 0);
                } else if (button == 't' || button == 'T') {
                    spannable.setSpan(new ForegroundColorSpan(mGreen), i, i+1, 0);
                } else if (button == 'x' || button == 'X') {
                    spannable.setSpan(new ForegroundColorSpan(mBlue), i, i+1, 0);
                }
            }
            start = string.indexOf(mSymbolRegex);
        }
        return string;
    }

    public ArrayList<String> extractImageUrl(TrophyGuide trophyGuide) {
        ArrayList<String> result = new ArrayList<String>();
        result.addAll(extractImageFrom(trophyGuide.getRoadmap()));

        ArrayList<Guide> guides = trophyGuide.getGuides();
        for (int i = guides.size()-1; i >= 0; i--) {
            result.addAll(extractImageFrom(guides.get(i).guide));
            result.add(guides.get(i).url);
        }

        return result;
    }

    private ArrayList<String> extractImageFrom(String guide) {
        ArrayList<String> links = new ArrayList<String>();

        int start = guide.indexOf(mImageRegex);
        while(start != -1) {
            guide = guide.replaceFirst(Pattern.quote(mImageRegex), "");
            int end = guide.indexOf(mImageRegex);
            guide = guide.replaceFirst(Pattern.quote(mImageRegex), "");
            String url = guide.substring(start, end);
            links.add(url);
            start = guide.indexOf(mImageRegex);
        }

        return links;
    }

}
