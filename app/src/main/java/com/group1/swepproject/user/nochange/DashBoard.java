package com.group1.swepproject.user.nochange;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

import com.group1.swepproject.user.nochange.Fragments.DashBoardFragment;
import com.group1.swepproject.user.nochange.Fragments.DebtorsAndCreditorsFragment;

public class DashBoard extends AppCompatActivity implements
        DashBoardFragment.OnFragmentInteractionListener,
        BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView buttomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        buttomNavigation = findViewById(R.id.nav_view);
        buttomNavigation.setOnNavigationItemSelectedListener(this);
        buttomNavigation.setSelectedItemId(R.id.nav_dashbooard);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_dashbooard:
                loadFragment(new DashBoardFragment());
                return true;
            case R.id.nav_myChange:
                loadFragment(new DebtorsAndCreditorsFragment());
                return true;
        }
        return false;
    }
    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

