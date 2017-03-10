package group2.tcss450.uw.edu.gymwatch.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import group2.tcss450.uw.edu.gymwatch.R;
import group2.tcss450.uw.edu.gymwatch.activities.GymDetailActivity;
import group2.tcss450.uw.edu.gymwatch.data.GymAdapter;
import group2.tcss450.uw.edu.gymwatch.data.GymItem;
import group2.tcss450.uw.edu.gymwatch.data.ItemClickSupport;


/**
 * This class is for the myGyms fragment which will hold gyms added by the user.
 */
public class MyGymsFragment extends Fragment {

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~xufang/getGymsFromDB.php";
    /** The URL for getting google images. */

    private static final String DELETE_URL
            = "http://cssgate.insttech.washington.edu/~xufang/deleteGymFromDB.php";

    private static final String FAKE_DATA = "http://cssgate.insttech.washington.edu/~xufang/" +
            "fakeData.php";
    /** This URL is used for when there are no pictures available for that gym. */

    private static final String NO_IMAGE = "https://upload.wikimedia.org/wikipedia/commons/" +
            "thumb/a/ac/No_image_available.svg/2000px-No_image_available.svg.png";


    private static final String TAG_NAME = "gymname";
    private static final String TAG_IMAGE = "imageurl";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_RATING = "rating";
    private static final String TAG_PLACE_ID = "placeid";

    /** Reference to the list of gyms for the user. */
    private List<GymItem> mUserGyms;
    /** Reference to the gym adapter. */
    private GymAdapter mGymAdapter;
    /** Reference to the view. */
    private View mView;
    /** Reference to the current user. */
    private String mCurrentUser;

    @Override
    /**
     * Method initializes the recycle view and adds it to this fragment.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mCurrentUser = getArguments().getString("username");
        mView = inflater.inflate(R.layout.fragment_my_gyms, container, false);

        mUserGyms = new ArrayList<>();
        AsyncTask<String, Void, String> task = new GetGymsFromDBTask();
        task.execute(PARTIAL_URL, mCurrentUser);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView title = (TextView) getActivity().findViewById(R.id.fragment_title);
        title.setText(R.string.my_gyms);
        AsyncTask<String, Void, String> task = new GetGymsFromDBTask();
        task.execute(PARTIAL_URL, mCurrentUser);
    }


    /** This method is the callback method for whenever a item within the recycler view is
     * swiped.
     * @return the callback
     */
    private ItemTouchHelper.Callback touchMethod() {
        ItemTouchHelper.SimpleCallback simpleGymTouchCallBack =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleGymTouchCallBack;
    }

    /**
     * Method is used to delete the item from the DB from the position.
     * @param position to be deleted
     */
    private void deleteItem(final int position) {
        GymItem gym = mUserGyms.get(position);
        mUserGyms.remove(position);
        AsyncTask<String, Void, String> task = new DeleteGymFromDBTask();
        task.execute(DELETE_URL, gym.getGymID(), mCurrentUser);
        mGymAdapter.notifyItemRemoved(position);
        Snackbar.make(getView(), "Removed from Gyms", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    /**
     * Inner AsyncTask class getting gyms from the DB, handle the internet connection with the web server. Passing
     * parameters to the web server and get the response back.
     */
    private class GetGymsFromDBTask extends AsyncTask<String, Void, String> {

        /** Reference to the progress dialog. */
        private ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgress = new ProgressDialog(getContext());
            mProgress.setMessage("Loading");
            mProgress.show();
        }
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
            try {
                JSONArray json = new JSONArray(result);
                mUserGyms = new ArrayList<>();
                // Building the gym item and placing into gym list
                for (int i = 0; i < json.length(); i++) {
                    String str_result = new StaticWebServiceTask().execute(FAKE_DATA).get();
                    JSONObject gym = json.getJSONObject(i);
                    String gymName = gym.getString(TAG_NAME);
                    String gymAddress = gym.getString(TAG_ADDRESS);
                    String photoReference = gym.getString(TAG_IMAGE);
                    String gymImage;
                    String placeId = gym.getString(TAG_PLACE_ID);
                    if (!photoReference.isEmpty()) {
                        gymImage = photoReference;
                    } else {
                        gymImage = NO_IMAGE;
                    }
                    String gymRating = gym.getString(TAG_RATING);
                    GymItem currentGym = new GymItem(gymName, gymRating, gymAddress, str_result, gymImage, placeId);
                    mUserGyms.add(currentGym);
                }
                //Setting up recycler view and adding the touch helper for delete
                RecyclerView gymRecView = (RecyclerView) mView.findViewById(R.id.gym_home_list);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                gymRecView.setLayoutManager(layoutManager);
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchMethod());
                itemTouchHelper.attachToRecyclerView(gymRecView);
                mGymAdapter = new GymAdapter(mUserGyms, getActivity());
                gymRecView.setAdapter(mGymAdapter);

                // Going into the detail view.
                ItemClickSupport.addTo(gymRecView).setOnItemClickListener
                                    (new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getActivity(), GymDetailActivity.class);
                        i.putExtra("Gym", mUserGyms.get(position));
                        startActivity(i);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();

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


    /**
     * Inner AsyncTask class for deleting, handle the internet connection with the web server.
     * Passing parameters to the web server and get the response back.
     */
    private class DeleteGymFromDBTask extends AsyncTask<String, Void, String> {

        /**
         * perform web connection, passing parameters and retrieve response back by POST
         * @param strings includes destination URL, and parameters we want to pass.
         * @return response is the string we get back from the web server.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
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
            return response;
        }


    }

}
