package group2.tcss450.uw.edu.gymwatch.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.LoginSavePreference;
import group2.tcss450.uw.edu.gymwatch.fragments.MyGymsFragment;
import group2.tcss450.uw.edu.gymwatch.fragments.SearchResultsFragment;
import group2.tcss450.uw.edu.gymwatch.fragments.SettingsFragment;

/**
 * This class represents the container for the main functionality fragments of this app.
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** Reference to the search view. */
    private SearchView mSearch;
    /** Reference to the title. */
    private TextView mTitle;

    private String mUsername;

    //private SharedPreferences mPrefs;

    //private int settingsSpinnerPos;
    @Override
    /**
     * Setup the home activity, initialize the instance field, and place the MyGymsFragment
     * into the container.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        SharedPreferences prefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
//        settingsSpinnerPos = prefs.getInt(getString(R.string.POSITION), 0);

        //Getting a reference to the search view and adding listeners so the title will disappear
        mSearch = (SearchView) findViewById(R.id.search);
        mTitle = (TextView) findViewById(R.id.fragment_title);
        mUsername = getIntent().getStringExtra("username");
        setupSearchView();

        //Setting up the my gyms recycle view
        if(savedInstanceState == null) {
            if (findViewById(R.id.content_home) != null) {
                Bundle bundle = new Bundle();
                bundle.putString("username", mUsername);
                MyGymsFragment gymsFragment = new MyGymsFragment();
                gymsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_home, gymsFragment)
                        .commit();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            mTitle.setText(R.string.my_gyms);
            mSearch.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method is used to setup the listeners for the search view.
     */
    private void setupSearchView() {
        mSearch.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitle.setText("");
            }
        });
        mSearch.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTitle.setText(R.string.my_gyms);
                return false;
            }
        });
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            /**
             * Listens to the submit button for the search, this method will handle the transition
             * to the results page.
             */
            public boolean onQueryTextSubmit(String query) {
                if (mSearch.getQuery().length() != 0) {
                    mTitle.setText("Results");
                    mSearch.setIconified(true);
                    SearchResultsFragment searchResults = new SearchResultsFragment();
                    //Place the query into a bundle to be passed to the results fragment
                    Bundle args = new Bundle();
                    args.putSerializable("query", query);
                    searchResults.setArguments(args);
                    FragmentTransaction transaction = getSupportFragmentManager().
                            beginTransaction().
                            replace(R.id.content_home, searchResults).
                            addToBackStack(null);
                    transaction.commit();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    /**
     * Handles what happens when each item in the navigation bar is clicked.
     */
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // This part is for switching the fragments when the navigation bar is used.
        if (id == R.id.nav_gyms) {
            mSearch.setVisibility(View.VISIBLE);
            mSearch.setIconified(true);
            mTitle.setText(R.string.my_gyms);
            Bundle bundle = new Bundle();
            bundle.putString("username", mUsername);
            MyGymsFragment gymsFragment = new MyGymsFragment();
            gymsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_home, gymsFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_setting) {
            SharedPreferences pref = getSharedPreferences(getString(R.string.SHARED_PREFS),
                                                Context.MODE_PRIVATE);
            int position = pref.getInt(getString(R.string.POSITION), 0);
            boolean notification = pref.getBoolean(getString(R.string.NOTIFICATION), false);
            String startTime = pref.getString(getString(R.string.START_TIME), "12:00");
            String endTime = pref.getString(getString(R.string.END_TIME), "24:00");

            mTitle.setText(R.string.action_settings);
            mSearch.setVisibility(View.INVISIBLE);
            SettingsFragment settings = new SettingsFragment();
            Bundle args = new Bundle();
            args.putInt(getString(R.string.POSITION), position);
            args.putBoolean(getString(R.string.NOTIFICATION), notification);
            args.putString(getString(R.string.START_TIME), startTime);
            args.putString(getString(R.string.END_TIME), endTime);
            settings.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home, settings).addToBackStack(null);
            transaction.commit();
        } else if(id == R.id.nav_signout) {
            Intent signOut = new Intent(this, LoginActivity.class);
            signOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            LoginSavePreference.setUser(this, "");
            startActivity(signOut);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
