package com.example.teambeans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public DrawerLayout mDrawer;
    private NavigationView navigationViewDrawer;
    private MenuItem getmenuItem;
    private ActionBarDrawerToggle drawerToggle;

    public void ChangeLock() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        drawerToggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mDrawer = findViewById(R.id.drawer_layout);

        // Navigation Drawer icon
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(false);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the Action Bar Toggle
        mDrawer.addDrawerListener(drawerToggle);

        navigationViewDrawer = (NavigationView) findViewById(R.id.navigationView);

        setupDrawerContent(navigationViewDrawer);

        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new Login());
        fragmentTransaction.commit();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    // Called when activity start-up is complete after onStart()
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                selectDrawerItem(menuItem);
                getmenuItem = menuItem;
                return true;
            }
        });
    }


    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Login login = new Login();
        String url = "http://coms-309-rp-09.cs.iastate.edu:8080/users/" + login.user + "/role";
        //connect and get role from backend
        connection(this, url, menuItem, "drawer");
        // Open fragment from item selected in drawer
    }

    private void openDrawer(MenuItem menuItem, String role) {
        System.out.println("role[0] is" + role);
        Fragment fragment = null;
        Class fragmentClass;
        Bundle b = new Bundle();
        int id = menuItem.getItemId();

        //go to different user profile based on role
        if (id == R.id.dashboard) {
            fragmentClass = Dashboard.class;
        } else if (id == R.id.myProfile && role.equals("STUDENT")) {
            fragmentClass = UserProfile.class;
        } else if (id == R.id.myProfile && role.equals("TA")) {
            fragmentClass = UserProfileTA.class;
        } else if (id == R.id.myProfile && role.equals("PROFESSOR")) {
            fragmentClass = UserProfileProfessor.class;
        } else if(id == R.id.inbox) {
            fragmentClass = Inbox.class;
        } else if (id == R.id.chatRoom) {
            fragmentClass = ChatRoom.class;
        }
        else {
            fragmentClass = MainActivity.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Replace existing fragment, if any, with selected fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);

        // Set title
        setTitle(menuItem.getTitle());

        // Close the Navigation Drawer
        mDrawer.closeDrawers();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void connection(Context context, String url, MenuItem menuItem, final String type) {
        //made it public just in case other places need to get role or get method
        //to use get just change the url, maybe ignore the menuItem, set the type to anything in order to
        //do anything you want after getting a response
        final String[] role = {""};
        final RequestQueue requestQueue = Volley.newRequestQueue(context);
        final StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           // System.out.println("RoleResponse" + response);
                            role[0] = response;
                           // System.out.println("ARRAY{0]" + role[0]);
                            //after getting , set the profile depends on role
                            if(type.equals("drawer")) {
                                openDrawer(getmenuItem, role[0]);
                            }
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR " + error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}