package de.mindyourbyte.alphalapse;

import android.os.Bundle;

public class TimeLapseSettingsActivity extends BaseActivity {

    NonTouchTimePicker startTimeButton;
    NonTouchTimePicker endTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse_settings);
        startTimeButton = (NonTouchTimePicker) findViewById(R.id.start_time_button);
        endTimeButton = (NonTouchTimePicker) findViewById(R.id.end_time_button);
    }
}
