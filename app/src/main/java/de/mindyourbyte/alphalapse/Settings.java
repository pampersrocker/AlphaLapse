package de.mindyourbyte.alphalapse;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    public final String INTERVAL_PREFIX = "de.mindyourbyte.alphalapse.interval.";
    public final String START_TIME_PREFIX = "de.mindyourbyte.alphalapse.starttime.";
    public final String END_TIME_PREFIX = "de.mindyourbyte.alphalapse.endtime.";
    public final String AUTOFOCUS = "de.mindyourbyte.alphalapse.autofocus";
    public final String TURN_OFF_SCREEN = "de.mindyourbyte.alphalapse.screen_off";

    public boolean Autofocus;
    public boolean TurnOffScreen;
    public TimeSetting Interval = new TimeSetting();
    public TimeSetting StartTime = new TimeSetting();
    public TimeSetting EndTime = new TimeSetting();

    private static final Settings ourInstance = new Settings();

    public static Settings getInstance() {
        return ourInstance;
    }

    private Settings() {
    }

    public void save(Context context){
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(AUTOFOCUS, Autofocus);
        editor.putBoolean(TURN_OFF_SCREEN, TurnOffScreen);
        Interval.save(editor, INTERVAL_PREFIX);
        StartTime.save(editor, START_TIME_PREFIX);
        EndTime.save(editor, END_TIME_PREFIX);
        editor.apply();
    }

    public void load(Context context){
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Autofocus = preferences.getBoolean(AUTOFOCUS, false);
        TurnOffScreen = preferences.getBoolean(TURN_OFF_SCREEN, true);
        Interval.load(preferences, INTERVAL_PREFIX);
        StartTime.load(preferences, START_TIME_PREFIX);
        EndTime.load(preferences, END_TIME_PREFIX);
    }
}
