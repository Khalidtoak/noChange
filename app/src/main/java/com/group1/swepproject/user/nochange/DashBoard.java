package com.group1.swepproject.user.nochange;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

