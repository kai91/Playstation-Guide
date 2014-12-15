package robustgametools.guide;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.adapter.TrophyGuideAdapter;
import robustgametools.model.BaseActivity;
import robustgametools.model.Profile;
import robustgametools.model.Trophy;
import robustgametools.model.TrophyGuide;
import robustgametools.model.Types;
import robustgametools.playstation.Playstation;
import robustgametools.playstation_guide.R;
import robustgametools.util.GuideDownloader;
import robustgametools.util.HttpClient;
import robustgametools.util.JsonFactory;
import robustgametools.util.Log;
import robustgametools.util.Storage;

public class GuideActivity extends BaseActivity {

    @InjectView(R.id.drawer) DrawerLayout mDrawer;
    @InjectView(R.id.drawer_menu) ListView mDrawerMenu;

    private ActionBarDrawerToggle mDrawerToggle;
    private TrophyGuide mTrophyGuide;
    private boolean mIsOffline;
    private int mCurrentPosition = 0;
    private int mPlatformChoice = 0;
    private int mNavigationIndex = -1;
    private boolean mExit = false;
    private Toast mToast;
    private Profile mProfile;
    private ArrayList<Boolean> mTrophyInfo = new ArrayList<>();
    private TrophyGuideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mProfile = Playstation.getInstance(this).getProfile();
        getTrophyGuideInfo();

        if (savedInstanceState == null) {
            Fragment frag = new GuideFragment();
            Bundle bundle = new Bundle();
            bundle.putString("rawGuide", mTrophyGuide.getRoadmap());
            bundle.putString("title", mTrophyGuide.getTitle());
            bundle.putBoolean("isOffline", mIsOffline);
            frag.setArguments(bundle);

            getFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }

        setTitle(mTrophyGuide.getTitle());
        updateTrophyInfo();
        initDrawer();

    }

    private void choosePlatform() {
        ArrayList<Types> platforms = mTrophyGuide.getPlatforms();
        ListView choiceList = new ListView(this);
        String[] choices = new String[platforms.size()];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = platforms.get(i).type;
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, choices);
        choiceList.setAdapter(adapter);
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Choose platform");
        dialog.setContentView(choiceList);
        choiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPlatformChoice != position) {
                    mPlatformChoice = position;
                    updateTrophyInfo();
                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void getTrophyGuideInfo() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("guideInfo");
        mTrophyGuide = new Gson().fromJson(json, TrophyGuide.class);
        mIsOffline = intent.getBooleanExtra("isOffline", false);
    }

    private void readTrophyInfo() {
        Storage storage = Storage.getInstance(this);
        String info = storage.readTrophyInfo(mTrophyGuide.getTitle(), mProfile.getOnlineId());
        ArrayList<Trophy> trophies = JsonFactory.getInstance().parseTrophyList(info);
        mTrophyInfo.clear();
        for (int i = 0; i < trophies.size(); i++) {
            mTrophyInfo.add(trophies.get(i).isEarned());
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        Log.i("s " + mTrophyInfo.size());
    }

    private void initDrawer() {
        setActionBarIcon(R.drawable.ic_drawer);
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                mToolbar,
                R.string.app_name,
                R.string.app_name
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (mNavigationIndex != -1) {
                    onNavigationItemSelected(mNavigationIndex);
                    mCurrentPosition = mNavigationIndex;
                    mNavigationIndex = -1;
                }
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        mAdapter = new TrophyGuideAdapter(this, mTrophyGuide, mTrophyInfo);
        mDrawerMenu.setAdapter(mAdapter);
        mDrawerMenu.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mNavigationIndex = position;
                mDrawerMenu.setItemChecked(position, true);
                mAdapter.setSelection(position);
                mDrawer.closeDrawers();
            }
        });
        mDrawer.openDrawer(Gravity.START);
    }

    private void onNavigationItemSelected(int position) {

        if (position == mCurrentPosition) {
            return;
        }

        String guide;
        Fragment frag = new GuideFragment();
        Bundle bundle = new Bundle();

        if (position == 0) {
            guide = mTrophyGuide.getRoadmap();
            bundle.putString("rawGuide",guide);
        } else {
            guide = new Gson().toJson(mTrophyGuide.getGuides().get(position-1));
            bundle.putString("guide", guide);
        }
        bundle.putString("title", mTrophyGuide.getTitle());
        bundle.putBoolean("isOffline", mIsOffline);

        frag.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, frag)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_guide;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(Gravity.START)) {
            mDrawer.closeDrawers();
        } else {
            if (mExit) {
                super.onBackPressed();
            } else {
                mExit = true;
                Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mExit = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        int guideMenu;
        if (mIsOffline) {
            guideMenu = R.menu.trophy_guide_offline;
        } else {
            guideMenu = R.menu.trophy_guide;
        }
        getMenuInflater().inflate(guideMenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_update_trophy_info) {
            updateTrophyInfo();
            return true;
        } else if (id == R.id.action_exit_guide) {
            finish();
            return true;
        } else if (id == R.id.action_save_trophy_guide) {
            GuideDownloader downloader = GuideDownloader.getInstance(this);
            mToast.setText("Downloading guide...");
            mToast.show();
            downloader.downloadGuide(mTrophyGuide.getTitle(), new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    mToast.setText("Guide downloaded");
                    mToast.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    mToast.setText("Download failed");
                    mToast.show();
                }
            });
            return true;
        } else if (id == R.id.action_switch_platform) {
            choosePlatform();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateTrophyInfo() {
        showToast("Fetching your trophy info");
        HttpClient.getTrophies(mProfile.getOnlineId(),
                mTrophyGuide.getPlatforms().get(mPlatformChoice).npId,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        persistTrophyInfo(new String(responseBody));
                        showToast("Your trophy info updated");
                        readTrophyInfo();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        showToast("Failed to fetch your trophy info");
                    }
                });
    }

    private void persistTrophyInfo(String data) {
        Storage storage = Storage.getInstance(this);
        storage.persistTrophyInfo(mTrophyGuide.getTitle(), mProfile.getOnlineId(), data);
    }

    private void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(msg);
            mToast.show();
        }
    }
}
