package de.mindyourbyte.alphalapse;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler((thread, exp) -> {
            Logger.exception(exp);
        });

        getTabHost().requestFocus();
        Logger.info("Created MainActivity");
        addTab("preview", getString(R.string.preview_tab_label), 0, PreviewActivity.class);

        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        Logger.info("Open GL support is: " + configurationInfo.getGlEsVersion());


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
