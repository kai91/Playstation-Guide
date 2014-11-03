package robustgametools.playstation;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.model.Profile;
import robustgametools.playstation_guide.R;
import robustgametools.util.JsonFactory;
import robustgametools.util.Log;
import robustgametools.util.Storage;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private static Profile mProfile;

    private HomeFragmentListener mListener;

    @InjectView(R.id.profile_image)
    ImageView mProfileImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        JsonFactory jsonFactory = JsonFactory.getInstance();
        Storage storage = Storage.getInstance(getActivity());
        String data = storage.readUserData();
        Log.i(TAG, data);
        mProfile = jsonFactory.parseUserProfile(data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);
        initHeader();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.home, menu);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGameClicked(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (HomeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initHeader() {
        Picasso.with(getActivity()).load(mProfile.getAvatarUrl())
                .into(mProfileImage);
    }

    public interface HomeFragmentListener {
        public void onGameClicked(Uri uri);
    }

}
