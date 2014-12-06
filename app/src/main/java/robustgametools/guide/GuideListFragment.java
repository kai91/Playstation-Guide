package robustgametools.guide;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import robustgametools.playstation_guide.R;
import robustgametools.util.HttpClient;
import robustgametools.util.Log;

public class GuideListFragment extends Fragment {

    @InjectView(R.id.total) TextView mTotal;
    @InjectView(R.id.content) LinearLayout mContent;
    @InjectView(R.id.loading) ProgressBar mLoading;

    private GuideListListener mListener;

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
                Toast.makeText(getActivity(), new String (responseBody), Toast.LENGTH_LONG).show();
                mContent.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), ":C", Toast.LENGTH_LONG).show();

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
