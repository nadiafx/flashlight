package com.pandamonium.lumens;

import com.pandamonium.lumens.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FlashLightActivity extends Activity {

    //TAG
    private static final String TAG = "FlashLightActivity";

    //flag to detect flash is on or off
    private boolean mIsLightOn = false;
    private Camera mCamera;
    private Button mPrimarySwitch;

    /**
     * onCreate
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set window features, no title, full screen, hidden nav
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //Set view
        setContentView(R.layout.main);

        //Instantiate the views
        mPrimarySwitch = (Button) findViewById(R.id.buttonFlashlight);

        PackageManager pm = this.getPackageManager();

        // Check if the camera feature is supported.
        //FIXME: This should be handled in the manifest layer as well so that people who can't use it can't download from play store.
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            return;
        }

        //Instantiate camera and get the parameters
        mCamera = Camera.open();
        final Parameters cameraParams = mCamera.getParameters();

        //Set up primary switch
        setUpPrimarySwitch(cameraParams);
    }

    /**
     * onStop
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (mCamera != null) {
            mCamera.release();
        }
    }

    /**
     * This method handles primary switch behavior
     */
    private void setUpPrimarySwitch(Parameters cameraParams) {
        //Add primary switch onClick behavior
        mPrimarySwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mIsLightOn) {
                    turnFlashOff();
                } else {
                    turnFlashOn();
                }
            }
        });
    }

    /**
     * Helper method to turn flash on
     */
    private void turnFlashOn() {
        Log.i(TAG, "Turned torch on");
        final Parameters cameraParams = mCamera.getParameters();
        cameraParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(cameraParams);
        mCamera.startPreview();
        mIsLightOn = true;
    }

    /**
     * Helper method to turn flash off
     */
    private void turnFlashOff() {
        Log.i(TAG, "Turned torch off");
        final Parameters cameraParams = mCamera.getParameters();
        cameraParams.setFlashMode(Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(cameraParams);
        mCamera.stopPreview();
        mIsLightOn = false;
    }
}