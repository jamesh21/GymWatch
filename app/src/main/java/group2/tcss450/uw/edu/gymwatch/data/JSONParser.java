package group2.tcss450.uw.edu.gymwatch.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class parses a JSON file and gives a list of GymItems as a result.
 */

public class JSONParser {
    private JSONObject json;
    GymItem gym;
    //Tags for reading each result's data
    private static final String TAG_RESULTS = "results";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "place_id";
    private static final String TAG_ICON = "icon";
    private static final String TAG_OPEN_HOURS = "opening_hours";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_RATINGS = "rating";
    private static final String TAG_ADDRESS = "vicinity";

    /** The URL for getting google images. */
    private static String PARTIAL_URL = "https://maps.googleapis.com/maps/api/place" +
            "/photo?maxwidth=400&photoreference=";

    /** API KEY for this app. */
    private static String API_KEY = "&key=AIzaSyC32qpLF5AVQGXBEq0iCGkCHHAI9V8Eb1w";

    /** This URL is used for when there are no pictures available for that gym. */
    private static final String NO_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/" +
            "thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png";

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
        //Can change to 20 for more results, but ten seems ok for now.
        try {
            JSONArray places = json.getJSONArray(TAG_RESULTS);
            // Going through each gym result and buidling a gym item object
            for (int i = 0; i < places.length(); i++) {
                JSONObject object = places.getJSONObject(i);
                String name = object.getString(TAG_NAME);
                //String hours = object.getString(TAG_OPEN_HOURS);
                String rating = object.getString(TAG_RATINGS);
                String address = object.getString(TAG_ADDRESS);
                String image;
                if (object.has(TAG_PHOTOS)) {
                    JSONArray photo = object.getJSONArray(TAG_PHOTOS);
                    JSONObject images = photo.getJSONObject(0);
                    String photoReference = images.getString("photo_reference");
                    image = PARTIAL_URL + photoReference + API_KEY;
                } else { // if no image are available
                    image = NO_IMAGE;
                }
                GymItem gym = new GymItem(name, rating, address, "50", image);
                gymList.add(gym);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gymList;
    }

}

