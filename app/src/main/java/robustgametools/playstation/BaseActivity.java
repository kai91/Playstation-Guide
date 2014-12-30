package robustgametools.playstation;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import robustgametools.playstation_guide.R;

/**
 * BaseActivity class to provide basic
 * functionalities and template to all
 * Activity.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    /**
     * To be implemented by all subclass to provide generic
     * layout inflation
     * @return
     */
    protected abstract int getLayoutResource();

    protected void setActionBarIcon(int iconRes) {
        mToolbar.setNavigationIcon(iconRes);
    }

    protected void setDisplayHomeAsUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




}
