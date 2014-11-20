package robustgametools.playstation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.adapter.NavigationDrawerAdapter;
import robustgametools.model.BaseActivity;
import robustgametools.playstation_guide.R;
import robustgametools.signin.SignInActivity;
import robustgametools.util.Storage;

public class HomeActivity extends BaseActivity implements HomeFragment.HomeFragmentListener {

    @InjectView(R.id.drawer) DrawerLayout mDrawer;
    @InjectView(R.id.drawer_menu) ListView mDrawerMenu;

    private ActionBarDrawerToggle mDrawerToggle;

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
        );
        mDrawer.setDrawerListener(mDrawerToggle);
        final NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(this);
        mDrawerMenu.setAdapter(adapter);
        mDrawerMenu.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerMenu.setItemChecked(position, true);
                adapter.selectItem(position);
                onNavigationItemSelected(position);
                mDrawer.closeDrawers();
            }
        });

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
    public void showGameDetail(Uri uri) {

    }

    private void onNavigationItemSelected(int position) {
        if (position == 2) {
            signOut();
        }
    }
}
