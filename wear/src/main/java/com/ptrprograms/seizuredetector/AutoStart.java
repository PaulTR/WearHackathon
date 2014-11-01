package com.ptrprograms.seizuredetector;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by paulruiz on 11/1/14.
 */
public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent( context, SeizureService.class );
        context.startService( serviceIntent );
    }

}
