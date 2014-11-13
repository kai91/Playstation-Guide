package robustgametools.playstation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import robustgametools.adapter.GameListAdapter;
import robustgametools.model.Game;
import robustgametools.model.Profile;
import robustgametools.playstation_guide.R;
import robustgametools.util.HttpClient;
import robustgametools.util.JsonFactory;
import robustgametools.util.Log;
import robustgametools.util.Storage;


public class HomeFragment extends Fragment {

    private static Profile mProfile;
    private static Playstation mPlaystation;

    private HomeFragmentListener mListener;
    private GameListAdapter adapter;

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
    @InjectView(R.id.loading) SmoothProgressBar mLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mPlaystation = Playstation.getInstance(getActivity());
        mProfile = mPlaystation.getProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        initHeader();
        initGameList();

        // Update the user game list
        Bundle args = getArguments();
        if (args != null) {
            boolean justUpdated = args.getBoolean("RECENTLY_UPDATED");
            if (justUpdated) {
                updateList(100);
                Log.i("Updating: Skip first 100 games");
            } else {
                updateHeader();
                updateList(0);
                Log.i("Updating: All games");
            }
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int index = preferences.getInt("index", 0);
        int top = preferences.getInt("top", 0);
        mGameList.setSelectionFromTop(index, top);

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
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        int index = mGameList.getFirstVisiblePosition();
        View v = mGameList.getChildAt(0);
        int top = (v == null) ? 0 : v.getTop();
        bundle.putInt("index", index);
        bundle.putInt("top", top);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("index", index);
        editor.putInt("top", top);
        editor.apply();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                updateHeader();
                updateList(0);
                mLoading.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void updateHeader() {
        HttpClient.signIn(mProfile.getOnlineId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data = new String(responseBody);
                mPlaystation.persistProfile(data);
                JsonFactory json = JsonFactory.getInstance();
                Profile newProfile = json.parseUserProfile(data);
                mProfile.setLevel(newProfile.getLevel());
                mProfile.setProgress(newProfile.getProgress());
                mProfile.setBronze(newProfile.getBronze());
                mProfile.setSilver(newProfile.getSilver());
                mProfile.setGold(newProfile.getGold());
                mProfile.setPlatinum(newProfile.getPlatinum());
                mProfile.setAvatarUrl(newProfile.getAvatarUrl());
                initHeader();
                Log.i("HomeFragment: updated header");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("HomeFragment: something wrong when updating header");
            }
        });
    }

    private void initGameList() {
        adapter = new GameListAdapter(getActivity(), mProfile.getGames());
        mGameList.setAdapter(adapter);
        mGameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateList(int offset) {
        mLoading.setVisibility(View.VISIBLE);
        if (offset == 0) {
            HttpClient.getRecentlyPlayedGames(mProfile.getOnlineId(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String data = new String(responseBody);
                    JsonFactory jsonFactory = JsonFactory.getInstance();
                    ArrayList<Game> games = jsonFactory.parseGames(data);
                    ArrayList<Game> current = mProfile.getGames();
                    current.clear();
                    adapter.notifyDataSetChanged();
                    current.addAll(games);
                    adapter.notifyDataSetChanged();
                    updateList(mProfile.getGames().size());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("Updating game list: Failed");
                    //Toast.makeText(getActivity(), "Failed updating data", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        int totalGameCount = mProfile.getGameCount();
        if (totalGameCount <= offset) {
            // Finished updating all games
            mLoading.setVisibility(View.GONE);
        } else {
            retrieveList(mProfile.getGames().size());
        }
    }

    /**
     * Updates game list in background
     * @param offset of the games to skip
     */
    private void retrieveList(int offset) {
        HttpClient.getGames(mProfile.getOnlineId(), offset,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String data = new String(responseBody);
                        JsonFactory jsonFactory = JsonFactory.getInstance();
                        jsonFactory.parseGames(data, mProfile.getGames());
                        adapter.notifyDataSetChanged();
                        Log.i("Game size: " + mProfile.getGames().size());
                        updateList(mProfile.getGames().size());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("Updating game list: Failed");
                        Toast.makeText(getActivity(), "Failed updating data", Toast.LENGTH_LONG).show();
                        mLoading.setVisibility(View.GONE);
                    }
                });
    }

    public interface HomeFragmentListener {
        public void onGameClicked(Uri uri);
    }

}
