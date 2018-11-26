package com.souvy.musicplayer;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import static com.souvy.musicplayer.CommonMediaPlayer.bp;
import static com.souvy.musicplayer.CommonMediaPlayer.mp;


public class NotificationGenerator{
    public static final String NOTIFY_PREVIOUS = "com.souvy.musicplayer.previous";
    public static final String NOTIFY_DELETE = "com.souvy.musicplayer.delete";
    public static final String NOTIFY_PAUSE = "com.souvy.musicplayer.pause";

    public static final String NOTIFY_NEXT = "com.souvy.musicplayer.next";

    static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;
    static final int NOTIFICATION_ID_CUSTOM_BIG = 9;


    @SuppressLint("RestrictedApi")
    public static void customBigNotification(Context context,boolean playpause){
        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_notification);

        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notifyIntent = new Intent(context, MainActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);
        nc.setSmallIcon(R.drawable.smallplay);
        nc.setPriority(2);
        nc.setCustomContentView(expandedView);
        nc.setOngoing(true);
        System.out.println("Notification generator");
        System.out.println(context);

        nc.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        nc.getContentView().setTextViewText(R.id.songNameN, CommonMediaPlayer.songNameMP);
        nc.getContentView().setInt(R.id.mainBackGroundN,"setBackgroundColor",CommonMediaPlayer.color);
        nc.getContentView().setTextColor(R.id.songNameN,CommonMediaPlayer.titleColor);
        if(playpause){
            nc.getContentView().setImageViewResource(R.id.btnPauseN,R.drawable.smallplay);
        }else{
            nc.getContentView().setImageViewResource(R.id.btnPauseN,R.drawable.smallpause);
        }
        if(bp==null){
            nc.getContentView().setImageViewResource(R.id.imageViewAlbumArtN,R.drawable.album_art);
        }
        else{
            nc.getContentView().setImageViewBitmap(R.id.imageViewAlbumArtN,bp);
        }
        setListeners(expandedView, context);
        nm.notify(NOTIFICATION_ID_CUSTOM_BIG, nc.build());
    }

    private static void setListeners(RemoteViews view, Context context){
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);


        PendingIntent pPrevious = PendingIntent.getBroadcast(context, 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPreviousN, pPrevious);


        PendingIntent pDelete = PendingIntent.getBroadcast(context, 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);


        PendingIntent pPause = PendingIntent.getBroadcast(context, 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPauseN, pPause);


        PendingIntent pNext = PendingIntent.getBroadcast(context, 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNextN, pNext);


    }

}
