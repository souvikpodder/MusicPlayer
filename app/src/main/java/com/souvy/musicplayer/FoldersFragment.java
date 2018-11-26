package com.souvy.musicplayer;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
public class FoldersFragment extends Fragment {
    ArrayList<String> folderName = new ArrayList<>();
    ArrayList<String> folderPath=new ArrayList<>();
    ListView lv;

    public FoldersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_folders, container, false);
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            getFolder();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);
        }
        lv= (ListView) v.findViewById(R.id.folderList);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.folde_text_view, folderName);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),FolderSongs.class);
                intent.putStringArrayListExtra("folderName", folderName);
                intent.putStringArrayListExtra("folderPath",folderPath);
                intent.putExtra("position",position);
                startActivity(intent);

            }
        });
        return v;
    }



    public void getFolder(){
        Cursor songCursor=getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
        if(songCursor.moveToFirst()) {
            int path = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                String p = songCursor.getString(path);
                int pos=p.lastIndexOf("/");
                p=p.substring(0,pos);
                String show=p;

                show=show.replace("/storage","");
                show=show.replace("/emulated","");
                show=show.replace("/0","");
                show=show.replaceFirst("/","");
                show= show.replace("/"," "+getResources().getString(R.string.Arrow)+" ");
                if (!folderPath.contains(p)) {
                    folderName.add(show);
                    folderPath.add(p);
                }
            } while (songCursor.moveToNext());
        }
    }


}
