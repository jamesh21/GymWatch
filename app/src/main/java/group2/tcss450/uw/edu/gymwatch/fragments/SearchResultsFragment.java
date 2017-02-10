package group2.tcss450.uw.edu.gymwatch.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group2.tcss450.uw.edu.gymwatch.R;

/**
 * This Class represents the search results fragment of the app, it will display all the gyms
 * matched the search.
 */
public class SearchResultsFragment extends Fragment {
    private static String PARTIAL_URL
            = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
            "json?location=-33.8670522,151.1957362&radius=500&type=restaurant&keyword=";
    private TextView mText;

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mText = (TextView) getActivity().findViewById(R.id.display_results);
            AsyncTask<String, Void, String> task = null;
            PARTIAL_URL += getArguments().getString("query") +
                    "&key=AIzaSyAsOBJx3muOOytqQo4k5WmcJr92xGvl5Sw%5Cn";//Replace with API Key
            task = new TestWebServiceTask();
            task.execute(PARTIAL_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView title = (TextView) getActivity().findViewById(R.id.fragment_title);
        SearchView search = (SearchView) getActivity().findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setIconified(true);
        title.setText(R.string.results_page);
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    private class TestWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
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
                //Toast.makeText(SearchResultsFragment.this, result, Toast.LENGTH_LONG)
                //     .show();
                return;
            }
            mText.setText(result);
        }
    }
}
