package robustgametools.util;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class SpoilerTextView extends TextView {

    private final String mWarning = "Spoiler! (Tap to reveal)";

    public SpoilerTextView(Context context) {
        super(context);
    }

    public SpoilerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpoilerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSpoiler(CharSequence warning, final CharSequence content) {
        setText(warning + "\n");
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(content + "\n");
            }
        });

    }

    public void setSpoiler(final SpannableStringBuilder content) {
        setText(mWarning + "\n");
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(content.append("\n"));
                Linkify.addLinks((TextView) view, Linkify.ALL);
            }
        });
    }
}
