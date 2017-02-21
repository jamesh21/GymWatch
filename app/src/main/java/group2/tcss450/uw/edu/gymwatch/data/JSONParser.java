package group2.tcss450.uw.edu.gymwatch.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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


    /**
     * Constructor
     * @param jsonResult the result String from the search query
     */
    public JSONParser (String jsonResult) {
        try {
            json = new JSONObject(jsonResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the JSON Object into an ArrayList of GymItems
     * @return gymList a list of GymItem objects with gym info.
     */
    public ArrayList<String> getGyms() {
        ArrayList<String> gymList = new ArrayList<String>();
        //Can change to 20 for more results, but ten seems ok for now.
        try {
            JSONArray places = json.getJSONArray(TAG_RESULTS);
            for(int i = 0; i < places.length(); i++) {
                JSONObject object = places.getJSONObject(i);
                gymList.add(object.getString(TAG_NAME));
                gymList.add(object.getString(TAG_OPEN_HOURS));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gymList;
    }




}
