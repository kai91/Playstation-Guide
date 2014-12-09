package robustgametools.signin;

import android.content.Intent;
import android.os.Bundle;

import robustgametools.model.BaseActivity;
import robustgametools.playstation.HomeActivity;
import robustgametools.playstation_guide.R;
import robustgametools.util.Log;
import robustgametools.util.Storage;


public class SignInActivity extends BaseActivity implements SignInFragment.onSignInListener {

    private static SignInFragment mSignInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Storage storage = Storage.getInstance(this);
        if (storage.userDataExists()) {
            alreadySignedIn();
            return;
        }

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
        Log.i("SignInActivity: onSignInSuccess");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("RECENTLY_UPDATED", true);
        startActivity(intent);
        finish();
    }

    public void alreadySignedIn() {
        Log.i("SignInActivity: Already signed in");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("RECENTLY_UPDATED", false);
        startActivity(intent);
        finish();
    }
}
