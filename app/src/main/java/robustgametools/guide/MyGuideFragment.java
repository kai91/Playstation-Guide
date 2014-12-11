package robustgametools.guide;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.adapter.DownloadedGuideListAdapter;
import robustgametools.playstation_guide.R;
import robustgametools.util.Storage;

public class MyGuideFragment extends Fragment {

    @InjectView(R.id.downloaded_guides) ListView mDownloadedList;
    @InjectView(R.id.empty) TextView mEmptyMessage;

    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mDownloadedTitle;
    private DownloadedGuideListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_guide, container, false);
        ButterKnife.inject(this, view);
        mDownloadedTitle = new ArrayList<>();
        refreshDownloadedList();
        showDownloadedGuides();
        return view;
    }

    public void refreshDownloadedList() {
        Storage storage = Storage.getInstance(getActivity());
        ArrayList<String> newList = storage.getGuideList();
        mDownloadedTitle.clear();
        mDownloadedTitle.addAll(newList);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showDownloadedGuides() {
        mAdapter = new DownloadedGuideListAdapter(getActivity(), mDownloadedTitle);
        mDownloadedList.setAdapter(mAdapter);
        mDownloadedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onDownloadedGuideSelected(mDownloadedTitle.get(position));
            }
        });
        if (!mDownloadedTitle.isEmpty()) {
            mEmptyMessage.setVisibility(View.GONE);
        }
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
