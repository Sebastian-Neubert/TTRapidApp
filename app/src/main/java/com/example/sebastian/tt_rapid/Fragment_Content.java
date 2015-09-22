package com.example.sebastian.tt_rapid;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 08.09.2015.
 */
public class Fragment_Content extends Fragment {

    Toolbar toolbar_fragment;
    DrawerLayout drawerLayout_fragment;
    ActionBarDrawerToggle drawerToggle_fragment;
    NavigationView navigationView_fragment;
    AppBarLayout appBarLayout;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    int currentID;
    private Activity_Home myContext;


    @Override
    public void onAttach(Activity activity) {
        myContext = (Activity_Home) activity;
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Resume", "Resume");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_frag_tabview, container, false);

        Bundle args = getArguments();
        currentID = args.getInt("mannschaftID", 0);
        // adapter einfügen und in irgendeiner variablen speichern

        drawerLayout_fragment = (DrawerLayout) view.findViewById(R.id.fragment_drawerLayout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.fragment_app_bar_layout);
        drawerToggle_fragment = new ActionBarDrawerToggle(getActivity(), drawerLayout_fragment, toolbar_fragment, R.string.drawer_open, R.string.drawer_close);
        navigationView_fragment = (NavigationView) getActivity().findViewById(R.id.nav_view);
        drawerToggle_fragment.syncState();

        mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mAdapter = new YourPagerAdapter(myContext.getSupportFragmentManager());
        mAdapter.setMannschaftsID(currentID);
        mPager = (ViewPager) view.findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);
        //Link the Tab Layout with the Pager Adapter
        mTabLayout.setTabsFromPagerAdapter(mAdapter);

        //link View Pager object and The Tab Layout
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        return view;
    }


}


class YourPagerAdapter extends FragmentStatePagerAdapter {

    public YourPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private int mid;

    public void setMannschaftsID(int id) {
        mid = id;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        android.support.v4.app.Fragment dummyFragment = new android.support.v4.app.Fragment();
        if (position == 0) {
            Fragment_LastGames myFragment = Fragment_LastGames.newInstance(mid);
            return myFragment;
        } else if (position == 1) {
            Fragment_Table myFragment = Fragment_Table.newInstance(mid);
            return myFragment;
        }
        return dummyFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = "";
        if (position == 0) {
            title = "Spielverlauf";
        } else if (position == 1) {
            title = "Tabelle";
        }
        return title;
    }
}
