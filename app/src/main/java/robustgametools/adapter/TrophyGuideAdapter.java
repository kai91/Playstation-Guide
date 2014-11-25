package robustgametools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import robustgametools.model.Guide;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;

/**
 * TrophyGuideAdapter to display
 * user's trophy info alongside
 * the guide's info
 */
public class TrophyGuideAdapter extends BaseAdapter {

    private TrophyGuide mTrophyGuide;
    private ArrayList<Guide> guides;
    private Context mContext;
    private LayoutInflater mInflater;

    public TrophyGuideAdapter(Context context, TrophyGuide trophyGuide) {
        mContext = context.getApplicationContext();
        mTrophyGuide = trophyGuide;
        guides = mTrophyGuide.getGuides();
        mInflater = LayoutInflater.from(mContext);
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

        if (position == 0) {
            holder.title.setText("Roadmap");
        } else {
            holder.title.setText(guides.get(position-1).title);
        }


        return view;
    }

    public static class ViewHolder {

        @InjectView(R.id.trophy_icon) CircleImageView trophyIcon;
        @InjectView(R.id.trophy_title) TextView title;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
