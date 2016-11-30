package com.optimalbd.flashlight;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class PowerLED {
    Camera m_Camera;
    boolean m_isOn;

    public boolean getIsOn() {
        return this.m_isOn;
    }

    public PowerLED() {
        this.m_isOn = false;
        this.m_Camera = Camera.open();
    }

    public void turnOn() {
        if (!this.m_isOn) {
            this.m_isOn = true;
            try {
                Parameters mParameters = this.m_Camera.getParameters();
                mParameters.setFlashMode("torch");
                this.m_Camera.setParameters(mParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void turnOff() {
        if (this.m_isOn) {
            this.m_isOn = false;
            try {
                Parameters mParameters = this.m_Camera.getParameters();
                mParameters.setFlashMode("off");
                this.m_Camera.setParameters(mParameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Destroy() {
        this.m_Camera.release();
    }
}
