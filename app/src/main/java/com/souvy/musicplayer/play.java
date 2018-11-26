package com.souvy.musicplayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.souvy.musicplayer.CommonMediaPlayer.*;

public class play extends Activity implements View.OnClickListener {

    static boolean isRunninBackGround=false;
    int i, flag = 0, flag1 = 0, nextflag = 0, preflag = 0, shuffle11 = 0;
    int lengthSongPath, shufflesonglist = 0;
    int a[];
    String songNameString;
    ArrayList<String> songName;
    ArrayList<String> songPath;
    ArrayList<String> albumArtId;
    ImageView imageView;
    TextView tv;
    ImageButton playpause, repeat, shuffle, next, pre;
    SeekBar seekBar;
    Cursor cursor;
    Random r;
    LinearLayout linearLayout;
    TextView songTotalDurationLabel,songCurrentDurationLabel,albumNameTx;
    Context c;
    static CountDownTimer countDownTimer;
    MediaPlayer.OnCompletionListener amp = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            if (flag1 == 0) {

                System.out.println("\n\nnext song\n\n");
                nextSong();

            }
            else {
                System.out.println("\n\nrepeat\n\n");
                seekBar.setMax(mp.getDuration());
            }
            mp.setOnCompletionListener(amp);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Intent intent = getIntent();
        playpause = (ImageButton) findViewById(R.id.playPauseSong);
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        repeat = (ImageButton) findViewById(R.id.repeat);
        pre = (ImageButton) findViewById(R.id.preSong);
        next = (ImageButton) findViewById(R.id.NxtSong);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv = (TextView) findViewById(R.id.SongName);
        linearLayout= (LinearLayout) findViewById(R.id.playMainLayout);
        songCurrentDurationLabel= (TextView) findViewById(R.id.currentTime);
        songTotalDurationLabel=(TextView)findViewById(R.id.totalTime);
        albumNameTx= (TextView) findViewById(R.id.albumName);
        c=this;

        r = new Random();
        playpause.setOnClickListener(this);
        shuffle.setOnClickListener(this);
        repeat.setOnClickListener(this);
        next.setOnClickListener(this);
        pre.setOnClickListener(this);
        i = intent.getExtras().getInt("position");
        songName = intent.getStringArrayListExtra("songName");
        songPath = intent.getStringArrayListExtra("songPath");
        albumArtId = intent.getStringArrayListExtra("albumArtId");
        a = new int[songPath.size()];
        if (mp == null) {
            playM();
        } else {
            mp.stop();
            playM();

        }
        mp.setOnCompletionListener(amp);
        System.out.println("onCreat");
        countDownTimer=new CountDownTimer(Long.MAX_VALUE, 300) {

            public void onTick(long millisUntilFinished) {
                if(presongchanger){
                    preSong();
                }
                if(nextsongchanger){
                    nextSong();
                }
                playPsuseIconUpdate();
            }

            public void onFinish() {
                System.out.println("Finished");
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunninBackGround=true;
        firstBackground=true;
        countDownTimer.cancel();
    }

    public void playpause () {
        if (mp.isPlaying()) {
            mp.pause();
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);
            playpause.setImageResource(R.drawable.play);
        } else {
            mp.start();
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            playpause.setImageResource(R.drawable.pause);
        }
        NotificationGenerator.customBigNotification(c,!mp.isPlaying());
    }
    public void nextSong () {
        if (shuffle11 == 1) {
            if(mp.isPlaying())
                mp.stop();
            nextflag = 1;
            playnxtpre();
            create(getApplicationContext(), Uri.parse(songPath.get(a[shufflesonglist])));
            songNameString = songName.get(a[shufflesonglist]);
            tv.setText(songNameString);
            tv.setSelected(true);
            getPic();
            seekBar.setMax(mp.getDuration());
            playpause.setImageResource(R.drawable.pause);
            mp.start();


        } else {
            if(mp.isPlaying())
                mp.stop();
            nextflag = 1;
            playnxtpre();
            create(getApplicationContext(), Uri.parse(songPath.get(i)));
            songNameString = songName.get(i);
            tv.setText(songNameString);
            tv.setSelected(true);
            getPic();
            seekBar.setMax(mp.getDuration());
            playpause.setImageResource(R.drawable.pause);
            mp.start();
        }
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        nextsongchanger =false;
        mp.setOnCompletionListener(amp);
    }



    public void preSong () {

        if (shuffle11 == 1) {
            if(mp.isPlaying())
                mp.stop();
            preflag = 1;
            playnxtpre();
            create(getApplicationContext(), Uri.parse(songPath.get(a[shufflesonglist])));
            songNameString = songName.get(a[shufflesonglist]);
            tv.setText(songNameString);
            tv.setSelected(true);
            getPic();
            seekBar.setMax(mp.getDuration());
            playpause.setImageResource(R.drawable.pause);
            mp.start();
        } else {
            if(mp.isPlaying())
                mp.stop();
            preflag = 1;
            playnxtpre();
            create(getApplicationContext(), Uri.parse(songPath.get(i)));
            songNameString = songName.get(i);
            tv.setText(songNameString);
            tv.setSelected(true);
            getPic();
            seekBar.setMax(mp.getDuration());
            playpause.setImageResource(R.drawable.pause);
            mp.start();
        }
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        presongchanger=false;
        mp.setOnCompletionListener(amp);
    }

