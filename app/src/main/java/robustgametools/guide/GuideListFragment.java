package robustgametools.guide;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.adapter.TrophyGuideListAdapter;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;
import robustgametools.util.HttpClient;
import robustgametools.util.Storage;

public class GuideListFragment extends Fragment {

    @InjectView(R.id.content) LinearLayout mContent;
    @InjectView(R.id.loading) ProgressBar mLoading;
    @InjectView(R.id.guide_list) ListView mGuideList;

    private GuideListListener mListener;
    private ArrayList<TrophyGuide> mGuides;
    private TrophyGuideListAdapter mAdapter;
    private ProgressDialog mLoadingDialog;
    private ArrayList<String> mDownloadedTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_guide, container, false);
        ButterKnife.inject(this, view);
        mDownloadedTitle = new ArrayList<>();
        initGuide();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDownloadedList();
    }

    public void initGuide() {
        HttpClient.getGuideList(0, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                mGuides = new Gson().fromJson(result, new TypeToken<List<TrophyGuide>>(){}.getType());
                mContent.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
                showResult();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                  Throwable error) {
                Toast.makeText(getActivity(), "Error downloading guide. " +
                        "Check your network and try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void refreshDownloadedList() {
        if (getActivity() != null) {
            Storage storage = Storage.getInstance(getActivity());
            ArrayList<String> newList = storage.getGuideList();
            mDownloadedTitle.clear();
            mDownloadedTitle.addAll(newList);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showResult() {
        mAdapter = new TrophyGuideListAdapter(getActivity(), mGuides, mDownloadedTitle);
        mGuideList.setAdapter(mAdapter);
        mGuideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String title = mGuides.get(position).getTitle();
                showProgressDialog();
                HttpClient.getGameGuide(title, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String guideInfo = new String (responseBody);
                        mLoadingDialog.dismiss();
                        mListener.onGuideSelected(guideInfo, mDownloadedTitle.contains(title));
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "Download error", Toast.LENGTH_LONG).show();
                        mLoadingDialog.dismiss();
                    }
                });
            }
        });
    }

    private void showProgressDialog() {
        mLoadingDialog = new ProgressDialog(getActivity());
        mLoadingDialog.setMessage("Loading...");
        mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                HttpClient.cancelGuideRequest();
                mLoadingDialog.dismiss();
            }
        });
        mLoadingDialog.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (GuideListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface GuideListListener {
        public void onGuideSelected(String guideContent, boolean isOffline);
    }

}
