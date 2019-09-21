package de.mindyourbyte.alphalapse;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class TimeLapseSettingsActivity extends BaseActivity {

    Button startTimeButton;
    Button endTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse_settings);
        startTimeButton = (Button) findViewById(R.id.start_time_button);
        startTimeButton.setOnClickListener(new TimeButtonListener(startTimeButton));
        endTimeButton = (Button) findViewById(R.id.end_time_button);
        endTimeButton.setOnClickListener(new TimeButtonListener(endTimeButton));
    }

    private class TimeButtonListener implements View.OnClickListener {

        private Button targetButton;

        public TimeButtonListener(Button button)
        {
            targetButton = button;
        }

        @Override
        public void onClick(View view) {
            TimePickerFragment fragment = new TimePickerFragment();
            fragment.setCallback((timePicker, hour, minute) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                java.text.DateFormat format = DateFormat.getTimeFormat(TimeLapseSettingsActivity.this.getApplicationContext());
                String time = format.format(calendar.getTime());
                targetButton.setText(time);
            });
            fragment.show(TimeLapseSettingsActivity.this.getFragmentManager(), "StartTimePicker");
        }
    }
}
