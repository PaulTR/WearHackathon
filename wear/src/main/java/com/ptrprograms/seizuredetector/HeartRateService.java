package com.ptrprograms.seizuredetector;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by paulruiz on 11/1/14.
 */
public class HeartRateService extends Service implements HeartRateDetector.Listener {

    private HeartRateDetector hrd;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        SensorManager sensorManager = (SensorManager) getSystemService( SENSOR_SERVICE );
        if( hrd == null )
            hrd = new HeartRateDetector( this );
        hrd.start( sensorManager );
        */
        Log.e( "Heart Rate", "onStartCommand" );

        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep( 3000 );
                } catch( InterruptedException e ) {
                }
                heartRate( 85 );
            }
        }).run();

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void heartRate(int heartRate) {

        Log.e( "HeartRateService", "heartrate: " + heartRate );
        Intent intent = new Intent( getApplicationContext(), TimerService.class );
        intent.setAction( TimerService.ACTION_SHOW_ALARM );
        startService( intent );

        if( hrd != null ) {
            hrd.stop();
            hrd = null;
        }
        stopSelf();

    }
}
