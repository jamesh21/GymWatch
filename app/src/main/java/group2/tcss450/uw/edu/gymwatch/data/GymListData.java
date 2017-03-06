package group2.tcss450.uw.edu.gymwatch.data;


// STILL THINKING IF WE SHOULD KEEP THIS CLASS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


/**
 * Created by james on 1/25/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to grab the data from the api to be displayed on the app. This is a work
 * in progress, used for testing the layout of the my gyms recycle view at the moment.
 */
public class GymListData {

    private static String[] mGymNameList = {"YMCA", "24 hr Fitness", "LA Fitness", "Gold's Gym"};
    private static String[] mGymAddressList = {"1144 Market St, Tacoma, WA 98402",
            "1144 Market St, Tacoma, WA 98402", "1144 Market St, Tacoma, WA 98402",
            "1144 Market St, Tacoma, WA 98402"};
    private static String[] mGymFillList = {"75", "80", "25", "90"};
    private static float[] mGymRatingList = {2.0f, 5.0f, 3.0f, 2.5f};
    private static int[] myGymImageList = {android.R.drawable.ic_dialog_alert,
            android.R.drawable.ic_dialog_map, android.R.drawable.ic_btn_speak_now,
            android.R.drawable.ic_dialog_email};


    /**
     * This method is used to get all the gyms to display on the screen
     * @return
     */
    public static List<GymItem> getList(String username) {
        List<GymItem> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            //Making a fake list of gyms to display onto the screen
            for (int j = 0; j < mGymNameList.length;j++) {
                data.add(new GymItem(mGymNameList[j], "5", mGymAddressList[j],
                        mGymFillList[j], "hello", new ArrayList<String>(), true));
            }
        }
        return data;
    }
}
