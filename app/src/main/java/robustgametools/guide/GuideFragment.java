package robustgametools.guide;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.model.Guide;
import robustgametools.model.TrophyLevel;
import robustgametools.playstation_guide.R;

public class GuideFragment extends Fragment {

    private GuideFactory mFormatter;
    private String mRawGuide, mTitle;
    private Guide mGuide;
    private boolean mIsOffline;

    @InjectView(R.id.trophy_detail) CardView mTrophyDetail;
    @InjectView(R.id.container) LinearLayout mContainer;
    @InjectView(R.id.trophy_icon) ImageView mTrophyIcon;
    @InjectView(R.id.trophy_type) ImageView mTrophyType;
    @InjectView(R.id.trophy_title) TextView mTrophyTitle;
    @InjectView(R.id.description) TextView mDescription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args.getString("rawGuide") != null) {
            mRawGuide = args.getString("rawGuide");
        } else {
            String guide = args.getString("guide");
            mGuide = new Gson().fromJson(guide, Guide.class);
            mRawGuide = mGuide.guide;
        }
        mIsOffline = args.getBoolean("isOffline");
        mTitle = args.getString("title");
        mFormatter = GuideFactory.getInstance(getActivity(), mTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        ButterKnife.inject(this, view);
        mFormatter.isOffline(mIsOffline).format(mRawGuide).into(mContainer);
        if (mGuide != null) {
            displayTrophyInfo();
        }
        return view;
    }

    private void displayTrophyInfo() {
        Picasso.with(getActivity()).load(mGuide.url).into(mTrophyIcon);
        mTrophyTitle.setText(mGuide.title);
        mDescription.setText(mGuide.description);
        TrophyLevel trophyLevel = TrophyLevel.valueOf(mGuide.type);
        int drawable = 0;
        switch (trophyLevel) {
            case BRONZE:
                drawable = R.drawable.bronze;
                break;
            case SILVER:
                drawable = R.drawable.silver;
                break;
            case GOLD:
                drawable = R.drawable.gold;
                break;
            case PLATINUM:
                drawable = R.drawable.platinum;
        }
        Picasso.with(getActivity()).load(drawable).into(mTrophyType);
        mTrophyDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.guide, menu);
    }
}
