package robustgametools.playstation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import robustgametools.model.BaseActivity;
import robustgametools.playstation_guide.R;
import robustgametools.signin.SignInActivity;
import robustgametools.util.Storage;

public class HomeActivity extends BaseActivity implements HomeFragment.HomeFragmentListener {
    @InjectView(R.id.drawer) DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }

        initDrawer();
    }

    private void initDrawer() {
        setActionBarIcon(R.drawable.ic_drawer);
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    }

    @OnClick(R.id.signOut)
    public void signOut() {
        Storage storage = Storage.getInstance(this);
        storage.deleteUserData();
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
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGameClicked(Uri uri) {

    }
}
