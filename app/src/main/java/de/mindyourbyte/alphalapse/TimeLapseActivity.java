package de.mindyourbyte.alphalapse;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.TextView;

import com.github.ma1co.openmemories.framework.DateTime;
import com.sony.scalar.hardware.CameraEx;
import com.sony.scalar.hardware.avio.DisplayManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimeLapseActivity extends BaseActivity {
    Button stopButton;
    Timer timer;
    CameraEx camera;
    long intervalTime;
    TextView nextShotText;
    Calendar nextShot;

    ActiveDisplay activeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timelapse_activity);
        stopButton = (Button) findViewById(R.id.stop_button);
        nextShotText = (TextView) findViewById(R.id.time_left_text);
        intervalTime = NonTouchTimePicker.loadIntervalFromPreferences(getSharedPreferences(
                getString(R.string.settings_key), Context.MODE_PRIVATE),
                getString(R.string.interval_time_settings_key));
        stopButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null)
        {
            stopTimeLapse();
        }
        startTimeLapse();
    }

    private void startTimeLapse() {
        setAutoPowerOffMode(false);
        activeDisplay = new ActiveDisplay(displayManager);
        activeDisplay.setAutoTurnOffTime(5000);
        timer = new Timer();
        if(camera == null) {
            Logger.info("Starting camera");
            camera = CameraEx.open(0, null);
        }
        scheduleAutoFocus(0);
        calculateNextShotTime(0);
        long delay = Math.max(0, nextShot.getTimeInMillis() - DateTime.getInstance().getCurrentTime().getTimeInMillis());
        timer.schedule(new TakeShotTimerTask(), delay);
        Logger.info(String.format("Scheduled shot in %d milliseconds.", intervalTime));
        timer.schedule(new UpdateTimerTask(), 1000, 1000);

    }

    @Override
    protected void onAnyKeyDown() {
        super.onAnyKeyDown();
        if (activeDisplay != null){
            activeDisplay.turnOn();
        }
    }

    @Override
    protected void onPause() {
        if (timer != null)
        {
            stopTimeLapse();
        }
        super.onPause();
    }

    private void stopTimeLapse() {
        activeDisplay.setAutoTurnOffTime(ActiveDisplay.NO_AUTO_TURN_OFF);
        activeDisplay.turnOn();
        setAutoPowerOffMode(true);
        timer.cancel();
        timer = null;
        if (camera != null)
        {
            camera.release();
            camera = null;
        }
    }

    private void scheduleAutoFocus(long difference) {
        if (intervalTime - difference > 4000){
            long nextInterval = intervalTime - difference - 4000;
            Logger.info(String.format("Scheduled autofocus in %d milliseconds.", nextInterval));
            timer.schedule(new AutoFocusTimerTask(), nextInterval);
        }
    }

    private class StartEndDates
    {
        public StartEndDates(Calendar start, Calendar end){
            startTime = start;
            endTime = end;
        }

        public Calendar startTime;
        public Calendar endTime;

        private boolean isInTimeFrame(Calendar now)
        {
            if (Math.abs(startTime.getTimeInMillis() - endTime.getTimeInMillis()) < 1000)
            {
                return true;
            }
            return startTime.before(now) && endTime.after(now);
        }
    }



    private StartEndDates getStartEndDates()
    {
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());

        Calendar startTime = NonTouchTimePicker.loadTimeFromPreferences(getApplicationContext(), getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.start_time_settings_key));
        Calendar endTime = NonTouchTimePicker.loadTimeFromPreferences(getApplicationContext(), getSharedPreferences(getString(R.string.settings_key), Context.MODE_PRIVATE), getString(R.string.end_time_settings_key));
        Logger.info("Start time is " + dateFormat.format(startTime.getTime()));
        Logger.info("End time is " + dateFormat.format(endTime.getTime()));
        if (startTime.compareTo(endTime) > 0){
            Calendar temp = startTime;
            startTime = endTime;
            endTime = temp;
            endTime.add(Calendar.DAY_OF_MONTH, 1);
            Logger.info("Swapped times and set end time to " + dateFormat.format(endTime.getTime()));

        }
        return new StartEndDates(startTime, endTime);
    }

    private class UpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            Calendar now = DateTime.getInstance().getCurrentTime();
            long difference = nextShot.getTimeInMillis() - now.getTimeInMillis();
            int hours = Math.max(0,(int)(difference/(1000 * 60 * 60)));
            int minutes = Math.max(0,(int)((difference/(1000 * 60)) % 60));
            int seconds = Math.max(0,(int)((difference/1000) % 60));
            String timeLeft = String.format(getString(R.string.time_left), hours, minutes, seconds);
            runOnUiThread(() -> {
                if (nextShotText != null) {
                    nextShotText.setText(String.format(getString(R.string.next_picture_in_s), timeLeft));
                }
            });

        }
    }

    private class TakeShotTimerTask extends TimerTask {
        @Override
        public void run() {
            Logger.info("taking shot");
            Calendar now = DateTime.getInstance().getCurrentTime();
            if (camera!= null) {
                camera.getNormalCamera().takePicture(() -> {
                    Logger.info("took shot");
                    camera.cancelTakePicture();
                    try {
                        camera.getNormalCamera().cancelAutoFocus();
                    } catch (Exception e) {
                        Logger.exception(e);
                    }
                    Calendar onShotTime = DateTime.getInstance().getCurrentTime();
                    long difference = onShotTime.getTimeInMillis() - now.getTimeInMillis();
                    calculateNextShotTime(difference);
                    scheduleAutoFocus(difference);
                    long delay = Math.max(0, nextShot.getTimeInMillis() - DateTime.getInstance().getCurrentTime().getTimeInMillis());
                    timer.schedule(new TakeShotTimerTask(), delay);
                    Logger.info("rescheduled shot");
                }, null, null);
            }
        }
    }

    private void calculateNextShotTime(long difference) {
        nextShot = DateTime.getInstance().getCurrentTime();
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        Logger.info("Now is " + dateFormat.format(nextShot.getTime()));

        nextShot.setTimeInMillis(nextShot.getTimeInMillis() + (intervalTime - difference));
        StartEndDates dates = getStartEndDates();
        if (!dates.isInTimeFrame(nextShot)){
            Logger.info("Next shot is not in time frame " + dateFormat.format(nextShot.getTime()));

            if (nextShot.compareTo(dates.startTime) > 0)
            {
                Logger.info("Add day because start time is on next day " + dateFormat.format(dates.startTime.getTime()));

                dates.startTime.add(Calendar.DAY_OF_MONTH, 1);
            }
            nextShot = dates.startTime;
        }
        Logger.info("Set next sho to " + dateFormat.format(nextShot.getTime()));

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
