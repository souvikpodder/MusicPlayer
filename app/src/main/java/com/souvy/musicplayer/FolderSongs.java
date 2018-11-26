package com.souvy.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FolderSongs extends AppCompatActivity {
    ArrayList<String> folderName,folderPath;
    int position;
    ArrayList <String> songName;
    ArrayList<String> songPath;
    ArrayList<String> albumArtId;
    ListView lv;
    TextView folderNameTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_songs);

        songName=new ArrayList<>();
        songPath=new ArrayList<>();
        albumArtId=new ArrayList<>();

        lv= (ListView) findViewById(R.id.FolderListSongs);

        folderNameTx= (TextView) findViewById(R.id.FolderNameTextView);


        Intent intent=getIntent();
        folderName=intent.getStringArrayListExtra("folderName");
        folderPath=intent.getStringArrayListExtra("folderPath");
        position=intent.getIntExtra("position",-1);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            getMusic();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);
        }

        folderNameTx.setText(folderName.get(position));
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


    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Cursor songCursor=contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.DATA + " LIKE "+"?",
                new String []{folderPath.get(position)+"%"},MediaStore.Audio.Media.DISPLAY_NAME);
        if(songCursor==null){
            Log.wtf("Error","Cursor is Null");
        }else{
            if(!songCursor.moveToFirst()){
                Log.wtf("Error","No data present");
            }else{
                int name=songCursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int album_id=songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int path =songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                do{
                    String n=songCursor.getString(name);
                    String p=songCursor.getString(path);
                    String albumId=songCursor.getString(album_id);
                    songName.add(n);
                    songPath.add(p);
                    albumArtId.add(albumId);

                }while(songCursor.moveToNext());
            }
        }

        songCursor.close();
    }
}
