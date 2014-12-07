package robustgametools.guide;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class GuideListFragment extends Fragment {

    @InjectView(R.id.content) LinearLayout mContent;
    @InjectView(R.id.loading) ProgressBar mLoading;
    @InjectView(R.id.guide_list) ListView mGuideList;

    private GuideListListener mListener;
    private ArrayList<TrophyGuide> mGuides;
    private TrophyGuideListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_guide, container, false);
        ButterKnife.inject(this, view);
        initGuide();
        return view;
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
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), ":C", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showResult() {
        mAdapter = new TrophyGuideListAdapter(getActivity(), mGuides);
        mGuideList.setAdapter(mAdapter);
        mGuideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = mGuides.get(position).getTitle();
                HttpClient.getGameGuide(title, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String guideInfo = new String (responseBody);
                        Intent intent = new Intent(getActivity(), GuideActivity.class);
                        intent.putExtra("guideInfo", guideInfo);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "Download error", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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
        public void onGuideSelected(String guideContent);
    }

}
