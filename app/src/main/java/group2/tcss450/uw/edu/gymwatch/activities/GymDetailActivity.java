package group2.tcss450.uw.edu.gymwatch.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;
import group2.tcss450.uw.edu.gymwatch.data.LoginSavePreference;

public class GymDetailActivity extends AppCompatActivity {
    /**The field for the URL that this activity connects to. */
    private static final String Part_URL
            = "http://cssgate.insttech.washington.edu/~xufang/insertGymToDB.php";
    private static final String Part_URL_DELETE
            = "http://cssgate.insttech.washington.edu/~xufang/deleteGymFromDB.php";
    private static final String Part_URL_CHECK
            ="http://cssgate.insttech.washington.edu/~xufang/addedOrNot.php";

    /**Field for the response which is returned form the web server. */
    //private String mResponse;
    private int iconType = 0;//0 is add, 1 is delete
    private int checkType = 0;//0 is add, 1 is delete, 2 is check
    private int status = 0;//0 is failed, 1 is pass.


    /**Reference to the registration activity.*/
    private Activity mActivity;

    ArrayList<Integer> hoursArray = new ArrayList<>();
    ArrayAdapter<String> adapter;
    AsyncTask<String, Void, String> task = null;
    AsyncTask<String, Void, String> task2 = null;
    AsyncTask<String, Void, String> task3 = null;
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_detail2);
        task = new GymDetailActivity.PostWebServiceTask();
        task2 = new GymDetailActivity.PostWebServiceTask();
        task3 = new GymDetailActivity.PostWebServiceTask();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Intent i = getIntent();
        GymItem gym = i.getExtras().getParcelable("Gym");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String userName = LoginSavePreference.getUser(this);
        final String gymName = gym.getGymName();
        final String gymAddress = gym.getGymAddress();
        final String gymPlaceId = gym.getGymID();
        String gymI = gym.getGymImage();
        final String gymImage;
        if(gymI.equals("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png)")){
            gymImage = "";
        } else {
            gymImage = gymI;
        }


        final String gymRating = gym.getGymRating();
        final int gymFill = Integer.parseInt(gym.getGymFill());



        //OpenSource Github API for multiline titles.
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout =
                (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(gym.getGymName());
        //Get the Gym address
        TextView gym_Address = (TextView) findViewById(R.id.gym_address_detail);
        gym_Address.setText(gym.getGymAddress());



        TextView gym_Fill = (TextView) findViewById(R.id.gym_fill_detail);
        gym_Fill.setText(gym.getGymFill());
//Change the color of the textview based on how full a gym is.
        if(gymFill >= 0 && gymFill <= 17) {
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.min_fill));
        } else if (gymFill >= 17 && gymFill <= 33){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.seventeen));

        } else if (gymFill >= 33 && gymFill <= 50){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.black_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.thirty_three));

        } else if (gymFill >= 50 && gymFill <= 66){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.black_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.mid_fill));

        } else if (gymFill >= 66 && gymFill <= 83) {
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.sixty_six));

        } else if (gymFill >= 82 && gymFill <= 90){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.eighty_two));

        } else {
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.max_fill));
        }

        ImageView gym_image_detail = (ImageView) findViewById(R.id.gymImageToolbar);
        Picasso.with(this)
                .load(gymImage)
                .into(gym_image_detail);


        checkType = 2;//Set the check type to check if place is already added.
        fab.setImageResource(R.drawable.ic_add_white_24px);

        task.execute(Part_URL_CHECK, gymPlaceId, userName);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(iconType == 0) {//If the icon is an add icon
                    checkType = 0;
                    task2.execute(Part_URL, gymPlaceId, userName, gymName, gymImage, gymAddress, gymRating);
                    Snackbar.make(view, "Saved to Gyms", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    iconType = 1;
                } else {//If the icon is a delete icon
                    checkType = 1;
                    task3.execute(Part_URL_DELETE, gymPlaceId, userName);
                    if(status == 1) {
                        Snackbar.make(view, "Removed from Gyms", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        iconType = 0;
                    }

                }

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    /**
     * Puts the maps valus into an array for listview
     * @param m the map
     * @param hours the array
     */
    private void convertMap(Map<Integer, Integer> m, ArrayList<Integer> hours) {
        for(int i = 0; i < m.size(); i+=2) {
            hours.add(m.get(i));
            hours.add(m.get(i+1));
        }
    }

    /**
     * Inner AsyncTask class, handle the internet connection with the web server. Passing
     * parameters to the web server and get the response back.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {
        /**
         * Perform web connection, passing parameters and retrieve response back by POST
         *
         * @param strings includes destination URL, and parameters we want to pass.
         * @return response is the string we get back from the web server.
         */
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            if (checkType == 0) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

                    String data = URLEncoder.encode("placeid", "UTF-8")
                            + "=" + URLEncoder.encode(strings[1], "UTF-8")
                            + "&" + URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(strings[2], "UTF-8")
                            + "&" + URLEncoder.encode("gymname", "UTF-8")
                            + "=" + URLEncoder.encode(strings[3], "UTF-8")
                            + "&" + URLEncoder.encode("imageurl", "UTF-8")
                            + "=" + URLEncoder.encode(strings[4], "UTF-8")
                            + "&" + URLEncoder.encode("address", "UTF-8")
                            + "=" + URLEncoder.encode(strings[5], "UTF-8")
                            + "&" + URLEncoder.encode("rating", "UTF-8")
                            + "=" + URLEncoder.encode(strings[6], "UTF-8");
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
            } else if (checkType == 1) {//Delete
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());

                    String data = URLEncoder.encode("placeid", "UTF-8")
                            + "=" + URLEncoder.encode(strings[1], "UTF-8")
                            + "&" + URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(strings[2], "UTF-8");
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
            } else if (checkType == 2) {//Check
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    for (int i = 0; i < strings.length; i++) {
                        System.out.println(strings[i]);
                    }
                    String data = URLEncoder.encode("placeid", "UTF-8")
                            + "=" + URLEncoder.encode(strings[1], "UTF-8")
                            + "&" + URLEncoder.encode("username", "UTF-8")
                            + "=" + URLEncoder.encode(strings[2], "UTF-8");
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

            }
            return response;
        }

        /**
         * This method checks if the response is valid, If it is, store the response
         * in to the field mResponse.
         *
         * @param result the response string we  get back from the server.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if(checkType == 0) {
                fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_white_24px));
            } else if (checkType == 1) {//Delete
                if (result.equals("deleted")) {
                    status = 1;
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_white_24px));
                }
            } else if (checkType == 2) {//Check
                if (result.equals("true")) {
                    System.out.println("TRUE");
                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_delete_white_24px));
                        iconType = 1;
                } else {//Gym is not there
                        System.out.println("FALSE");
                        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_white_24px));
                        iconType = 0;
                    }
                }
            }

        }


}
