package com.hsappdev.ahs;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static SettingsManager mInstance;

    // Use getApplicationContext() rather than getContext()
    public static SettingsManager getInstance(Context context) {
        if(mInstance == null)
            mInstance = new SettingsManager(context);
        return mInstance;
    }


    private final SharedPreferences settings;
    private final String themeKey;
    private SettingsManager(Context context) {
        settings = context.getSharedPreferences(context.getResources().getString(R.string.settings_file), Context.MODE_PRIVATE);
        themeKey = context.getResources().getString(R.string.theme_key);
    }

    public interface DayNightCallback {
        void onNewMode(boolean isNightModeOn);
    }

    //https://medium.com/androiddevelopers/appcompat-v23-2-daynight-d10f90c83e94
    /**
     * gets whether Night Mode has been turned on from SharedPreferences
     * @return above or false by default if there is no setting yet
     */
    public boolean isNightModeOn() {
        return settings.getBoolean(themeKey, false);
    }

    /**
     * updates whether Night Mode has been turned on in SharedPreferences
     * @param isNightModeOn exactly what it sounds like
     */
    public void updateNightMode(boolean isNightModeOn) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(themeKey, isNightModeOn).apply();
    }

}