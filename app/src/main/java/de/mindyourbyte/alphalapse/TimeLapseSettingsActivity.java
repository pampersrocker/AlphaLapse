package de.mindyourbyte.alphalapse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.ma1co.openmemories.framework.DateTime;

import java.text.DateFormat;
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

        Settings settings = Settings.getInstance();
        settings.load(getApplicationContext());
        startTimePicker.loadData(settings.StartTime);
        endTimePicker.loadData(settings.EndTime);
        intervalTimePicker.loadData(settings.Interval);


        startTimePicker.setCallback(picker -> {
            picker.saveChanges(settings.StartTime);
            settings.save(getApplicationContext());
        });
        endTimePicker.setCallback(picker -> {
            picker.saveChanges(settings.EndTime);
            settings.save(getApplicationContext());
        });
        intervalTimePicker.setCallback(picker -> {
            picker.saveChanges(settings.Interval);
            settings.save(getApplicationContext());
        });
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (dateText != null){
                        DateFormat format = DateFormat.getTimeInstance(DateFormat.MEDIUM);
                        dateText.setText(format.format(DateTime.getInstance().getCurrentTime().getTime()));
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
