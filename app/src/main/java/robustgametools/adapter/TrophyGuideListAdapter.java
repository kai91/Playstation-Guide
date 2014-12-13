package robustgametools.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;
import robustgametools.util.GuideDownloader;
import robustgametools.util.Storage;

public class TrophyGuideListAdapter extends BaseAdapter {

    private ArrayList<TrophyGuide> mGuides;
    private ArrayList<String> mDownloadedGuides;
    private Context mContext;
    private LayoutInflater mInflater;
    private SweetAlertDialog mDownloadDialog;
    private GuideDownloader mDownloader;

    public TrophyGuideListAdapter(Context context, ArrayList<TrophyGuide> guides,
                                  ArrayList<String> downloadedGuide) {
        mContext = context;
        mGuides = guides;
        mInflater = LayoutInflater.from(mContext);
        mDownloadedGuides = downloadedGuide;
        mDownloader = GuideDownloader.getInstance(mContext);
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
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_available_guide, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.guideTitle.setText(mGuides.get(position).getTitle());
        if (mDownloadedGuides.contains(mGuides.get(position).getTitle())) {
            holder.downloadIcon.setImageResource(R.drawable.ic_delete);
            holder.downloadIcon.setTag(R.drawable.ic_delete);
        } else {
            holder.downloadIcon.setImageResource(R.drawable.ic_file_download);
            holder.downloadIcon.setTag(R.drawable.ic_file_download);
        }

        holder.downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) holder.downloadIcon.getTag();
                if (tag == R.drawable.ic_file_download) {
                    downloadGuide(position);
                } else {
                    deleteGuide(mGuides.get(position).getTitle());
                    new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Guide deleted").show();
                }

            }
        });

        return view;
    }

    private void downloadGuide(final int position) {
        downloadGuide(mGuides.get(position).getTitle());
        mDownloadDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
        mDownloadDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDownloadDialog.setCancelText("Cancel").setTitleText("Downloading");
        mDownloadDialog.show();
        mDownloadDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mDownloader.cancelOngoingDownload();
                deleteGuide(mGuides.get(position).getTitle());
            }
        });
        mDownloadDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                mDownloadDialog.dismiss();
                mDownloader.cancelOngoingDownload();
                deleteGuide(mGuides.get(position).getTitle());
            }
        });
    }

    private void deleteGuide(String title) {
        mDownloadedGuides.remove(title);
        Storage storage = Storage.getInstance(mContext);
        storage.deleteGuide(title);
        notifyDataSetChanged();
    }

    private void downloadGuide(final String title) {
        mDownloader.downloadGuide(title, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                mDownloadDialog.dismiss();
                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Download complete").show();
                mDownloadedGuides.add(title);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(mContext, "Error downloading guide. Check your network" +
                        "and try again.", Toast.LENGTH_LONG).show();
                mDownloadDialog.dismiss();
            }
        });
    }

    static class ViewHolder {
        
        @InjectView(R.id.download) ImageView downloadIcon;
        @InjectView(R.id.title) TextView guideTitle;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
