package robustgametools.playstation_guide;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class SignInFragment extends Fragment {

    private onSignInListener mListener;


    public static SignInFragment newInstance() {
        return new SignInFragment();
    }
    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        //textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    public void onSignInButtonPressed(String username) {
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
