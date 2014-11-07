package robustgametools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.model.Game;
import robustgametools.playstation_guide.R;
import robustgametools.util.Log;

/**
 * An adapter to display list of games in user profile
 */
public class GameListAdapter extends BaseAdapter {

    private ArrayList<Game> mGames;
    private LayoutInflater mInflater;

    public GameListAdapter(Context context, ArrayList<Game> games) {
        mInflater = LayoutInflater.from(context);
        mGames = games;
    }

    @Override
    public int getCount() {
        return mGames.size();
    }

    @Override
    public Game getItem(int position) {
        return mGames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.card_game_stat, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
            Log.i("inflating: ", Integer.toString(position));
        }

        Game game = mGames.get(position);
        holder.title.setText(game.getTitle());
        //holder.platform.setText(game.getPlatform().toString());

        return view;
    }

    public static class ViewHolder {
        @InjectView(R.id.title) TextView title;
        @InjectView(R.id.platform) TextView platform;
        @InjectView(R.id.progress) NumberProgressBar progress;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
