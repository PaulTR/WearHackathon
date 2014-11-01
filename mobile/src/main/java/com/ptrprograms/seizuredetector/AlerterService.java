package com.ptrprograms.seizuredetector;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Geocoder;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;

/**
 * Created by paulruiz on 11/1/14.
 */
public class AlerterService extends WearableListenerService implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    public static final String MESSAGE_PATH_SEIZURE = "message_path_seizure";
    private LocationClient mLocationClient;
    private LatLng mCurrentLocation;


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if( messageEvent.getPath().equalsIgnoreCase( MESSAGE_PATH_SEIZURE ) ) {
            Toast.makeText( getApplicationContext(), "Message received", Toast.LENGTH_SHORT ).show();
            if( mLocationClient == null )
                mLocationClient = new LocationClient( this, this, this );
            if( !mLocationClient.isConnected() && !mLocationClient.isConnecting() ) {
                mLocationClient.connect();
            } else {
                getLocation();
            }
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

    private void getLocation() {
        if( mCurrentLocation == null ) {
            mCurrentLocation = new LatLng( mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude() );
        }

        Geocoder geocoder = new Geocoder( getApplicationContext() );
        String address;
        try {
            address = geocoder.getFromLocation( mCurrentLocation.latitude, mCurrentLocation.longitude, 1 ).get( 0 ).getAddressLine( 0 );
        } catch( IOException e ) {
            address = "";
        }

        sendText( address );
    }

    private void sendText( String address ) {
        PendingIntent sIntent = PendingIntent.getBroadcast(
                this, 0, new Intent( "com.ptrprograms.seizuredetector.SEND" ), 0);
        PendingIntent dIntent = PendingIntent.getBroadcast(
                this, 0, new Intent("com.ptrprograms.seizuredetector.DELIVERED"), 0);
        //Monitor status of the operation
        //Send the message
        String message = "Help, I'm having a seizure! My name is Paul Trebilcox-Ruiz.";
        if( !TextUtils.isEmpty( address ) ) {
            message += " I'm at " + address + ".";
        }
        message += " Please send medical assistance.";
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage("+15599608922", null, message,
                sIntent, dIntent);

    }

    @Override
    public void onConnected(Bundle bundle) {
        getLocation();
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
