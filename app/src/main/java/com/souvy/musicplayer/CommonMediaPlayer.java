package com.souvy.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;

public class CommonMediaPlayer {
    public static MediaPlayer mp;
    public static String songNameMP;
    public static Bitmap bp;
    public static int titleColor;
    public static int color;
    static boolean presongchanger=false;
    static boolean nextsongchanger =false;
    static boolean playpusechange =false;
    static boolean firstBackground=false;
    public static void create(Context cx,Uri path){
            mp=MediaPlayer.create(cx,path);
    }

}
