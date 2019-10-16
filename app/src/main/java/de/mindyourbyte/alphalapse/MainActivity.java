package de.mindyourbyte.alphalapse;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler((thread, exp) -> {
            Logger.exception(exp);
            System.exit(1);
        });

        getTabHost().requestFocus();
        Logger.info("Created MainActivity");
        addTab("interval_settings", getString(R.string.interval_settings_label), 0, TimeLapseSettingsActivity.class);
        addTab("settings", getString(R.string.settings_label), 0, SettingsActivity.class);
        try {
            addTab("preview", getString(R.string.preview_tab_label), 0, PreviewActivity.class);
        }
        catch (Throwable e)
        {
            Logger.exception(e);
        }
    }

    protected void addTab(String tag, String label, int iconId, Class activity)
    {
        Logger.info("Adding Tab " + label);
        TabHost.TabSpec tab = getTabHost().newTabSpec(tag);
        tab.setIndicator(label);
        tab.setContent(new Intent(this, activity));
        getTabHost().addTab(tab);
    }
}
