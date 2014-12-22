package robustgametools.guide;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import robustgametools.playstation_guide.R;

public class GuideHomeFragment extends Fragment {

    @InjectView(R.id.tabs) PagerSlidingTabStrip mTabStrip;
    @InjectView(R.id.pager) ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_guide_home, container, false);
        ButterKnife.inject(this, view);

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(sectionsPagerAdapter);

        // Bind the tabs to the ViewPager
        mTabStrip.setViewPager(mViewPager);

        // When user switched to MyGuideFragment, refreshDownloadedList the list in MyGuideFragment
        // in case user downloaded new guide
        mTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(
                    int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    ((MyGuideFragment)sectionsPagerAdapter.getItem(1)).refreshDownloadedList();
                } else if (position == 0) {
                    ((GuideListFragment)sectionsPagerAdapter.getItem(0)).refreshDownloadedList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        return view;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private MyGuideFragment myGuideFragment;
        private GuideListFragment guideListFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the myGuideFragment for the given page.
            if (position == 0) {
                if (guideListFragment == null) {
                    guideListFragment = new GuideListFragment();
                }
                return guideListFragment;
            }
            else {
                if (myGuideFragment == null) {
                    myGuideFragment = new MyGuideFragment();
                }
                return myGuideFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

}
