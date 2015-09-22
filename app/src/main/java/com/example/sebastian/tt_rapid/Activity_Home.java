package com.example.sebastian.tt_rapid;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Activity_Home extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    TextView textView;
    Fragment curfrag = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_home);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.clear();
        String[] mannschaften = getResources().getStringArray(R.array.Mannschaften);
        // das selbe wie die Zeile drüber:
        //      String[] mannschaften = getResources().getStringArray(getResources().getIdentifier("Mannschaften", "array", this.getPackageName()));
        String cgroup = "";
        SubMenu subMenu = null;
        int cgroupID = 0, count = 0;
        for (String mannschaft : mannschaften) {
            String[] mannschaftDetail = getResources().getStringArray(getResources().getIdentifier(mannschaft, "array", this.getPackageName()));
            int itemId = Integer.parseInt(mannschaftDetail[7]);
            if (itemId == 0) {
                menu.add(cgroupID, itemId, count, mannschaftDetail[1]);
            } else {
                if (!cgroup.equals(mannschaftDetail[6])) {
                    cgroup = mannschaftDetail[6];
                    subMenu = menu.addSubMenu(cgroup);
                    cgroupID++;
                }

                subMenu.add(cgroupID, itemId, count, mannschaftDetail[1]).setIcon(getResources().getIdentifier(mannschaftDetail[5], "drawable", this.getPackageName()));
            }
            count++;

            // FCK YEEEEEES, IT WORKS!!!!
            for (int i = 0, counter = navigationView.getChildCount(); i < counter; i++) {
                final View child = navigationView.getChildAt(i);
                if (child != null && child instanceof ListView) {
                    final ListView menuView = (ListView) child;
                    final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                    final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                    wrapped.notifyDataSetChanged();
                }
            }

        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                loadFragment(itemId);

                return true;
            }
        });

        loadFragment(0);




    }

    public void loadFragment(int itemId) {
        Fragment fragment = null;
        if (itemId == 0) {
            fragment = new Fragment_Overview();
        } else {
            fragment = new Fragment_Content();
        }
        Bundle args = new Bundle();
        args.putInt("mannschaftID", itemId);
        fragment.setArguments(args);
        //getSupportActionBar().hide();


        android.app.FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (curfrag != null) {
            try {
                transaction.remove(curfrag);
            } catch (Exception e) {

            }
        }

        transaction.add(R.id.viewGroup_Root_Fragment, fragment, "frag_cont");
        curfrag = fragment;
        transaction.commit();

        String[] mannschaften = getResources().getStringArray(R.array.Mannschaften);

        String title="";

        for (String mannschaft : mannschaften) {
            String[] mannschaftDetail = getResources().getStringArray(getResources().getIdentifier(mannschaft, "array", getPackageName()));
            if (String.valueOf(itemId).equals(mannschaftDetail[7])) {
                title = mannschaftDetail[1];
            }
        }

        getSupportActionBar().setTitle(title);
        //toolbar set text (Mannschaft)
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(new Configuration());
    }
}

