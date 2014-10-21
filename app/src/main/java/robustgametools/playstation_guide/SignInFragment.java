package robustgametools.playstation_guide;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnEditorAction;


public class SignInFragment extends Fragment {
    @InjectView(R.id.username) EditText mUsername;

    private onSignInListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in_form, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @OnEditorAction(R.id.username)
    public boolean signIn() {
        Toast.makeText(getActivity(), mUsername.getText().toString(), Toast.LENGTH_LONG).show();
        return true;
    }

    public void onSignInButtonPressed(String username) {
        //TODO Implement login behaviour
        if (mListener != null) {
            mListener.signIn(username);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onSignInListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onSignInListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface onSignInListener {
        public void signIn(String username);
    }

}
