package group2.tcss450.uw.edu.gymwatch.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import group2.tcss450.uw.edu.gymwatch.fragments.MyGymsFragment;
import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.fragments.SearchResultsFragment;
import group2.tcss450.uw.edu.gymwatch.fragments.SettingsFragment;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /** Reference to the searchview. */
    private SearchView mSearch;
    /** Reference to the title. */
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Getting a reference to the search view and adding listeners so the title will disappear
        mSearch = (SearchView) findViewById(R.id.search);
        mTitle = (TextView) findViewById(R.id.fragment_title);
        setupSearchView();

        //Setting up the my gyms recycle view
        if(savedInstanceState == null) {
            if (findViewById(R.id.content_home) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_home, new MyGymsFragment())
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
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        //MenuItem searchItem = menu.findItem(R.id.search);
//        //mSearch = (SearchView) searchItem.getActionView();
//        mSearch = (SearchView)findViewById(R.id.search);
//        mSearch.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTitle.setText("");
//            }
//        });
//        mSearch.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                mTitle.setText(R.string.my_gyms);
//                return false;
//            }
//        });
//        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
            public boolean onQueryTextSubmit(String query) {
                System.out.println(mSearch.getQuery());
                if (mSearch.getQuery().length() != 0) {
                    mTitle.setText(R.string.results_page);
                    mSearch.setIconified(true);
                    SearchResultsFragment searchResults = new SearchResultsFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("query", mSearch.getQuery().toString());
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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // This part is for switching the fragments when the navagation bar is used.
        if (id == R.id.nav_gyms) {
            mSearch.setVisibility(View.VISIBLE);
            mSearch.setIconified(true);
            mTitle.setText(R.string.my_gyms);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home, new MyGymsFragment())
                    .addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_setting) {
            mTitle.setText(R.string.action_settings);
            mSearch.setVisibility(View.INVISIBLE);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home, new SettingsFragment())
                    .addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
//    public void onFragmentInteraction(int buttonId) {
//        switch(buttonId) {
//            //case R.id.
//        }
//    }
}
