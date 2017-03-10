package group2.tcss450.uw.edu.gymwatch.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.data.LoginSavePreference;

/**
 * This Activity Class handles log in activity.
 */
public class LoginActivity extends AppCompatActivity {

    /**The field for the URL for Login. */
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~xufang/logintest.php";

    /**Field for the reponse gets back from the web server. */
    private String mResponse;

    /** Reference to the username. */
    private String mUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!LoginSavePreference.getUser(this).isEmpty()) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", LoginSavePreference.getUser(this));
            startActivity(intent);
        }
        mResponse = "";
    }

    /**
     * This method handles the evnet after you click the log in button.
     * @param view the view we get id from
     */
    public void buttonClick(View view) {
        AsyncTask<String, Void, String> task = null;
        mUsername = ((EditText) findViewById(R.id.editTextusername)).getText().toString();
        //get the string password from the editText.
        String password = ((EditText) findViewById(R.id.editTextpassword)).getText().toString();
        switch (view.getId()) {
            case R.id.buttonLogin:
                //When the user did not input password, shows up a toast.
                if(password.equals("")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(this);
                    alert.setMessage("Please input a " +
                            "password.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    alert.show();
                } else {
                    //when the user did enter something as password.
                    task = new PostWebServiceTask();
                    task.execute(PARTIAL_URL, mUsername, password);
                }
                break;
            default:
                throw new IllegalStateException("Not implemented");
        }
    }

    /**
     * Thw method that starts the registration activity.
     * @param view which represents what called this method
     */
    public void doRegistration(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    /**
     * Check what got returned by the web server after passing username and
     * password the user entered to the web server. The actual password
     * compare of the password is done in the php. reset mResponse field when
     * checks are done.
     */
    private void checker(){
        // when the username is not found in the database
        if (mResponse.equals("Invaild username.")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Can't find this username on server.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            alert.show();
        }
        //start next activity here
        else if (mResponse.equals("correct password")) {
            LoginSavePreference.setUser(this, mUsername);
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("username", mUsername);
            startActivity(intent);

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Incorrect password.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            alert.show();
        }

        mResponse = "";
    }


    /**
     * Inner AsyncTask class, handle the internet connection with the web server. Passing
     * parameters to the web server and get the response back.
     */
    private class PostWebServiceTask extends AsyncTask<String, Void, String> {

        /**
         * perform web connection, passing parameters and retrieve response back by POST
         * @param strings includes destination URL, and parameters we want to pass.
         * @return response is the string we get back from the web server.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("Three String arguments required.");
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
                        + "=" + URLEncoder.encode(strings[1], "UTF-8")
                        + "&" + URLEncoder.encode("password", "UTF-8")
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
            checker();
        }

    }

}
