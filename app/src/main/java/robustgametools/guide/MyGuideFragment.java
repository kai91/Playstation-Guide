package robustgametools.guide;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import robustgametools.playstation_guide.R;

public class MyGuideFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_guide, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    public void refresh() {
        Toast.makeText(getActivity(), "refresh", Toast.LENGTH_LONG).show();
    }

    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onDownloadedGuideSelected(name);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
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

    public interface OnFragmentInteractionListener {
        public void onDownloadedGuideSelected(String name);
    }

}
