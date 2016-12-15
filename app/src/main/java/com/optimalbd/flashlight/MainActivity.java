package com.optimalbd.flashlight;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends AppCompatActivity {

    ImageView onButton;
    ImageView muteImageView;

    private Camera camera;
    private boolean isFlashOn = false;
    private boolean hasFlash;
    private boolean hasFlashLight;
    boolean isSound = true;
    Camera.Parameters params;
    MediaPlayer mp;
    Context context;
    final public int CAMERA_PERM = 0;
    private TextView batteryTextView;
    ProgressBar levelPB;
    AdView mAdView;

    ConnectionDetection detection;
    Boolean isInternetConnection = false;

     Tracker mTracker;
    GoogleAnalyticsApplication application;


    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {

            int level = intent.getIntExtra("level", 0);
            levelPB.setProgress(level);
            batteryTextView.setText(String.valueOf(level) + "%");

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        onButton = (ImageView) findViewById(R.id.onButton);
        batteryTextView = (TextView) findViewById(R.id.batteryTextView);
        muteImageView = (ImageView) findViewById(R.id.muteImageView);
        levelPB = (ProgressBar) findViewById(R.id.levelPB);
         mAdView = (AdView) findViewById(R.id.adView);

        detection = new ConnectionDetection(context);
        isInternetConnection = detection.isConnect();

        application = (GoogleAnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        this.registerReceiver(this.mBatInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));


        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);

        hasFlashLight = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        onButton.setOnClickListener(new FlashOnOffListener());


        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERM);
        } else {
            if (hasFlash) {
                camera = Camera.open();
                params = camera.getParameters();
                flashOnOff();
            } else {
                showNoFlashAlert();
            }
        }

        muteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSound) {
                    isSound = false;
                    muteImageView.setImageResource(R.drawable.sounddeactive);
                } else {
                    isSound = true;
                    muteImageView.setImageResource(R.drawable.soundactive);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isInternetConnection) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }else {
            mAdView.setVisibility(View.GONE);
        }

        mTracker.setScreenName("Main Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    private class FlashOnOffListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            flashOnOff();

        }

    }

    private void flashOnOff() {
        if (!isFlashOn) {
            onButton.setImageResource(R.drawable.active);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            if (isSound) {
                playSound();
            }
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

        } else {
            onButton.setImageResource(R.drawable.deactive);
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            if (isSound) {
                playSound();
            }
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERM:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted for Flash Light", Toast.LENGTH_SHORT).show();
                    if (hasFlash) {
                        camera = Camera.open();
                        params = camera.getParameters();
                        flashOnOff();
                    } else {
                        showNoFlashAlert();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied \n please Enable your Camera permission !", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Playing sound
    // will play button toggle sound on flash on / off
    private void playSound() {
        if (isFlashOn) {
            mp = MediaPlayer.create(MainActivity.this, R.raw.sound);
        } else {
            mp = MediaPlayer.create(MainActivity.this, R.raw.sound);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }

    private void showNoFlashAlert() {
        new AlertDialog.Builder(this)
                .setMessage("Your device hardware does not support flashlight!")
                .setIcon(android.R.drawable.ic_dialog_alert).setTitle("Error")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        super.onDestroy();
        if (mBatInfoReceiver != null) {
            unregisterReceiver(mBatInfoReceiver);
        }
    }
}