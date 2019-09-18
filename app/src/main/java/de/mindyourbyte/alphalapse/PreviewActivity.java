package de.mindyourbyte.alphalapse;

import android.os.Bundle;


import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.github.ma1co.openmemories.framework.DisplayManager;
import com.sony.scalar.hardware.CameraEx;

public class PreviewActivity extends BaseActivity implements SurfaceHolder.Callback, CameraEx.ShutterSpeedChangeListener, CameraEx.ApertureChangeListener, CameraEx.AutoISOSensitivityListener {

    SurfaceHolder surfaceHolder;
    CameraEx camera;
    private DisplayManager displayManager;
    TextView shutterText;
    TextView apertureText;
    TextView isoText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_preview);
            shutterText = (TextView) findViewById(R.id.shutterSpeed);
            apertureText = (TextView) findViewById(R.id.fStop);
            isoText = (TextView) findViewById(R.id.iso);
            SurfaceView cameraView = (SurfaceView)findViewById(R.id.cameraSurface);
            surfaceHolder = cameraView.getHolder();
            surfaceHolder.addCallback(this);
        } catch (Throwable e) {
            Logger.exception(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            displayManager = DisplayManager.create(this);
            setColorDepth(true);

            camera = CameraEx.open(0, null);
            surfaceHolder.addCallback(this);
            camera.setShutterSpeedChangeListener(this);
            camera.setApertureChangeListener(this);
            camera.setAutoISOSensitivityListener(this);
        } catch (Throwable e) {
            Logger.exception(e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            camera.setShutterSpeedChangeListener(null);
            camera.setApertureChangeListener(null);
            camera.setAutoISOSensitivityListener(null);
            camera.release();
            camera = null;
            surfaceHolder.removeCallback(this);
            setColorDepth(false);

            displayManager.release();
            displayManager = null;
        } catch (Throwable e) {
            Logger.exception(e);
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.getNormalCamera().setPreviewDisplay(surfaceHolder);
            camera.getNormalCamera().startPreview();


        } catch (Throwable e) {
            Logger.exception(e);
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    protected void setColorDepth(boolean highQuality) {
    }

    @Override
    protected boolean onFocusKeyDown() {
        if (camera != null){
            camera.getNormalCamera().autoFocus(null);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onFocusKeyUp() {
        if(camera!= null){
            camera.getNormalCamera().cancelAutoFocus();
            return true;
        }
        return false;
    }

    @Override
    protected boolean onShutterKeyDown() {
        if (camera != null){
            camera.getNormalCamera().takePicture(null,null,null);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onShutterKeyUp() {
        if (camera != null){
            camera.cancelTakePicture();
            return true;
        }
        return false;
    }


    @Override
    protected boolean onLowerDialChanged(int value) {
        if (camera != null){
            camera.adjustShutterSpeed(value);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onUpperDialChanged(int value) {
        if (camera != null){
            camera.adjustAperture(value);
            return true;
        }
        return false;
    }

    @Override
    public void onShutterSpeedChange(CameraEx.ShutterSpeedInfo shutterSpeedInfo, CameraEx cameraEx) {
        String shutterSpeed;
        float timeInSeconds = (float)shutterSpeedInfo.currentShutterSpeed_n / (float)shutterSpeedInfo.currentShutterSpeed_d;
        // Identifier for BULB mode
        if (shutterSpeedInfo.currentShutterSpeed_n == 65535)
        {
            shutterSpeed = getString(R.string.bulb);
        }
        else if (timeInSeconds <= 0.3f){
            shutterSpeed = String.format("%d/%d", shutterSpeedInfo.currentShutterSpeed_n, shutterSpeedInfo.currentShutterSpeed_d);
        }
        else if (timeInSeconds == Math.round(timeInSeconds))
        {
            shutterSpeed = String.format("%d\"", (int)timeInSeconds);
        }
        else
        {
            shutterSpeed = String.format("%.1f\"", timeInSeconds);
        }
        Logger.info("Shutter changed to " + shutterSpeed);

        shutterText.setText(shutterSpeed);
    }

    @Override
    public void onApertureChange(CameraEx.ApertureInfo apertureInfo, CameraEx cameraEx) {
        float apertureRatio = apertureInfo.currentAperture / 100.0f;
        String aperture;
        if (apertureRatio != Math.round(apertureRatio))
        {
            aperture = String.format("f/%.1f", apertureRatio);
        }
        else
        {
            aperture = String.format("f/%d", apertureInfo.currentAperture / 100);
        }
        Logger.info("Aperture changed to " + aperture);

        apertureText.setText(aperture);
    }

    @Override
    public void onChanged(int i, CameraEx cameraEx) {
        String iso = String.format("ISO %d", i);
        Logger.info("ISO changed to " + iso);
        isoText.setText(iso);
    }
}