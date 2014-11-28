package robustgametools.guide;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private GuideListListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_guide, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @OnClick(R.id.guide)
    public void onGuideSelected() {
        HttpClient.getGameGuide("https://api.myjson.com/bins/5aefj", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                Log.i(response);
                mListener.onGuideSelected(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                if (getActivity() == null) {
                    Toast.makeText(getActivity(), "Error downloading guide, check your network and" +
                            "try again", Toast.LENGTH_LONG).show();
                }
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
