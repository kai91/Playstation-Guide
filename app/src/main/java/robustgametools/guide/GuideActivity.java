package robustgametools.guide;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.adapter.TrophyGuideAdapter;
import robustgametools.model.BaseActivity;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;

public class GuideActivity extends BaseActivity {

    @InjectView(R.id.drawer) DrawerLayout mDrawer;
    @InjectView(R.id.drawer_menu) ListView mDrawerMenu;

    private ActionBarDrawerToggle mDrawerToggle;
    private TrophyGuide mTrophyGuide;
    private int mCurrentPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        getTrophyGuideInfo();

        if (savedInstanceState == null) {
            Fragment frag = new GuideFragment();
            Bundle bundle = new Bundle();
            bundle.putString("rawGuide", mTrophyGuide.getRoadmap());
            frag.setArguments(bundle);

            getFragmentManager().beginTransaction()
                    .add(R.id.container, frag)
                    .commit();
        }

        setTitle(mTrophyGuide.getTitle());
        initDrawer();
    }

    private void getTrophyGuideInfo() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("guideInfo");
        mTrophyGuide = new Gson().fromJson(json, TrophyGuide.class);
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
        );
        mDrawer.setDrawerListener(mDrawerToggle);
        final TrophyGuideAdapter adapter = new TrophyGuideAdapter(this, mTrophyGuide);
        mDrawerMenu.setAdapter(adapter);
        mDrawerMenu.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerMenu.setItemChecked(position, true);
                adapter.setSelection(position);
                onNavigationItemSelected(position);
                mCurrentPosition = position;
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
            super.onBackPressed();
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
        getMenuInflater().inflate(R.menu.trophy_guide, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_update_trophy_info) {
            return true;
        } else if (id == R.id.action_exit_guide) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
