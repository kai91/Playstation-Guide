package robustgametools.playstation;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import robustgametools.model.BaseActivity;
import robustgametools.playstation_guide.R;

public class HomeActivity extends BaseActivity implements HomeFragment.HomeFragmentListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new HomeFragment())
                    .commit();
        }

        initDrawer();
    }

    private void initDrawer() {
        setActionBarIcon(R.drawable.ic_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
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
                drawer.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onGameClicked(Uri uri) {

    }
}
