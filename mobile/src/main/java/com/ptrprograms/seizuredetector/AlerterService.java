package com.ptrprograms.seizuredetector;


import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by paulruiz on 11/1/14.
 */
public class AlerterService extends WearableListenerService {

    public static final String MESSAGE_PATH_SEIZURE = "message_path_seizure";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if( messageEvent.getPath().equalsIgnoreCase( MESSAGE_PATH_SEIZURE ) ) {
            Toast.makeText( getApplicationContext(), "Message received", Toast.LENGTH_SHORT ).show();
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}
