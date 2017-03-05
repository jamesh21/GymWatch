package group2.tcss450.uw.edu.gymwatch.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by James on 3/5/2017.
 */

public class LoginSavePreference {

    private static final String PREF_USER = "username";
    private static final String PREF_FILE = "group2.tcss450.uw.edu.gymwatch_preferences";

    public static void setUser(Context context, String username) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_USER, username).apply();
    }

    public static String getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        return prefs.getString(PREF_USER, "");
    }
}
