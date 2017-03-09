package group2.tcss450.uw.edu.gymwatch.data;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * This class parses a JSON file and gives a list of GymItems as a result.
 */

public class JSONParser {
    private JSONObject json;
    GymItem gym;
    //Tags for reading each result's data
    private static final String TAG_RESULTS = "results";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_RATINGS = "rating";
    private static final String TAG_ADDRESS = "vicinity";
    private static final String TAG_PLACE_ID = "place_id";
    private static final String TAG_OPEN = "open_now";
    /** The URL for getting google images. */
    private static String PARTIAL_URL = "https://maps.googleapis.com/maps/api/place" +
            "/photo?maxwidth=400&photoreference=";

    /** API KEY for this app. */
    private static String API_KEY = "&key=AIzaSyC32qpLF5AVQGXBEq0iCGkCHHAI9V8Eb1w";

    /** This URL is used for when there are no pictures available for that gym. */
    private static final String NO_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/" +
            "thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png";

    private static final String FAKE_DATA = "http://cssgate.insttech.washington.edu/~xufang/fakeData.php";

    private String mResponse;
    /**
     * Constructor
     *
     * @param jsonResult the result String from the search query
     */
    public JSONParser(String jsonResult) {
        try {
            json = new JSONObject(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the JSON Object into an ArrayList of GymItems
     *
     * @return gymList a list of GymItem objects with gym info.
     */
    public ArrayList<GymItem> getGyms() {
        ArrayList<GymItem> gymList = new ArrayList<>();
        AsyncTask<String, Void, String> task = null;
        //Can change to 20 for more results, but ten seems ok for now.
        try {
            JSONArray places = json.getJSONArray(TAG_RESULTS);
            // Going through each gym result and buidling a gym item object
            for (int i = 0; i < places.length(); i++) {
                String str_result= new StaticWebServiceTask().execute(FAKE_DATA).get();
                JSONObject object = places.getJSONObject(i);
                String rating = "No Rating";
                if(object.has(TAG_RATINGS)) {
                    rating = object.getString(TAG_RATINGS);
                }
                String name = object.getString(TAG_NAME);
                String address = object.getString(TAG_ADDRESS);
                String placeId = object.getString(TAG_PLACE_ID);
                String image;
                if (object.has(TAG_PHOTOS)) {
                    JSONArray photo = object.getJSONArray(TAG_PHOTOS);
                    JSONObject images = photo.getJSONObject(0);
                    String photoReference = images.getString("photo_reference");
                    image = PARTIAL_URL + photoReference + API_KEY;
                } else { // if no image are available
                    image = NO_IMAGE;
                }
                GymItem gym = new GymItem(name, rating, address, str_result, image, placeId);
                gymList.add(gym);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return gymList;
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

