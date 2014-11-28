package robustgametools.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.playstation_guide.R;

/**
 * An adapter to display options in navigation
 * drawer
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private String[] mChoices = {"Home", "Guides", "Sign out"};
    private LayoutInflater mInflater;
    private int mCurrentlySelected = 0;

    public NavigationDrawerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mChoices.length;
    }

    @Override
    public String getItem(int position) {
        return mChoices[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void selectItem(int i) {
        mCurrentlySelected = i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.drawer_item_home_menu, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.text.setText(mChoices[position]);

        if (position == mCurrentlySelected) {
            int c = Color.parseColor("#EF5350"); // red
            holder.text.setTextColor(c);
        } else {
            int c = Color.parseColor("#757575"); // grey
            holder.text.setTextColor(c);
        }

        return view;
    }

    public static class ViewHolder {
        @InjectView(R.id.menu) TextView text;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }
}
