package robustgametools.playstation;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.adapter.GameListAdapter;
import robustgametools.model.Game;
import robustgametools.model.Profile;
import robustgametools.playstation_guide.R;
import robustgametools.util.HttpClient;
import robustgametools.util.JsonFactory;
import robustgametools.util.Log;
import robustgametools.util.Storage;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static Profile mProfile;

    private HomeFragmentListener mListener;

    @InjectView(R.id.profile_image) ImageView mProfileImage;
    @InjectView(R.id.username) TextView mUsername;
    @InjectView(R.id.level) TextView mLevel;
    @InjectView(R.id.progress) NumberProgressBar mProgress;
    @InjectView(R.id.bronze) TextView mBronze;
    @InjectView(R.id.silver) TextView mSilver;
    @InjectView(R.id.gold) TextView mGold;
    @InjectView(R.id.platinum) TextView mPlatinum;
    @InjectView(R.id.games) ListView mGameList;
    @InjectView(R.id.plus) ImageView mPlus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        JsonFactory jsonFactory = JsonFactory.getInstance();
        Storage storage = Storage.getInstance(getActivity());
        String data = storage.readUserData();
        Log.i(TAG, data);
        mProfile = jsonFactory.parseUserProfile(data);
        String gameData = storage.readGameData();
        ArrayList<Game> recentGames = jsonFactory.parseGames(gameData);
        mProfile.setGames(recentGames);
        int gameCount = jsonFactory.parseGameCount(gameData);
        mProfile.setGameCount(gameCount);

        // Update the user game list
        // If we already got all user's games, return
        if (mProfile.getGameCount() == recentGames.size()) {
            return;
        }

        Bundle args = getArguments();
        boolean justUpdated = args.getBoolean("RECENTLY_UPDATED");
        if (justUpdated) {
            updateList(100);
            Log.i("Updating: Skip first 100 games");
        } else {
            updateList(0);
            Log.i("Updating: All games");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        initHeader();
        initGameList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home, menu);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGameClicked(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (HomeFragmentListener) activity;
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

    private void initHeader() {
        Picasso.with(getActivity()).load(mProfile.getAvatarUrl())
                .into(mProfileImage);
        mUsername.setText(mProfile.getOnlineId());
        mLevel.setText("Level " + mProfile.getLevel());
        mProgress.setProgress(mProfile.getProgress());
        mBronze.setText(Integer.toString(mProfile.getBronze()));
        mSilver.setText(Integer.toString(mProfile.getSilver()));
        mGold.setText(Integer.toString(mProfile.getGold()));
        mPlatinum.setText(Integer.toString(mProfile.getPlatinum()));
        if (mProfile.isPlus()) {
            mPlus.setVisibility(View.VISIBLE);
        }
    }

    private void initGameList() {
        GameListAdapter adapter = new GameListAdapter(getActivity(), mProfile.getGames());
        mGameList.setAdapter(adapter);
    }

    /**
     * Updates game list in background
     * @param offset of the games to skip
     */
    private void updateList(int offset) {
        int totalGameCount = mProfile.getGameCount();
        HttpClient.getGames(mProfile.getOnlineId(), offset, totalGameCount,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String data = new String(responseBody);
                        JsonFactory jsonFactory = JsonFactory.getInstance();
                        jsonFactory.parseGames(data, mProfile.getGames());
                        Log.i("Game size: " + mProfile.getGames().size());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "Failed updating data", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public interface HomeFragmentListener {
        public void onGameClicked(Uri uri);
    }

}
