package group2.tcss450.uw.edu.gymwatch.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by James on 3/5/2017.
 */

/**
 * This class is used for saving the username to the shared preference, in order for auto login.
 */
public class LoginSavePreference {

    /** Constant for the key of the shared preference. */
    private static final String PREF_USER = "username";
    /** Constant for the name of the shared preference file. */
    private static final String PREF_FILE = "group2.tcss450.uw.edu.gymwatch_preferences";

    /**
     * This method is used to set the username within the shared preference file.
     * @param context of the application
     * @param username the new username to set to the file
     */
    public static void setUser(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_USER, username).apply();
    }

    /**
     * This method is used to get the username within the shared preference file.
     * @param context of the application
     * @return the username within the file
     */
    public static String getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return prefs.getString(PREF_USER, "");
    }
}
