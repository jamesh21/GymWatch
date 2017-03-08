package group2.tcss450.uw.edu.gymwatch.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.Map;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;
import group2.tcss450.uw.edu.gymwatch.data.LoginSavePreference;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

public class GymDetailActivity extends AppCompatActivity {
    /**The field for the URL that this activity connects to. */
    private static final String PART_URL
            = "http://cssgate.insttech.washington.edu/~xufang/insertGymToDB.php";

    private static final String HISTORY_URL = "http://cssgate.insttech.washington.edu/" +
                                                     "~xufang/bargraph.php";

    /**Field for the response which is returned form the web server. */
    private String mResponse;

    private static final String NO_IMAGE = "https://upload.wikimedia.org/wikipedia/" +
            "commons/thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png";



    /**Reference to the registration activity.*/
    private Activity mActivity;

    ArrayList<Integer> hoursArray = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_detail2);
        Intent intent = getIntent();
        GymItem gym = intent.getParcelableExtra("Gym");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final String userName = LoginSavePreference.getUser(this);
        final String gymName = gym.getGymName();
        final String gymAddress = gym.getGymAddress();
        final String gymPlaceId = gym.getGymID();
        final String gymI = gym.getGymImage();
        final String gymRating = gym.getGymRating();
        final int gymFill = Integer.parseInt(gym.getGymFill());

        AsyncTask<String, Void, String> task = new GymDetailActivity.GetGymFillHistory();
        task.execute(HISTORY_URL);


//        List<Column> columns = new ArrayList<>();
//        List<AxisValue> axisList = new ArrayList<>();
//        Axis axisX = new Axis();
//
//        ColumnChartView barGraph = (ColumnChartView) findViewById(R.id.bar_graph);
//        ColumnChartData barData;
//        barGraph.setZoomEnabled(false);
//        barGraph.setInteractive(false);
//        String[] labels = {"6am", "8am", "10am", "12pm",
//                "2pm",  "4pm",  "6pm",  "8pm",  "10pm"};
//        List<SubcolumnValue> percentages;
//        for (int i = 0; i < labels.length; i++) {
//            int num = i * 5;
//            percentages = new ArrayList<>();
//            percentages.add(new SubcolumnValue((float) num, getResources().getColor(R.color.dot_light_screen2)));
//            Column column = new Column(percentages);
//
//            column.setHasLabels(true);
//            columns.add(column);
//            axisList.add(new AxisValue(i).setLabel(labels[i]));
//        }
//        barData = new ColumnChartData(columns);
//        axisX.setValues(axisList);
//        axisX.setTextSize(12);
//        axisX.setTextColor(Color.BLACK);
//        barData.setAxisXBottom(axisX);
//        barGraph.setColumnChartData(barData);

        //OpenSource Github API for multiline titles.
        net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout collapsingToolbarLayout =
                (net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle(gym.getGymName());
        //Get the Gym address
        TextView gym_Address = (TextView) findViewById(R.id.gym_address_detail);
        gym_Address.setText(gym.getGymAddress());



        TextView gym_Fill = (TextView) findViewById(R.id.gym_fill_detail);
        gym_Fill.setText(gym.getGymFill());

        if(gymFill >= 0 && gymFill <= 17) {
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.min_fill));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.min_fill));
        } else if (gymFill >= 17 && gymFill <= 33){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.seventeen));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.seventeen));

        } else if (gymFill >= 33 && gymFill <= 50){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.black_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.thirty_three));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.thirty_three));

        } else if (gymFill >= 50 && gymFill <= 66){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.black_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.mid_fill));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.mid_fill));

        } else if (gymFill >= 66 && gymFill <= 83) {
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.sixty_six));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.sixty_six));

        } else if (gymFill >= 82 && gymFill <= 90){
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.eighty_two));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.eighty_two));

        } else {
            gym_Fill.setTextColor(ContextCompat.getColor(this, R.color.white_text));
            gym_Fill.setBackgroundColor(ContextCompat.getColor(this, R.color.max_fill));
            //gym_Address.setBackgroundColor(ContextCompat.getColor(this, R.color.max_fill));
        }

        ImageView gym_image_detail = (ImageView) findViewById(R.id.gymImageToolbar);
        Picasso.with(this)
                .load(gymI)
                .into(gym_image_detail);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask<String, Void, String> task = null;
                task = new GymDetailActivity.PostWebServiceTask();
                String gymImage;
                if(gymI.equals(NO_IMAGE)) {
                    gymImage = "";
                } else {
                    gymImage = gymI;
                }
                task.execute(PART_URL, gymPlaceId, userName, gymName, gymImage, gymAddress, gymRating);
                Snackbar.make(view, "Saved!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                fab.setImageResource(R.drawable.ic_delete_white_24px);
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

        }

    }


    private class GetGymFillHistory extends AsyncTask<String, Void, String> {


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
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            try {
                JSONObject fillObject = new JSONObject(result);
                List<Column> columns = new ArrayList<>();
                List<AxisValue> axisList = new ArrayList<>();
                Axis axisX = new Axis();
                ColumnChartView barGraph = (ColumnChartView) findViewById(R.id.bar_graph);
                ColumnChartData barData;
                barGraph.setZoomEnabled(false);
                barGraph.setInteractive(false);
                String[] labels = {"6am", "8am", "10am", "12pm",
                        "2pm", "4pm", "6pm", "8pm", "10pm"};
                List<SubcolumnValue> percentages;
                for (int i = 0; i < labels.length; i++) {
                    String numPercentage = fillObject.getString(Integer.toString(i));
                    percentages = new ArrayList<>();
                    percentages.add(new SubcolumnValue(Float.parseFloat(numPercentage), getResources().getColor(R.color.dot_light_screen2)));
                    Column column = new Column(percentages);
                    column.setHasLabels(true);
                    columns.add(column);
                    axisList.add(new AxisValue(i).setLabel(labels[i]));
                }
                barData = new ColumnChartData(columns);
                axisX.setValues(axisList);
                axisX.setTextSize(12);
                axisX.setTextColor(Color.BLACK);
                barData.setAxisXBottom(axisX);
                barGraph.setColumnChartData(barData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
