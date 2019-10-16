package de.mindyourbyte.alphalapse;

import android.content.SharedPreferences;

public class TimeSetting {
    public int Hour;
    public int Minute;
    public int Second;
    public boolean AM;

    public void load(SharedPreferences preferences, String prefix)
    {
        Hour = preferences.getInt(prefix + "hours", 0);
        Minute  = preferences.getInt(prefix + "minutes", 0);
        Second = preferences.getInt(prefix + "seconds", 0);
        AM = preferences.getBoolean(prefix + "ampm", false);
    }

    public void save(SharedPreferences.Editor preferences, String prefix)
    {
        preferences.putInt(prefix + "hours", Hour);
        preferences.putInt(prefix + "minutes", Minute);
        preferences.putInt(prefix + "seconds", Second);
        preferences.putBoolean(prefix + "ampm", AM);
    }
}
