package robustgametools.playstation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import robustgametools.adapter.TrophyListAdapter;
import robustgametools.model.BaseActivity;
import robustgametools.model.Game;
import robustgametools.model.Profile;
import robustgametools.model.Trophy;
import robustgametools.playstation_guide.R;
import robustgametools.util.HttpClient;
import robustgametools.util.JsonFactory;
import robustgametools.util.Log;

public class GameActivity extends BaseActivity {

    private Game mGame;
    private Profile mProfile;

    @InjectView(R.id.game_image) ImageView mGameImage;
    @InjectView(R.id.title) TextView mTitle;
    @InjectView(R.id.bronze) TextView mBronze;
    @InjectView(R.id.silver) TextView mSilver;
    @InjectView(R.id.gold) TextView mGold;
    @InjectView(R.id.platinum) LinearLayout mPlatinum;
    @InjectView(R.id.progress) NumberProgressBar mProgress;
    @InjectView(R.id.trophies) ListView mTrophyList;
    @InjectView(R.id.loading) SmoothProgressBar mLoadingBar;
    @InjectView(R.id.loading_progress) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        Playstation playstation = Playstation.getInstance(this);
        mProfile = playstation.getProfile();

        setDisplayHomeAsUp();
        getGameInfo();
        setTitle(mGame.getTitle());
        initHeader();
        getTrophyList();
    }

    private void initHeader() {
        Picasso.with(getApplicationContext()).load(mGame.getTrophyIconUrl())
                .placeholder(R.drawable.placeholder_image).into(mGameImage);
        mBronze.setText(Integer.toString(mGame.getBronze()));
        mSilver.setText(Integer.toString(mGame.getSilver()));
        mGold.setText(Integer.toString(mGame.getGold()));
        int visibility = (mGame.getPlatinum() != 0) ? View.VISIBLE:View.GONE;
        mPlatinum.setVisibility(visibility);
        mProgress.setProgress(mGame.getProgress());
        mTitle.setText(mGame.getTitle());
    }

    private void getGameInfo() {
        Intent intent = getIntent();
        String gameData = intent.getStringExtra("info");
        mGame = new Gson().fromJson(gameData, Game.class);
    }

    private void getTrophyList() {
        HttpClient.getTrophies(mProfile.getOnlineId(), mGame.getNpCommunicationId(),
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String data = new String(responseBody);
                        initTrophyList(data);
                        changeLoadingVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.i("Get trophy info failed");
                        changeLoadingVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Failed to retrieve data." +
                                        " Check your network and try again.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initTrophyList(String data) {
        JsonFactory jsonFactory = JsonFactory.getInstance();
        ArrayList<Trophy> trophies = jsonFactory.parseTrophyList(data);

        //if (Playstation.sDebug) {
        //    TrophyGuide.parseFromTrophyList(trophies);
        //}

        TrophyListAdapter adapter = new TrophyListAdapter(this, trophies);
        mTrophyList.setAdapter(adapter);
    }

    private void changeLoadingVisibility(int visibility) {
        mLoadingBar.setVisibility(visibility);
        mProgressBar.setVisibility(visibility);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_game;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh_game) {
            getTrophyList();
            mLoadingBar.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        HttpClient.cancelTrophyRequest();
        super.onBackPressed();
    }
}
