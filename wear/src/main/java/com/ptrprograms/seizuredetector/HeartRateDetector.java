package com.ptrprograms.seizuredetector;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by paulruiz on 11/1/14.
 */
public class HeartRateDetector implements SensorEventListener {

    private Listener listener;
    private SensorManager mSensorManager;
    private Sensor mHeartRateSensor;

    public interface Listener {
        void heartRate(int heartRate);
    }

    public HeartRateDetector(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if ( event.sensor.getType() == Sensor.TYPE_HEART_RATE ) {
            if ( (int) event.values[0] > 0 ) {
                listener.heartRate( (int) event.values[0] );
            }
        }
    }

    public boolean start( SensorManager manager ) {
        this.mSensorManager = manager;
        mHeartRateSensor = mSensorManager.getDefaultSensor( Sensor.TYPE_HEART_RATE );

        if ( mHeartRateSensor != null ) {
            mSensorManager.registerListener( this, mHeartRateSensor, SensorManager.SENSOR_DELAY_FASTEST );
        }

        return mHeartRateSensor != null;
    }

    public void stop() {

        if ( mHeartRateSensor != null ) {
            mSensorManager.unregisterListener( this, mHeartRateSensor );
            listener = null;
            mSensorManager = null;
            mHeartRateSensor = null;
        }
    }

    @Override
    public void onAccuracyChanged( Sensor sensor, int accuracy ) {

    }
}
