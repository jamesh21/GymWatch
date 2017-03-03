package group2.tcss450.uw.edu.gymwatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;

public class GymDetailActivity extends AppCompatActivity {
    /**The field for the URL that this activity connects to. */
    private static final String Part_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~xufang/registertest.php";//Needs to change once Xufang finishes the new .php file

    /**Field for the response which is returned form the web server. */
    private String mResponse;

    /**Reference to the register button. */
    private Button mButton;

    /**Reference to the registration activity.*/
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_detail2);
        Intent i = getIntent();
        GymItem gym = (GymItem) i.getParcelableExtra("Gym");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String gymName = gym.getGymName();
        final String gymAddress = gym.getGymAddress();
        String gymFill = gym.getGymFill();
        final String gymImage = gym.getGymImage();
        final String gymRating = gym.getGymRating();


        //OpenSource Github API for multiline titles.
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout =
                (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(gym.getGymName());
        //Get the Gym address
        TextView gym_Address = (TextView) findViewById(R.id.gym_address_detail);
        gym_Address.setText(gym.getGymAddress());

        RatingBar gym_Ratingbar = (RatingBar) findViewById(R.id.gym_rating_detail);
        gym_Ratingbar.setRating(Float.parseFloat(gymRating));

        TextView gym_Fill = (TextView) findViewById(R.id.gym_fill_detail);
        gym_Address.setText(gym.getGymFill());



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, Void, String> task = null;
                task = new GymDetailActivity.PostWebServiceTask();
                task.execute(Part_URL, gymName, gymAddress, gymRating, gymImage);
                Snackbar.make(view, "Saved to My Gyms", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Inner AsyncTask class, handle the internet connection with the web server. Passing
     * parameters to the web server and get the response back.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {


        /**
         * Perform web connection, passing parameters and retrieve response back by POST
         * @param strings includes destination URL, and parameters we want to pass.
         * @return response is the string we get back from the web server.
         */
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            HttpURLConnection urlConnection = null;
            String url = strings[0];
            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("name", "UTF-8")
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("address", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8")
                        + "&" + URLEncoder.encode("rating", "UTF-8")
                        + "=" + URLEncoder.encode(strings[2], "UTF-8")
                        + "&" + URLEncoder.encode("image", "UTF-8")
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
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            mResponse += result;
            mButton.setEnabled(true);
        }

    }
}
