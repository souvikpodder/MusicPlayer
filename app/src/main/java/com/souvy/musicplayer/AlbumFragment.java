package com.souvy.musicplayer;


import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> AlbumArt=new ArrayList<>();
    ArrayList<String> AlbumName=new ArrayList<>();
    ArrayList<String> AlbumId=new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_album, container, false);
        layoutManager=new GridLayoutManager(getActivity().getApplicationContext(),2);
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerViewAlbum);
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            getAlbum();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecyclerAdapter(AlbumId,AlbumArt,AlbumName,getContext(),v);

        recyclerView.setAdapter(adapter);

        return v;
    }

    public void getAlbum(){

        ContentResolver contentResolver=getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String [] {MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ALBUM_ART,MediaStore.Audio.Albums.ALBUM},
                null, null, MediaStore.Audio.Albums.ALBUM);

        int albumId=cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
        int albumArt=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
        int albumName=cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
        if(cursor==null) {
            Log.i("Error","cursor is null");

        }else {
            cursor.moveToFirst();
            do {
                AlbumId.add(cursor.getString(albumId));
                AlbumArt.add(cursor.getString(albumArt));
                AlbumName.add(cursor.getString(albumName));
            } while (cursor.moveToNext());
        }

    }

}
