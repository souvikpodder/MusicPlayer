package com.souvy.musicplayer;


import android.Manifest;
import android.app.LauncherActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import android.view.View;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends Fragment {
    ListView lv;
    public ArrayList <String> songName;
    public ArrayList<String> songPath;
    public ArrayList<String> albumArtId;
    //boolean isInSelectedMode=false;
   // ArrayList<Integer> selectedItem=new ArrayList<>();
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==5&&grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                getMusic();
            }
        }
    }
    public SongListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_song_list, container, false);
        lv= (ListView) v.findViewById(R.id.songList);
        songName=new ArrayList<>();
        songPath=new ArrayList<>();
        albumArtId=new ArrayList<>();
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            getMusic();
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},5);
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(),R.layout.white_text_listview_item,songName);
        lv.setAdapter(arrayAdapter);


//        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        lv.setLongClickable(true);
//
//        lv.setFocusable(false);
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                isInSelectedMode=true;
//                System.out.println("1");
//
//                ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<>(getContext(),R.layout.multiplechoice_white_text,songName);
//                System.out.println("2");
//                lv.setAdapter(arrayAdapter1);
//                System.out.println("3");
//                return false;
//            }
//        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(!isInSelectedMode) {
                Intent intent = new Intent(getActivity(), play.class);
                intent.putStringArrayListExtra("songName", songName);
                intent.putStringArrayListExtra("songPath", songPath);
                intent.putStringArrayListExtra("albumArtId", albumArtId);
                intent.putExtra("position", position);
                startActivity(intent);
                CommonMediaPlayer.songNameMP=songName.get(position);

                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID+ "=?",
                    new String[] {albumArtId.get(position)},
                    null);

                if (cursor.moveToFirst()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    Bitmap bp = BitmapFactory.decodeFile(path);
                    CommonMediaPlayer.bp=bp;
                }
//                }
//                else{
//                    if(lv.isItemChecked(position)){
//                        selectedItem.add(position);
//                    }else {
//                        selectedItem.remove(Integer.valueOf(position));
//                    }
//                    if(selectedItem.size()==0){
//                        isInSelectedMode=false;
//                        lv.setAdapter(arrayAdapter);
//                    }
//                    else{
//                        System.out.println(selectedItem);
//                    }
//                }
            }
        });


        return v;
    }
    public void getMusic(){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor songCursor=contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DISPLAY_NAME);
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
