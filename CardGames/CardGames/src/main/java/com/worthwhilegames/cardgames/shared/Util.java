package com.worthwhilegames.cardgames.shared;

import android.content.Context;
import android.content.SharedPreferences;
import com.worthwhilegames.cardgames.BuildConfig;

/**
 * A Utility class that contains common generic methods relevant
 * to any object in the application
 */
public class Util {

    public static final String TAG_GENERIC = Util.class.getName();

    /**
     * Are we in a unit test?
     */
    public static boolean isTestSuite = false;

    /**
     * Checks whether this is a debug build or not.
     *
     * This toggles different logging capabilities
     *
     * @return whether this is a production build or not
     */
    public static boolean isDebugBuild() {
        return !isTestSuite && BuildConfig.DEBUG;
    }

    /**
     * Checks whether cheater mode is enabled
     *
     * This displays all player hands
     *
     * @return whether cheater mode is enabled
     */
    public static boolean isCheaterMode(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences(Constants.PREFERENCES, 0);

        return prefs.getBoolean(Constants.PREF_CHEATER_MODE, false);
    }
}
