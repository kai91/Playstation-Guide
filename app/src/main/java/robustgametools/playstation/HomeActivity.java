package robustgametools.playstation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.guide.GuideActivity;
import robustgametools.guide.GuideHomeFragment;
import robustgametools.guide.GuideListFragment;
import robustgametools.guide.MyGuideFragment;
import robustgametools.model.BaseActivity;
import robustgametools.model.Game;
import robustgametools.playstation_guide.R;
import robustgametools.signin.SignInActivity;
import robustgametools.util.Storage;

public class HomeActivity extends BaseActivity
        implements HomeFragment.HomeFragmentListener,
        GuideListFragment.GuideListListener,
        MyGuideFragment.OnFragmentInteractionListener {

    @InjectView(R.id.drawer) DrawerLayout mDrawer;

    private ActionBarDrawerToggle mDrawerToggle;
    private int mCurrentlySelected = 0;
    private int mNavigationIndex = -1;
    private boolean mExit = false;
    private ArrayList<TextView> mMenuText;

    private final int[] NAVDRAWER_ITEM_ID = new int[] {
            R.id.home,
            R.id.guides,
            R.id.exit
    };

    private final int[] NAVDRAWER_TITLE_RES_ID = new int[] {
            R.string.nav_home,
            R.string.nav_guides,
            R.string.nav_exit
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            // Propogate the state of user's data
            HomeFragment fragment = new HomeFragment();
            Bundle bundle = getIntent().getExtras();
            Bundle argBundle = new Bundle();
            if (bundle != null) {
                boolean justUpdated = bundle.getBoolean("RECENTLY_UPDATED");
                argBundle.putBoolean("RECENTLY_UPDATED", justUpdated);
                fragment.setArguments(argBundle);
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        mCurrentlySelected = preferences.getInt("navigationIndex", 0);
        initDrawer();
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
                if (mNavigationIndex == 0) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new HomeFragment())
                            .commit();
                }
                else if (mNavigationIndex == 1) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new GuideHomeFragment())
                            .commit();

                } else if (mNavigationIndex == 2) {
                    signOut();
                }
                mNavigationIndex = -1;
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        setUpNavDrawer();
    }

    private void setUpNavDrawer() {
        mMenuText = new ArrayList<>();
        for (int i = 0; i < NAVDRAWER_ITEM_ID.length; i++) {
            final int j = i;
            TextView view = (TextView) findViewById(NAVDRAWER_ITEM_ID[i]).findViewById(R.id.menu);
            view.setText(NAVDRAWER_TITLE_RES_ID[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationItemSelected(j);
                }
            });
            mMenuText.add(view);
        }
        formatNavDrawer(0);
    }

    private void formatNavDrawer(int index) {
        int red = Color.parseColor("#EF5350"); // red
        int gray = Color.parseColor("#757575"); // grey
        for (int i = 0; i <mMenuText.size(); i++) {
            TextView text = mMenuText.get(i);
            if (i == index) {
                text.setTextColor(red);
            } else {
                text.setTextColor(gray);
            }
        }
    }

    public void signOut() {
        Storage storage = Storage.getInstance(this);
        storage.deleteUserData();
        Playstation playstation = Playstation.getInstance(this);
        playstation.destroy();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("navigationIndex");
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void showGameDetail(Game game) {
        Intent intent = new Intent(this, GameActivity.class);
        String gameData = new Gson().toJson(game);
        intent.putExtra("info", gameData);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(Gravity.START)) {
            mDrawer.closeDrawers();
        } else {
            if (mCurrentlySelected != 0) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new HomeFragment())
                        .commit();
                mCurrentlySelected = 0;
            }
            else if (mExit) {
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

    private void onNavigationItemSelected(int position) {
        if (position != mCurrentlySelected) {
            mNavigationIndex = position;
            mCurrentlySelected = position;
            formatNavDrawer(position);
        }
        mDrawer.closeDrawers();
    }

    private void saveNavigationPos() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("navigationIndex", mCurrentlySelected);
        editor.apply();
    }

    @Override
    public void onGuideSelected(String guideContent, boolean isOffline) {
        saveNavigationPos();
        startGuideActivity(guideContent, isOffline);
    }

    @Override
    public void onDownloadedGuideSelected(String name) {
        Storage storage = Storage.getInstance(this);
        String guideInfo = storage.readGuide(name);
        saveNavigationPos();
        startGuideActivity(guideInfo, true);
    }

    private void startGuideActivity(String guideContent, boolean isOffline) {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra("guideInfo", guideContent);
        intent.putExtra("isOffline", isOffline);
        startActivity(intent);
    }
}
