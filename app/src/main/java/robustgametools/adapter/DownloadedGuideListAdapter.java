package robustgametools.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.playstation_guide.R;
import robustgametools.util.Storage;

public class DownloadedGuideListAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> mGuideList;
    private LayoutInflater mInflater;

    public DownloadedGuideListAdapter(Context context, ArrayList<String> guideList) {
        mContext = context;
        mGuideList = guideList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mGuideList.size();
    }

    @Override
    public String getItem(int position) {
        return mGuideList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if(view == null) {
            view = mInflater.inflate(R.layout.list_downloaded_guide, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.title.setText(mGuideList.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGuide(mGuideList.get(position));
                Toast.makeText(mContext, "Guide deleted", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void deleteGuide(String title) {
        mGuideList.remove(title);
        Storage storage = Storage.getInstance(mContext);
        storage.deleteGuide(title);
        notifyDataSetChanged();
    }

    static class ViewHolder {

        @InjectView(R.id.title) TextView title;
        @InjectView(R.id.delete) ImageView delete;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
