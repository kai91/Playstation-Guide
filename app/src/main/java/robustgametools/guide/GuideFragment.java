package robustgametools.guide;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.playstation_guide.R;

public class GuideFragment extends Fragment {

    private GuideFormatter mFormatter;
    private String mRawGuide;
    @InjectView(R.id.container) LinearLayout mContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFormatter = GuideFormatter.getInstance(getActivity());
        Bundle args = getArguments();
        mRawGuide = args.getString("rawGuide");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.inject(this, view);
        mFormatter.format(mRawGuide).into(mContainer);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.guide, menu);
    }
}
