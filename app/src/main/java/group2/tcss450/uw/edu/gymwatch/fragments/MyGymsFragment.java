package group2.tcss450.uw.edu.gymwatch.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.GymAdapter;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;


/**
 * This class is for the myGyms fragment which will hold gyms added by the user.
 */
public class MyGymsFragment extends Fragment {

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~xufang/getGymsFromDB.php";
    /** The URL for getting google images. */
    private static String IMAGE_URL = "https://maps.googleapis.com/maps/api/place" +
            "/photo?maxwidth=400&photoreference=";

    /** API KEY for this app. */
    private static String API_KEY = "&key=AIzaSyC32qpLF5AVQGXBEq0iCGkCHHAI9V8Eb1w";

    private static final String FAKE_DATA = "http://cssgate.insttech.washington.edu/~xufang/" +
            "fakeData.php";
    /** This URL is used for when there are no pictures available for that gym. */

    private static final String NO_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/" +
            "thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png";

    private static final String TAG_NAME = "gymname";
    private static final String TAG_IMAGE = "imageurl";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_RATING = "rating";
    private static final String TAG_PLACE_ID = "placeid";
    private static List<GymItem> mUserGyms;

    private View mView;

    @Override
    /**
     * Method initializes the recycle view and adds it to this fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String username = getArguments().getString("username");
        mView = inflater.inflate(R.layout.fragment_my_gyms, container, false);

        mUserGyms = new ArrayList<>();
        AsyncTask<String, Void, String> task = new GetGymsFromDBTask();
        task.execute(PARTIAL_URL, username);
        return mView;
    }

    /**
     * Inner AsyncTask class, handle the internet connection with the web server. Passing
     * parameters to the web server and get the response back.
     */
    private class GetGymsFromDBTask extends AsyncTask<String, Void, String> {

        /**
         * perform web connection, passing parameters and retrieve response back by POST
         * @param strings includes destination URL, and parameters we want to pass.
         * @return response is the string we get back from the web server.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 2) {
                throw new IllegalArgumentException("Two String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("username", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8");
                wr.write(data);
                wr.flush();
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

        /**
         * This method checks if the response is valid, If it is, store the response
         * in to the field mResponse.
         * @param result the response string we  get back from the server.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray json = new JSONArray(result);
                List<GymItem> userGyms = new ArrayList<>();
                System.out.println("!!!!!" + json.length());
                for (int i = 0; i < json.length(); i++) {
                    System.out.println("HELLO HELLO");
                    String str_result = new StaticWebServiceTask().execute(FAKE_DATA).get();
                    JSONObject gym = json.getJSONObject(i);
                    String gymName = gym.getString(TAG_NAME);
                    String gymAddress = gym.getString(TAG_ADDRESS);
                    String photoReference = gym.getString(TAG_IMAGE);
                    String gymImage;
                    String placeId = gym.getString(TAG_PLACE_ID);
                    if (!photoReference.isEmpty()) {
                        gymImage = IMAGE_URL + photoReference + API_KEY;
                    } else {
                        gymImage = NO_IMAGE;
                    }
                    String gymRating = gym.getString(TAG_RATING);
                    GymItem currentGym = new GymItem(gymName, gymRating, gymAddress, str_result, gymImage, placeId);
                    userGyms.add(currentGym);
                }
                System.out.println("User gyms ==== " + userGyms.size());
                RecyclerView gymRecView = (RecyclerView) mView.findViewById(R.id.gym_home_list);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                gymRecView.setLayoutManager(layoutManager);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration
                        (gymRecView.getContext(), layoutManager.getOrientation());
                gymRecView.addItemDecoration(dividerItemDecoration);
                GymAdapter gymAdapter = new GymAdapter(userGyms, getActivity());
                gymRecView.setAdapter(gymAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

    }
    /**
     * Inner AsyncTask class, handle the internet connection with the web server. Passing
     * parameters to the web server and get the response back.
     */
    private class StaticWebServiceTask extends AsyncTask<String, Void, String> {

        /**
         * Perform web connection, passing parameters and retrieve response back by POST
         * @param strings includes destination URL, and parameters we want to pass.
         * @return response is the string we get back from the web server.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 1) {
                throw new IllegalArgumentException("One String arguments required.");
            }
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url);
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

    }

}
