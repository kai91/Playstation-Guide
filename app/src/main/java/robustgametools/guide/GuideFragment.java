package robustgametools.guide;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import robustgametools.model.TrophyGuide;
import robustgametools.playstation_guide.R;

public class GuideFragment extends Fragment {

    private TrophyGuide mTrophyGuide;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle args = getArguments();
        String json = args.getString("guideInfo");
        mTrophyGuide = new Gson().fromJson(json, TrophyGuide.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.inject(this, view);
        getActivity().setTitle(mTrophyGuide.getTitle());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.guide, menu);
    }
}
