package robustgametools.adapter;

import android.content.Context;
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;

        if (view == null) {
            view = mInflater.inflate(R.layout.drawer_layout_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.menu.setText(mChoices[position]);

        return view;
    }

    public static class ViewHolder {

        @InjectView(R.id.menu) TextView menu;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

    }
}
