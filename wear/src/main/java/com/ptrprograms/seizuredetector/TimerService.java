package com.ptrprograms.seizuredetector;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by paulruiz on 11/1/14.
 */
public class TimerService extends IntentService {

    public static final String ACTION_REMOVE_TIMER = "action_remove_timer";
    public static final String ACTION_SEND_HELP = "action_send_help";
    public static final String ACTION_SHOW_ALARM = "action_show_alarm";

    public TimerService() {
        super( "TimerService" );
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        if( ACTION_SHOW_ALARM.equals( action ) ) {
            showAlarm();
        } else if( ACTION_REMOVE_TIMER.equals( action ) ) {
            removeAlarm();
        } else if( ACTION_SEND_HELP.equals( action ) ) {
            removeAlarm();
            sendHelp();
        }
    }

    private void sendHelp() {
        Intent intent = new Intent( getApplicationContext(), HelpIsOnTheWayActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
    }

    private void showAlarm() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel( 1 );
        notificationManager.notify( 1, buildNotification( 10000 ) );//10 seconds
        registerAlarmManager( 10000 );
        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 0, 10000 };
        v.vibrate( pattern, -1 );
    }

    private void removeAlarm() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from( this );
        notificationManager.cancel( 1 );

        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );

        Intent intent = new Intent( ACTION_SEND_HELP, null, this, TimerService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        alarmManager.cancel(pendingIntent);
    }

    private Notification buildNotification( long duration ) {
        Intent removeIntent = new Intent( ACTION_REMOVE_TIMER, null, this, TimerService.class );
        PendingIntent pendingRemoveIntent = PendingIntent.getService( this, 0, removeIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        return new NotificationCompat.Builder( this )
                .setSmallIcon( R.drawable.ic_launcher )
                .setContentTitle( "Seizure?!" )
                .setContentText(TimeUtil.getTimeString( duration ) )
                .setUsesChronometer( true )
                .setLargeIcon( BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher) )
                .setWhen( System.currentTimeMillis() + duration )
                .addAction( R.drawable.ic_launcher, "Remove Timer", pendingRemoveIntent )
                .setDeleteIntent( pendingRemoveIntent )
                .setLocalOnly( true )
                .build();
    }

    private void registerAlarmManager( long duration ) {
        AlarmManager alarmManager = (AlarmManager) getSystemService( Context.ALARM_SERVICE );
        Intent intent = new Intent( ACTION_SEND_HELP, null, this, TimerService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        long time = System.currentTimeMillis() + duration;
        alarmManager.setExact( AlarmManager.RTC_WAKEUP, time, pendingIntent );
    }

}
