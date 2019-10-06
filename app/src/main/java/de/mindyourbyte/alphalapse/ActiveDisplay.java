package de.mindyourbyte.alphalapse;


import android.os.Handler;

import com.github.ma1co.openmemories.framework.DisplayManager;

public class ActiveDisplay {

    private DisplayManager displayManager;
    private DisplayManager.Display defaultDisplay;
    public static final int NO_AUTO_TURN_OFF = -1;

    private int autoTurnOffTime = NO_AUTO_TURN_OFF;

    Handler handler = new Handler();
    Runnable turnOffRunnable = new Runnable() {
        @Override
        public void run() {
            turnOff();
        }
    };

    public ActiveDisplay(DisplayManager manager){
        displayManager = manager;
        defaultDisplay = displayManager.getActiveDisplay();
        if (defaultDisplay == DisplayManager.Display.NONE){
            defaultDisplay = DisplayManager.Display.SCREEN;
        }
    }

    public void turnOn(){
        displayManager.setActiveDisplay(defaultDisplay);

        scheduleAutoTurnOff();
    }

    public void turnOff() {
        displayManager.setActiveDisplay(DisplayManager.Display.NONE);
    }

    public void setAutoTurnOffTime(int milliseconds){
        autoTurnOffTime = milliseconds;
        if (autoTurnOffTime <= NO_AUTO_TURN_OFF){
            autoTurnOffTime = NO_AUTO_TURN_OFF;
        }

        scheduleAutoTurnOff();
    }

    private void scheduleAutoTurnOff() {
        if (autoTurnOffTime > NO_AUTO_TURN_OFF){
            handler.postDelayed(turnOffRunnable, autoTurnOffTime);
        }
    }
}
