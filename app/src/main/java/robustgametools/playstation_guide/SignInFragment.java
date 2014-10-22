package robustgametools.playstation_guide;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnEditorAction;
import robustgametools.util.HttpClient;


public class SignInFragment extends Fragment {
    @InjectView(R.id.username) EditText mUsername;

    private onSignInListener mListener;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in_form, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @OnEditorAction(R.id.username)
    public boolean signInClicked() {
        String username = mUsername.getText().toString();
        showLoadingDialog();
        HttpClient.signIn(username, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                hideLoadingDialog();
                hideKeyboard();
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                hideLoadingDialog();
                Toast.makeText(getActivity(), "Error signing in: " + Integer.toString(statusCode), Toast.LENGTH_LONG).show();
            }
        });
        return true;
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
        HttpClient.cancelRequests();
    }

    private void showLoadingDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Logging in...");
        mProgressDialog.show();
    }

    private void hideLoadingDialog() {
        if (mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUsername.getWindowToken(), 0);
    }

    public interface onSignInListener {
        public void onSignInSuccess(String username);
    }

}
