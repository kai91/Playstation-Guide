package robustgametools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;

public class TrophyGuideListAdapter extends BaseAdapter {

    private ArrayList<TrophyGuide> mGuides;
    private Context mContext;
    private LayoutInflater mInflater;

    public TrophyGuideListAdapter(Context context, ArrayList<TrophyGuide> guides) {
        mContext = context;
        mGuides = guides;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mGuides.size();
    }

    @Override
    public TrophyGuide getItem(int i) {
        return mGuides.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_available_guide, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.guideTitle.setText(mGuides.get(position).getTitle());

        return view;
    }

    static class ViewHolder {
        
        @InjectView(R.id.download) ImageView downloadIcon;
        @InjectView(R.id.title) TextView guideTitle;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
