package group2.tcss450.uw.edu.gymwatch.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.activities.GymDetailActivity;
import group2.tcss450.uw.edu.gymwatch.data.GymAdapter;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;
import group2.tcss450.uw.edu.gymwatch.data.ItemClickSupport;
import group2.tcss450.uw.edu.gymwatch.data.JSONParser;

/**
 * This Class represents the search results fragment of the app, it will display all the gyms
 * matched the search.
 */
public class SearchResultsFragment extends Fragment {

    /** This is used for the google place api. */
    private static String PARTIAL_URL;
    private static String final_search;
    double latitude = 0.0, longitude = 0.0;
    private static final int location_permission_request_code = 1;
    private LocationManager lm;
    private LocationListener locationListener;
    private ArrayList<GymItem> results = new ArrayList<>();
    private String query;
    Location netLoc = null, gpsLoc = null, finalLoc = null;

    private View mView;

    /** Reference to the title bar. */
    private TextView mText;

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            handleLocation();
            //mText = (TextView) getActivity().findViewById(R.id.display_results);
            AsyncTask<String, Void, String> task = null;
            query = getArguments().getString("query");
            System.out.println("Original: " + query);
            String query2 = query.replaceAll("\\s", "");
            System.out.println("Changed: " + query2);
            PARTIAL_URL += query2 +
                    "&key=AIzaSyC32qpLF5AVQGXBEq0iCGkCHHAI9V8Eb1w";//Replace with API Key
            System.out.println("Key: " + PARTIAL_URL);
            task = new TestWebServiceTask();
            task.execute(PARTIAL_URL);
        }

        }
    @Override
    public void onPause() {
        super.onPause();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView title = (TextView) getActivity().findViewById(R.id.fragment_title);
        SearchView search = (SearchView) getActivity().findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setIconified(true);
        //title.setText(R.string.results_page);
        title.setText(query);
        mView = inflater.inflate(R.layout.fragment_search_results, container, false);
        return mView;
    }

    /**
     * Handles all location details.
     * Checks for location permissions, settings and sets location data and
     * search string.
     */
    private void handleLocation() {
        //We call getActivity() because Location manager needs context, and Fragments don't extend Contexts; the activity it resides in does.

        //if (getArguments() != null) {
            //Check if the user has granted us the required permissions
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Request Permission for Coarse Location
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        location_permission_request_code);
            } else {
                lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                //Location Listener helps get the latest location based on below methods.
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        finalLoc = location;
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,10, locationListener);
                //Check if the GPS is on
                boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(!gps_enabled && !network_enabled) {
                    Toast.makeText(getActivity(), "Search Requires GPS", Toast.LENGTH_SHORT).show();
                } else if (gps_enabled || network_enabled) {
                    if(gps_enabled) {
                        gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    if(network_enabled) {
                        netLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                if(gpsLoc != null && netLoc != null) {
                    if (gpsLoc.getAccuracy() > netLoc.getAccuracy())//Check which one is more accurate
                        finalLoc = netLoc;
                    else
                        finalLoc = gpsLoc;
                } else {
                    if (gpsLoc != null) {
                        finalLoc = gpsLoc;
                    } else if (netLoc != null) {
                        finalLoc = netLoc;
                    }
                }
                if(finalLoc != null) {
                    latitude = finalLoc.getLatitude();
                    longitude = finalLoc.getLongitude();
                        PARTIAL_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                                "json?location=" + latitude + "," + longitude + "&radius=16000&type=gym&rankBy=distance&name=";
                } else {
                    Toast.makeText(getActivity(), "Search requires GPS", Toast.LENGTH_SHORT).show();

                }

            }


        }
    private void populateView(final ArrayList<GymItem> results) {
        RecyclerView gymRecView = (RecyclerView) mView.findViewById(R.id.gym_rec_list);
        gymRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GymAdapter gymAdapter = new GymAdapter(results, getActivity());
        gymRecView.setAdapter(gymAdapter);
        ItemClickSupport.addTo(gymRecView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), GymDetailActivity.class);
                i.putExtra("Gym", results.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        populateView(results);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getActivity(), "Unable to get Location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * This Inner class is used to make calls to the web service to get the search results.
     */
    private class TestWebServiceTask extends AsyncTask<String, Void, String> {

        @Override
        /**
         * Method for grabbing the search results in the background.
         */
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(PARTIAL_URL);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return response;
        }

        @Override
            protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getContext(), "Search Failed", Toast.LENGTH_LONG)
                     .show();
                return;
            }
            //mText.setText(result);
            //Gives the result string to a JSONParser object which will parse the string.
            JSONParser parser = new JSONParser(result);
            results = parser.getGyms();
            RecyclerView gymRecView = (RecyclerView) mView.findViewById(R.id.gym_rec_list);
            gymRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
            GymAdapter gymAdapter = new GymAdapter(results, getActivity());
            gymRecView.setAdapter(gymAdapter);
            ItemClickSupport.addTo(gymRecView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    Intent i = new Intent(getActivity(), GymDetailActivity.class);
                    i.putExtra("Gym", results.get(position));
                    startActivity(i);
                }
            });
        }
    }
}
