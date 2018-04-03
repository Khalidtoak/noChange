package com.group1.swepproject.user.nochange;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.group1.swepproject.user.nochange.Fragments.ChangeRecord;
import com.group1.swepproject.user.nochange.Fragments.DebtorsRecord;

import java.util.ArrayList;
import java.util.List;

public class DebtorsAndCreditorsActivity extends AppCompatActivity {
    //this Activity creates a view pager with 2 fragments for displaying change  or debt
    //View pager gives us a page like feel where you can swipe between the pages of an activity
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtors_and_creditors);
        //hide the action bar
        getSupportActionBar().hide();
        //find the view pager in the xml
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        //set up tablayout together with viewPager
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        //initialize the Viewpager adapter with the support fragment manager as the parameter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //add our change and debtors fragments
        adapter.addFragment(new ChangeRecord(), "Change");
        adapter.addFragment(new DebtorsRecord(), "Debtors");
        //set the view pager adapter
        viewPager.setAdapter(adapter);
    }
    //here is our viewPager adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {
        //get an array list of fragments that we will add to our view pager
        private final List<Fragment> mFragmentList = new ArrayList<>();
        //also an array list oof strings for the titles of each of the fragments
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        //get Fragment position from the list of fragments
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        //get the size of the fragment list array
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        //get the title of each page by adding the fragment list and the titles
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * Normally, calling setDisplayHomeAsUpEnabled(true) (we do so in onCreate here) as well as
         * declaring the parent activity in the AndroidManifest is all that is required to get the
         * up button working properly. However, in this case, we want to navigate to the previous
         * screen the user came from when the up button was clicked, rather than a single
         * designated Activity in the Manifest.
         *
         * We use the up button's ID (android.R.id.home) to listen for when the up button is
         * clicked and then call onBackPressed to navigate to the previous Activity when this
         * happens.
         */
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}