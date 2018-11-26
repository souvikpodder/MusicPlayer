package com.souvy.musicplayer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class artistSong extends AppCompatActivity {
    String artistName;
    String artistId;
    ListView lv;
    ImageView albumArt;
    ArrayList<String> songName,songPath,albumArtId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_song);
        Intent intent=getIntent();

        lv= (ListView) findViewById(R.id.listViewArtistSong);
        albumArt= (ImageView) findViewById(R.id.imageViewArtistSong);

        songName=new ArrayList<>();
        songPath=new ArrayList<>();
        albumArtId=new ArrayList<>();
        artistName=intent.getStringExtra("artistName");
        artistId=intent.getStringExtra("artistId");
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.ARTIST_ID +"=?",
                new String[] {artistId},
                MediaStore.Audio.Media.DISPLAY_NAME);
        if(cursor==null){
            Log.i("Cursor","is null");
        }else{
            cursor.moveToFirst();
            int name=cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
            int path =cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int album_Id=cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            do{
                String n=cursor.getString(name);
                String p=cursor.getString(path);
                String albumId=cursor.getString(album_Id);
                songName.add(n);
                songPath.add(p);
                albumArtId.add(albumId);
            }while(cursor.moveToNext());
        }
        Cursor cursor1 = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {albumArtId.get(0)},
                null);

        if (cursor1.moveToFirst()) {
            String path = cursor1.getString(cursor1.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            Bitmap bp = BitmapFactory.decodeFile(path);
            albumArt.setImageBitmap(bp);
        }else{
            albumArt.setImageResource(R.drawable.album_art);
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getApplicationContext(),R.layout.white_text_listview_item,songName);
        lv.setAdapter(arrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(), play.class);
                intent.putStringArrayListExtra("songName",songName);
                intent.putStringArrayListExtra("songPath",songPath);
                intent.putStringArrayListExtra("albumArtId",albumArtId);
                intent.putExtra("position",position);
                startActivity(intent);
                CommonMediaPlayer.songNameMP=songName.get(position);



            }
        });

    }
}