    public void playPsuseIconUpdate(){
        if(mp.isPlaying()){
            if(playpusechange) {
                NotificationGenerator.customBigNotification(c, false);
                playpusechange=false;
            }
            playpause.setImageResource(R.drawable.pause);
        }else{
            if(playpusechange) {
                NotificationGenerator.customBigNotification(c, true);
                playpusechange=false;
            }
            playpause.setImageResource(R.drawable.play);
        }
        playpusechange=false;
    }

    public void getPic () {
        CommonMediaPlayer.songNameMP=songNameString;
        imageView = (ImageView) findViewById(R.id.imageView);
        if (shuffle11 == 1) {
            cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART,MediaStore.Audio.Albums.ALBUM},
                    MediaStore.Audio.Albums._ID + "=?",
                    new String[]{albumArtId.get(a[shufflesonglist])},
                    null);
        } else {
            cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART,MediaStore.Audio.Albums.ALBUM},
                    MediaStore.Audio.Albums._ID + "=?",
                    new String[]{albumArtId.get(i)},
                    null);

        }

        if (cursor.moveToFirst()) {

            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            final String albumName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            Bitmap bp = BitmapFactory.decodeFile(path);


            albumNameTx.setText(albumName);
            if(bp==null){
                bp=BitmapFactory.decodeResource(getResources(),R.drawable.album_art);
            }
            imageView.setImageBitmap(bp);
            Palette.from(bp).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getDarkVibrantSwatch();
                    if (swatch == null) swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                    if (swatch != null) {
                        // Set the background color of the player bar based on the swatch color
                        linearLayout.setBackgroundColor(swatch.getRgb());

                        // Update the track's title with the proper title text color
                        tv.setTextColor(swatch.getBodyTextColor());
                        titleColor=swatch.getBodyTextColor();
                        color=swatch.getRgb();
                        // Update the artist name with the proper body text color
                        albumNameTx.setTextColor(swatch.getTitleTextColor());
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(swatch.getRgb());

                    }
                }
            });

            CommonMediaPlayer.bp=bp;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                   NotificationGenerator.customBigNotification(c,false);

                }
            }, 400);

        }
    }

    private Handler mSeekbarUpdateHandler = new Handler();
    private Runnable mUpdateSeekbar = new Runnable() {
        @Override
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText(""+ String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(totalDuration),
                    TimeUnit.MILLISECONDS.toSeconds(totalDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalDuration))
            ));
            // Displaying time completed playing
            songCurrentDurationLabel.setText(""+ String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(currentDuration),
                    TimeUnit.MILLISECONDS.toSeconds(currentDuration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentDuration))
            ));

            seekBar.setProgress(mp.getCurrentPosition());
            mSeekbarUpdateHandler.postDelayed(this, 50);
        }
    };
    public void onShuffle () {
        if (shuffle11 == 0) {
            shuffle.setImageResource(R.drawable.shuffleon);
            shuffle11 = 1;
            playnxtpre();
            flag = 1;
        } else {
            shuffle.setImageResource(R.drawable.shuffle);
            shuffle11 = 0;
        }
    }
    public void onRepeat () {
        if (flag1 == 0) {
            mp.setLooping(true);
            repeat.setImageResource(R.drawable.repeaton);
            flag1 = 1;
        } else {
            mp.setLooping(false);
            repeat.setImageResource(R.drawable.repeat);
            flag1 = 0;
        }
        mp.setOnCompletionListener(amp);
    }
    public void playnxtpre () {
        lengthSongPath = songPath.size();
        if (shuffle11 == 0) {
            if (nextflag == 1) {
                i = i + 1;
                if (i > songPath.size() - 1) {
                    i = 0;
                }
                nextflag = 0;
            }
            if (preflag == 1) {
                i = i - 1;
                if (i < 0) {
                    i = songPath.size() - 1;
                }
                preflag = 0;
            }
        } else {
            if (flag == 0) {
                for (int j = 1; j < lengthSongPath; j++) {
                    a[j] = r.nextInt(lengthSongPath - 1);
                }

            } else {
                if (nextflag == 1) {
                    ++shufflesonglist;
                    if (shufflesonglist > a.length - 1) {
                        shufflesonglist = 0;
                    }
                    nextflag = 0;
                }
                if (preflag == 1) {
                    --shufflesonglist;
                    if (shufflesonglist < 0) {
                        shufflesonglist = a.length - 1;
                    }
                    preflag = 0;
                }
            }
        }
    }

    public void playM () {
        create(getApplicationContext(), Uri.parse(songPath.get(i)));
        songNameString = songName.get(i);
        seekBar.setMax(mp.getDuration());
        getPic();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    mp.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
            }
        });
        tv.setText(songNameString);
        tv.setSelected(true);
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar, 0);
        playpause.setImageResource(R.drawable.pause);
        mp.start();
    }

    @Override
    public void onClick (View view){
        int id = view.getId();
        switch (id) {
            case R.id.playPauseSong:
                playpause();
                break;
            case R.id.NxtSong:
                nextSong();
                break;
            case R.id.preSong:
                preSong();
                break;
            case R.id.shuffle:
                onShuffle();
                break;
            case R.id.repeat:
                onRepeat();
        }
    }

}

