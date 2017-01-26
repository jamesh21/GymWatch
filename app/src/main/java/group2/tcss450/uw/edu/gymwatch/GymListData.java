package group2.tcss450.uw.edu.gymwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 1/25/2017.
 */

public class GymListData {

    private static String[] mGymNameList = {"YMCA", "24 hr Fitness", "LA Fitness", "Gold's Gym"};
    private static String[] mGymAddressList = {"1144 Market St, Tacoma, WA 98402",
            "1144 Market St, Tacoma, WA 98402", "1144 Market St, Tacoma, WA 98402",
            "1144 Market St, Tacoma, WA 98402"};
    private static String[] mGymFillList = {"75", "80", "25", "90"};
    private static String[] mGymRatingList = {"5 Stars", "4 Stars", "5 Stars", "2 Stars"};
    private static int[] myGymImageList = {android.R.drawable.ic_dialog_alert,
            android.R.drawable.ic_dialog_map, android.R.drawable.ic_btn_speak_now,
            android.R.drawable.ic_dialog_email};


    public static List<GymItem> getList() {
        List<GymItem> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < mGymNameList.length;j++) {
                data.add(new GymItem(mGymNameList[j], mGymRatingList[j], mGymAddressList[j],
                        mGymFillList[j], myGymImageList[j]));
            }
        }
        return data;
    }
}