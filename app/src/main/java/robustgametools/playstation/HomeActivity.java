package robustgametools.playstation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.Locale;

import robustgametools.model.BaseActivity;
import robustgametools.playstation_guide.R;

public class HomeActivity extends BaseActivity {
    private DrawerLayout drawer;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, PlaceholderFragment.newInstance(1))
                    .commit();
            Toast.makeText(this, "ccmmiting fragment", Toast.LENGTH_LONG).show();
        }*/

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return new PlaceholderFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private ViewPager mViewPager;


        public PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sign_in_form, container, false);
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            /*SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            // Bind the tabs to the ViewPager
            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
            tabs.setViewPager(mViewPager);*/
            // When swiping between different sections, select the corresponding
            // tab. We can also use ActionBar.Tab#select() to do this if we have
            // a reference to the Tab.
           // Toast.makeText(getActivity(), "works?", Toast.LENGTH_LONG).show();
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }


    }

}
