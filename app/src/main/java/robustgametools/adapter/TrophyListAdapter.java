package robustgametools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.model.Trophy;
import robustgametools.model.TrophyLevel;
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
        mContext = context.getApplicationContext();
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
            view = mInflater.inflate(R.layout.list_trophy_stat, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Trophy trophy = mTrophies.get(i);

        holder.trophyTypeBronze.setVisibility(View.GONE);
        holder.trophyTypeSilver.setVisibility(View.GONE);
        holder.trophyTypeGold.setVisibility(View.GONE);
        holder.trophyTypePlatinum.setVisibility(View.GONE);
        holder.trophyTypeSecret.setVisibility(View.GONE);
        holder.earned.setVisibility(View.GONE);
        boolean earned = trophy.isEarned();

        if (!trophy.isHidden()) {
            holder.title.setText(trophy.getTitle());
            holder.description.setText(trophy.getDescription());
            TrophyLevel level = trophy.getTrophyLevel();

            Picasso.with(mContext).load(trophy.getIconUrl()).
                    placeholder(R.drawable.placeholder_trophy).
                    into(holder.trophyIcon);

            switch (level) {
                case BRONZE:
                    holder.trophyTypeBronze.setVisibility(View.VISIBLE);
                    if (earned) setEarned(holder.earned, R.drawable.bronze);
                    break;
                case SILVER:
                    holder.trophyTypeSilver.setVisibility(View.VISIBLE);
                    if (earned) setEarned(holder.earned, R.drawable.silver);
                    break;
                case GOLD:
                    holder.trophyTypeGold.setVisibility(View.VISIBLE);
                    if (earned) setEarned(holder.earned, R.drawable.gold);
                    break;
                case PLATINUM:
                    holder.trophyTypePlatinum.setVisibility(View.VISIBLE);
                    if (earned) setEarned(holder.earned, R.drawable.platinum);
                    break;
                default:
                    holder.trophyTypeSecret.setVisibility(View.VISIBLE);
            }


        } else {
            Picasso.with(mContext).load(R.drawable.placeholder_trophy).into(holder.trophyIcon);
            holder.title.setText("This trophy is hidden");
            holder.trophyTypeSecret.setVisibility(View.VISIBLE);
            holder.description.setText("");
            if (earned) setEarned(holder.earned, R.drawable.secret);
        }

        return view;
    }

    private void setEarned(ImageView trophy, int type) {
        Picasso.with(mContext).load(type).into(trophy);
        trophy.setVisibility(View.VISIBLE);
    }

    public static class ViewHolder {

        @InjectView(R.id.trophy_type_bronze) ImageView trophyTypeBronze;
        @InjectView(R.id.trophy_type_silver) ImageView trophyTypeSilver;
        @InjectView(R.id.trophy_type_gold) ImageView trophyTypeGold;
        @InjectView(R.id.trophy_type_platinum) ImageView trophyTypePlatinum;
        @InjectView(R.id.trophy_type_secret) ImageView trophyTypeSecret;
        @InjectView(R.id.title) TextView title;
        @InjectView(R.id.description) TextView description;
        @InjectView(R.id.trophy_icon) ImageView trophyIcon;
        @InjectView(R.id.earned) ImageView earned;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
