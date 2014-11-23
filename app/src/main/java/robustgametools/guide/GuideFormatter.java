package robustgametools.guide;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import robustgametools.model.TrophyGuide;

/**
 * A GuideFormatter class to
 * return the right view to be
 * displayed on the screen
 */
public class GuideFormatter {

    private static GuideFormatter mFormatter = null;
    private Context mContext;

    private GuideFormatter() {};

    public GuideFormatter getInstance(Context context) {
        if (mFormatter == null) {
            mFormatter = new GuideFormatter();
            mContext = context;
        }
        return mFormatter;
    }

    public View getView(String rawGuide) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView text = new TextView(mContext);
        text.setText(rawGuide);
        linearLayout.addView(text);
        return linearLayout;
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
