package robustgametools.playstation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.model.BaseActivity;
import robustgametools.model.Game;
import robustgametools.playstation_guide.R;
import robustgametools.util.Log;

public class GameActivity extends BaseActivity {

    private Game mGame;

    @InjectView(R.id.game_image) ImageView mGameImage;
    @InjectView(R.id.title) TextView mTitle;
    @InjectView(R.id.bronze) TextView mBronze;
    @InjectView(R.id.silver) TextView mSilver;
    @InjectView(R.id.gold) TextView mGold;
    @InjectView(R.id.platinum) LinearLayout mPlatinum;
    @InjectView(R.id.progress) NumberProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        setDisplayHomeAsUp();
        getGameInfo();
        setTitle(mGame.getTitle());
        initHeader();
        displayInfo();
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

    private void displayInfo() {
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
