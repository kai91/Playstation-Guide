package robustgametools.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import robustgametools.model.Guide;
import robustgametools.model.TrophyGuide;
import robustgametools.model.TrophyLevel;
import robustgametools.playstation_guide.R;
import robustgametools.util.TrophyColor;

/**
 * TrophyGuideAdapter to display
 * user's trophy info alongside
 * the guide's info
 */
public class TrophyGuideAdapter extends BaseAdapter {

    private TrophyGuide mTrophyGuide;
    private ArrayList<Guide> guides;
    private ArrayList<Boolean> mTrophyInfo;
    private Context mContext;
    private LayoutInflater mInflater;
    private int mCurrentSelected = 0;

    public TrophyGuideAdapter(Context context, TrophyGuide trophyGuide,
                              ArrayList<Boolean> trophyInfo) {
        mContext = context.getApplicationContext();
        mTrophyGuide = trophyGuide;
        guides = mTrophyGuide.getGuides();
        mTrophyInfo = trophyInfo;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setSelection(int currentSelected) {
        mCurrentSelected = currentSelected;
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.drawer_item_trophy, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position == mCurrentSelected) {
            holder.title.setTypeface(null, Typeface.BOLD);
        } else {
            holder.title.setTypeface(null, Typeface.NORMAL);
        }

        if (position == 0) {
            holder.title.setText("Roadmap");
            holder.trophyIcon.setImageDrawable(null);
            holder.status.setVisibility(View.GONE);
        } else {
            Guide guide = guides.get(position - 1);
            Picasso.with(mContext).load(guide.url).
                    placeholder(R.drawable.placeholder_trophy).into(holder.trophyIcon);
            holder.title.setText(guide.title);
            if (mTrophyInfo.size() != 0 && mTrophyInfo.get(position-1)) {
                holder.status.setVisibility(View.VISIBLE);
            } else {
                holder.status.setVisibility(View.GONE);
            }
            TrophyLevel level = TrophyLevel.valueOf(guide.type);
            switch (level) {
                case BRONZE:
                    holder.status.setTextColor(TrophyColor.BRONZE);
                    break;
                case SILVER:
                    holder.status.setTextColor(TrophyColor.SILVER);
                    break;
                case GOLD:
                    holder.status.setTextColor(TrophyColor.GOLD);
                    break;
                default:
                    holder.status.setTextColor(TrophyColor.PLATINUM);
            }
        }

        return view;
    }

    public static class ViewHolder {

        @InjectView(R.id.trophy_icon) CircleImageView trophyIcon;
        @InjectView(R.id.trophy_title) TextView title;
        @InjectView(R.id.status) ShimmerTextView status;
        Shimmer shimmer;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            if (shimmer == null) {
                shimmer = new Shimmer();
                shimmer.start(status);
            }
        }
    }
}
