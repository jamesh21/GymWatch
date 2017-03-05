package group2.tcss450.uw.edu.gymwatch.data;


// STILL THINKING IF WE SHOULD KEEP THIS CLASS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


/**
 * Created by james on 1/25/2017.
 */

import android.os.AsyncTask;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class is used to grab the data from the api to be displayed on the app. This is a work
 * in progress, used for testing the layout of the my gyms recycle view at the moment.
 */
public class GymListData {

//    private static String[] mGymNameList = {"YMCA", "24 hr Fitness", "LA Fitness", "Gold's Gym"};
//    private static String[] mGymAddressList = {"1144 Market St, Tacoma, WA 98402",
//            "1144 Market St, Tacoma, WA 98402", "1144 Market St, Tacoma, WA 98402",
//            "1144 Market St, Tacoma, WA 98402"};
//    private static String[] mGymFillList = {"75", "80", "25", "90"};
//    private static float[] mGymRatingList = {2.0f, 5.0f, 3.0f, 2.5f};
//    private static int[] myGymImageList = {android.R.drawable.ic_dialog_alert,
//            android.R.drawable.ic_dialog_map, android.R.drawable.ic_btn_speak_now,
//            android.R.drawable.ic_dialog_email};

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~xufang/getGymsFromDB.php";

    private static final String TAG_NAME = "gymname";
    private static final String TAG_IMAGE = "imageurl";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_RATING = "rating";
    private static final String FAKE_DATA = "http://cssgate.insttech.washington.edu/~xufang/fakeData.php";

    private static List<GymItem> mUserGyms;
    /**
     * This method is used to get all the gyms to display on the screen
     * @return
     */
    public static List<GymItem> getList(String username) {
        GymListData gymData = new GymListData();
        mUserGyms = new ArrayList<>();
        AsyncTask<String, Void, String> task = gymData.new GetGymsFromDBTask();
        task.execute(PARTIAL_URL, username);

//        for (int i = 0; i < 5; i++) {
//            //Making a fake list of gyms to display onto the screen
//            for (int j = 0; j < mGymNameList.length;j++) {
//                data.add(new GymItem(mGymNameList[j], "5", mGymAddressList[j],
//                        mGymFillList[j], "hello"));
//            }
//        }
        return mUserGyms;
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
            // Something wrong with the network or the URL.
//            if (result.startsWith("Unable to")) {
//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
//                        .show();
//                return;
//            }
            //List<GymItem> usersGyms = new ArrayList<>();
            try {
                JSONArray json = new JSONArray(result);
                for (int i = 0; i < json.length(); i++) {
                    String str_result = new StaticWebServiceTask().execute(FAKE_DATA).get();
                    JSONObject gym = json.getJSONObject(i);
                    String gymName = gym.getString(TAG_NAME);
                    String gymAddress = gym.getString(TAG_ADDRESS);
                    String gymImage = gym.getString(TAG_IMAGE);
                    String gymRating = gym.getString(TAG_RATING);
                    GymItem currentGym = new GymItem(gymName, gymRating, gymAddress, str_result, gymImage);
                    mUserGyms.add(currentGym);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
//            mUserGyms = usersGyms;
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
