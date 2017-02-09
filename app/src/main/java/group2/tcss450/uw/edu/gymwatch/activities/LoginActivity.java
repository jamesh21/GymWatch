package group2.tcss450.uw.edu.gymwatch.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
import group2.tcss450.uw.edu.gymwatch.activities.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/" +
            "~xufang/logintest.php";

    private String mResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mResponse = "";
    }

    public void buttonClick(View view) {
        AsyncTask<String, Void, String> task = null;
        String user = ((EditText) findViewById(R.id.editTextusername)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextpassword)).getText().toString();
        switch (view.getId()) {
            case R.id.buttonLogin:
                if(password.equals("")){
                    Toast.makeText(getApplicationContext(), "empty password"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    task = new GetWebServiceTask();
                    task.execute(PARTIAL_URL, user, password);
                }
                break;
            default:
                throw new IllegalStateException("Not implemented");
        }
    }

    public void doRegistration(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void checker(){
        if (mResponse.equals("Invaild username.")) {
            Toast.makeText(getApplicationContext(), ""+ mResponse
                    , Toast.LENGTH_LONG)
                    .show();
        }
        else if (mResponse.equals("correct password")) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);

        } else {
            Toast.makeText(getApplicationContext(), "Incorrect Password."
                    , Toast.LENGTH_SHORT)
                    .show();
        }

        mResponse = "";
    }
    private class GetWebServiceTask extends AsyncTask<String, Void, String> {

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
        }

    }

}
