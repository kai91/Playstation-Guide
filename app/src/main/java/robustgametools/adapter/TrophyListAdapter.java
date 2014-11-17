package robustgametools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import robustgametools.model.Trophy;
import robustgametools.playstation_guide.R;

/**
 * TrophyListAdapter to show and
 * display views for trophy details
 */
public class TrophyListAdapter extends BaseAdapter{

    private ArrayList<Trophy> mTrophies;
    private Context mContext;
    private LayoutInflater mInflater;

    public TrophyListAdapter(Context context, ArrayList<Trophy> trophies) {
        mTrophies = trophies;
        mContext = context;
        mInflater = LayoutInflater.from(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return mTrophies.size();
    }

    @Override
    public Trophy getItem(int i) {
        return mTrophies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.card_trophy_stat, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Trophy trophy = mTrophies.get(i);

        return view;
    }

    public static class ViewHolder {

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
