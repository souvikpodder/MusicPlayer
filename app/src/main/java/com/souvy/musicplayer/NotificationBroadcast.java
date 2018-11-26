package com.souvy.musicplayer;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import static com.souvy.musicplayer.CommonMediaPlayer.*;

import static com.souvy.musicplayer.NotificationGenerator.NOTIFICATION_ID_CUSTOM_BIG;

/**
 * Created by delaroy on 6/4/17.
 */
public class NotificationBroadcast extends BroadcastReceiver {

    NotificationManager nm;
    NotificationCompat nc;

    @SuppressLint("RestrictedApi")
    @Override
    public  void onReceive(Context context, Intent intent){
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (intent.getAction().equals(NotificationGenerator.NOTIFY_PAUSE)){
            playpusechange=true;
            if(firstBackground&&play.isRunninBackGround) {
                play.countDownTimer.start();
                firstBackground=false;
            }
            if(mp.isPlaying()){
                mp.pause();

            }else {
                mp.start();

            }
        }else if (intent.getAction().equals(NotificationGenerator.NOTIFY_NEXT)){
            if(firstBackground&&play.isRunninBackGround) {
                play.countDownTimer.start();
                firstBackground=false;
            }
            nextsongchanger=true;

        }else if (intent.getAction().equals(NotificationGenerator.NOTIFY_DELETE)){

            nm.cancel(NOTIFICATION_ID_CUSTOM_BIG);
        }else if (intent.getAction().equals(NotificationGenerator.NOTIFY_PREVIOUS)){
            if(firstBackground&&play.isRunninBackGround) {
                play.countDownTimer.start();
                firstBackground=false;
            }
            presongchanger=true;
        }
    }

}