package group2.tcss450.uw.edu.gymwatch.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import group2.tcss450.uw.edu.gymwatch.activities.HomeActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final String Part_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~xufang/registertest.php";

    private String mResponse;

    private Button mButton;

    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mResponse = "";
        mButton = (Button) findViewById(R.id.button_Register);
        mActivity = this;
    }

    public void buttonClick(View view) {
        AsyncTask<String, Void, String> task = null;
        String user = ((EditText) findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        String confirm = ((EditText) findViewById(R.id.editTextConfirm)).getText().toString();
        switch (view.getId()) {
            case R.id.button_Register:
                if(password.equals("")){
                    Toast.makeText(getApplicationContext(), "empty password"
                            , Toast.LENGTH_LONG)
                            .show();
                } else if(!password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "the password you have entered do not match"
                            , Toast.LENGTH_LONG)
                            .show();
                } else if(user.length() > 25) {
                    Toast.makeText(getApplicationContext(), "username has to be less than 25 characters"
                            , Toast.LENGTH_LONG)
                            .show();
                } else if(password.length() < 8 || password.length() > 16) {
                    Toast.makeText(getApplicationContext(), "password has to be not less than 8 characters," +
                                    "no more than 16 characters."
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    task = new GetWebServiceTask();
                    task.execute(Part_URL, user, password);
                }
                break;
            default:
                throw new IllegalStateException("Not implemented");
        }
    }

    private void checker() {
        if (mResponse.equals("username already taken")) {
            Toast.makeText(getApplicationContext(), ""+ mResponse
                    , Toast.LENGTH_LONG)
                    .show();
        } else if (mResponse.equals("Creating username on server has failed")) {
            Toast.makeText(getApplicationContext(), ""+ mResponse
                    , Toast.LENGTH_LONG)
                    .show();
        } else if (mResponse.equals("Creating password on server has failed")) {
            Toast.makeText(getApplicationContext(), ""+ mResponse
                    , Toast.LENGTH_LONG)
                    .show();
        }else if (mResponse.equals("Account created successfully")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Creating Account successfully! Please remember your username and password" +
                    "Click Continue to auto login using your new account!").setPositiveButton("Continue..", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(mActivity, HomeActivity.class));
                    dialog.dismiss();
                }
            }).create();
            alert.show();
        }
        mResponse = "";
    }

    private class GetWebServiceTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute(){
            mButton.setEnabled(false);
        }

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
            mButton.setEnabled(true);
        }

    }
}
