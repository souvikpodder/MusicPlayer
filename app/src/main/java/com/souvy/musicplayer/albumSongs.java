package com.souvy.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class albumSongs extends AppCompatActivity {
    ArrayList<String> songName,songPath,albumId;
    ImageView imageViewAlbumArt;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_songs);
        Intent intent=getIntent();
        lv= (ListView) findViewById(R.id.listViewAlbumSong);
        songName=intent.getStringArrayListExtra("songName");
        songPath=intent.getStringArrayListExtra("songPath");
        albumId=intent.getStringArrayListExtra("albumId");


        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(),R.layout.white_text_listview_item,songName);

        lv.setAdapter(adapter);

        imageViewAlbumArt= (ImageView) findViewById(R.id.imageViewAlbumSong);
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {albumId.get(0)},
                null);

        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            Bitmap bp = BitmapFactory.decodeFile(path);
            if(bp==null){
                imageViewAlbumArt.setImageResource(R.drawable.album_art);
            }else {
                imageViewAlbumArt.setImageBitmap(bp);
            }
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), play.class);
                intent.putStringArrayListExtra("songName",songName);
                intent.putStringArrayListExtra("songPath",songPath);
                intent.putStringArrayListExtra("albumArtId",albumId);
                intent.putExtra("position",position);
                startActivity(intent);
                CommonMediaPlayer.songNameMP=songName.get(position);

            }
        });
    }


}
