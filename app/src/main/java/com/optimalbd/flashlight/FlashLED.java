package com.optimalbd.flashlight;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;

public class FlashLED {
    Camera camera;
    boolean on_off;
    MediaPlayer mp;

    public boolean getIsOn() {
        return this.on_off;
    }

    public FlashLED() {
        this.on_off = false;
        this.camera = Camera.open();
    }

    public void lightOn() {
        if (!this.on_off) {
            this.on_off = true;
            try {
                Parameters mParameters = this.camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(mParameters);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void lightOff() {
        if (this.on_off) {
            this.on_off = false;
            try {
                Parameters params = this.camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(params);
                camera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Destroy() {
        if (camera!=null){
            this.camera.release();
        }

    }

    public void playSound(Context context) {
        if (on_off) {
            mp = MediaPlayer.create(context, R.raw.turnon);
        } else {
            mp = MediaPlayer.create(context, R.raw.turnon);
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
}
