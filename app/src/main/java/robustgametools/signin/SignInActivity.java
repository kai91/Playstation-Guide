package robustgametools.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import robustgametools.model.BaseActivity;
import robustgametools.playstation.HomeActivity;
import robustgametools.playstation_guide.R;


public class SignInActivity extends BaseActivity implements SignInFragment.onSignInListener {

    private static SignInFragment mSignInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInFragment = new SignInFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mSignInFragment)
                    .commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void onSignInSuccess() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
