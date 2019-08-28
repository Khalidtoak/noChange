package com.group1.swepproject.user.nochange.Fragments;


import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.group1.swepproject.user.nochange.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebtorsAndCreditorsFragment extends Fragment {
    //this Activity creates a view pager with 2 fragments for displaying change  or debt
    //View pager gives us a page like feel where you can swipe between the pages of an activity
    private Toolbar toolbar;


    public DebtorsAndCreditorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_debtors_and_creditors, container, false);
        //find the view pager in the xml
        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        //set up tablayout together with viewPager
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }
    private void setupViewPager(ViewPager viewPager) {
        //initialize the Viewpager adapter with the support fragment manager as the parameter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
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

        ViewPagerAdapter(FragmentManager fm) {
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
        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

}
