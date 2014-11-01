package com.ptrprograms.seizuredetector;

import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by paulruiz on 11/1/14.
 */
public class SeizureService extends Service implements ShakeDetector.Listener {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start( sensorManager );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void hearShake() {

        Toast.makeText(this, "Shaking!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( getApplicationContext(), HeartRateService.class );
        startService( intent );
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
