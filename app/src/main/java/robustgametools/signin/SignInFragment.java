package robustgametools.signin;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import butterknife.OnClick;
import butterknife.OnEditorAction;
import robustgametools.playstation_guide.R;
import robustgametools.util.HttpClient;
import robustgametools.util.Storage;


public class SignInFragment extends Fragment {
    @InjectView(R.id.username) EditText mUsername;

    private onSignInListener mListener;
    private ProgressDialog mProgressDialog;
    private Storage mStorage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sign_in_form, container, false);
        ButterKnife.inject(this, rootView);
        mStorage = Storage.getInstance(getActivity());
        return rootView;
    }

    @OnClick(R.id.sign_in)
    public void signInButtonClicked() {
        signInClicked();
    }


    @OnEditorAction(R.id.username)
    public boolean signInClicked() {

        String username = mUsername.getText().toString();
        showLoadingDialog();
        HttpClient.signIn(username, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {

                String response = new String(responseBody);
                persistUserData(response);
                getRecentlyPlayedGames();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                hideLoadingDialog();
                Toast.makeText(getActivity(), "Error signing in: " +
                        Integer.toString(statusCode), Toast.LENGTH_LONG).show();
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

    private void getRecentlyPlayedGames() {
        HttpClient.getRecentlyPlayedGames(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                persistRecentGames(response);
                successfullyLoggedIn();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error retrieving information: " +
                        Integer.toString(statusCode), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called upon successfully logged in. Clean up
     * and finally pass on to the activity to handle
     */
    private void successfullyLoggedIn() {
        hideKeyboard();
        mListener.onSignInSuccess();
    }

    /**
     * Save player's data to the internal storage
     * @param data
     */
    private void persistUserData(String data) {
        mStorage.persistUserData(data);
    }

    private void persistRecentGames(String data) {
        mStorage.persistRecentGames(data);
    }

    private void showLoadingDialog() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Logging in...");
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // Cancels all connection in case user wants to cancel it
                HttpClient.cancelRequests();
            }
        });
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
        public void onSignInSuccess();
    }

}
