package com.souvy.musicplayer;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {
    ListView lv;
    ArrayList<String> artistName= new ArrayList<>();
    ArrayList<String> artistId=new ArrayList<>();
    public ArtistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_artist, container, false);
        lv= (ListView) v.findViewById(R.id.listViewArtist);
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            getArtist();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.white_text_listview_item, artistName);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),artistSong.class);
                intent.putExtra("artistName", artistName.get(position));
                intent.putExtra("artistId", artistId.get(position));
                startActivity(intent);

            }
        });
        return v;

    }


    public void getArtist(){
        ContentResolver contentResolver=getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String [] {MediaStore.Audio.Artists._ID,MediaStore.Audio.Artists.ARTIST},
                null, null, MediaStore.Audio.Artists.ARTIST);

        int artistIdi=cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
        int artisNamei=cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
        if(cursor==null) {
            Log.i("Error","cursor is null");

        }else {
            cursor.moveToFirst();
            do {
                artistId.add(cursor.getString(artistIdi));
                artistName.add(cursor.getString(artisNamei));
                } while (cursor.moveToNext());
        }
    }
}
