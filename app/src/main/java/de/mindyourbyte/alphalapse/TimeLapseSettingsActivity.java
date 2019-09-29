package de.mindyourbyte.alphalapse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    CameraEx camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse_settings);
        startTimePicker = (NonTouchTimePicker) findViewById(R.id.start_time_button);
        endTimePicker = (NonTouchTimePicker) findViewById(R.id.end_time_button);
        intervalTimePicker = (NonTouchTimePicker) findViewById(R.id.interval_picker);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this::onClick);

        startTimePicker.setCallback(picker -> {
            picker.saveChanges(getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.start_time_settings_key));
        });
        endTimePicker.setCallback(picker -> {
            picker.saveChanges(getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.end_time_settings_key));
        });
        intervalTimePicker.setCallback(picker -> {
            picker.saveChanges(getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.interval_time_settings_key));
        });
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE);
        startTimePicker.loadData(sharedPreferences, getString(R.string.start_time_settings_key));
        endTimePicker.loadData(sharedPreferences, getString(R.string.end_time_settings_key));
        intervalTimePicker.loadData(sharedPreferences, getString(R.string.interval_time_settings_key));
    }

    private void onClick(View view) {
        if (timer != null)
        {
            setAutoPowerOffMode(true);
            startButton.setText(getString(R.string.start_text));
            timer.cancel();
            timer = null;
            if (camera != null)
            {
                camera.release();
                camera = null;
            }
        }
        else{
            setAutoPowerOffMode(false);
            startButton.setText(getString(R.string.stop_text));
            timer = new Timer();
            if(camera == null) {
                Logger.info("Starting camera");
                camera = CameraEx.open(0, null);
            }
            scheduleAutoFocus(0);
            timer.schedule(new TakeShotTimerTask(), intervalTimePicker.getMillisecondsInterval());
            Logger.info(String.format("Scheduled shot in %d milliseconds.", intervalTimePicker.getMillisecondsInterval()));
        }

    }

    private void scheduleAutoFocus(long difference) {
        if (intervalTimePicker.getMillisecondsInterval() - difference > 4000){
            long nextInterval = intervalTimePicker.getMillisecondsInterval() - difference - 4000;
            Logger.info(String.format("Scheduled autofocus in %d milliseconds.", nextInterval));
            timer.schedule(new AutoFocusTimerTask(), nextInterval);
        }
    }

    private class TakeShotTimerTask extends TimerTask {
        @Override
        public void run() {
            Logger.info("taking shot");
            Calendar now = Calendar.getInstance();
            if (camera!= null) {
                camera.getNormalCamera().takePicture(() -> {
                    Logger.info("took shot");
                    camera.cancelTakePicture();
                    try {
                        camera.getNormalCamera().cancelAutoFocus();
                    } catch (Exception e) {
                        Logger.exception(e);
                    }
                    Calendar onShotTime = Calendar.getInstance();
                    long difference = onShotTime.getTimeInMillis() - now.getTimeInMillis();
                    scheduleAutoFocus(difference);
                    timer.schedule(new TakeShotTimerTask(), intervalTimePicker.getMillisecondsInterval() - difference);
                    Logger.info("rescheduled shot");
                }, null, null);
            }
        }
    }

    private class AutoFocusTimerTask extends TimerTask {
        @Override
        public void run() {
            if (camera != null){
                Logger.info("autofocus");
                try{
                    camera.getNormalCamera().autoFocus((b, camera) -> Logger.info("Auto focus is: " + b));
                }catch (RuntimeException exc){
                    Logger.exception(exc);
                }
            }
        }
    }
}
