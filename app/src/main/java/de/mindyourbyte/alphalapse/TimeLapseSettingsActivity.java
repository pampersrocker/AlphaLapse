package de.mindyourbyte.alphalapse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.ma1co.openmemories.framework.DateTime;
import com.sony.scalar.hardware.CameraEx;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimeLapseSettingsActivity extends BaseActivity {

    NonTouchTimePicker startTimePicker;
    NonTouchTimePicker endTimePicker;
    NonTouchTimePicker intervalTimePicker;
    Button startButton;
    Timer timer;
    TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse_settings);
        startTimePicker = (NonTouchTimePicker) findViewById(R.id.start_time_button);
        endTimePicker = (NonTouchTimePicker) findViewById(R.id.end_time_button);
        intervalTimePicker = (NonTouchTimePicker) findViewById(R.id.interval_picker);
        startButton = (Button) findViewById(R.id.start_button);
        dateText = (TextView) findViewById(R.id.date_text);
        startButton.setOnClickListener(this::onClick);


        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE);
        startTimePicker.loadData(sharedPreferences, getString(R.string.start_time_settings_key));
        endTimePicker.loadData(sharedPreferences, getString(R.string.end_time_settings_key));
        intervalTimePicker.loadData(sharedPreferences, getString(R.string.interval_time_settings_key));


        startTimePicker.setCallback(picker -> {
            picker.saveChanges(getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.start_time_settings_key));
        });
        endTimePicker.setCallback(picker -> {
            picker.saveChanges(getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.end_time_settings_key));
        });
        intervalTimePicker.setCallback(picker -> {
            picker.saveChanges(getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.interval_time_settings_key));
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (dateText != null){
                        DateFormat format = new android.text.format.DateFormat();
                        dateText.setText(format.format("hh:mm:ss", DateTime.getInstance().getCurrentTime().getTime()));
                    }
                });
            }
        }, 1000, 1000);
    }

    private void onClick(View view) {
        Intent intent = new Intent(this, TimeLapseActivity.class);
        startActivity(intent);
    }
}
