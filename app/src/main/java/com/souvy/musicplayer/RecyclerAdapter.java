package com.souvy.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import static com.souvy.musicplayer.CommonMediaPlayer.bp;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    ArrayList<String> AlbumId,AlbumArt,AlbumName;
    Context cx;
    View mainView;
    public RecyclerAdapter(ArrayList<String> AlbumId,ArrayList<String> AlbumArt,ArrayList<String> AlbumName,Context cx,View mainView){
       this.AlbumArt=AlbumArt;
       this.AlbumId=AlbumId;
       this.AlbumName=AlbumName;
       this.cx=cx;
       this.mainView=mainView;

    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.album,parent,false);
        return new ImageViewHolder(view,cx,mainView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String path = AlbumArt.get(position);
        BitmapFactory.Options op=new BitmapFactory.Options();
        op.outHeight=150;
        op.outWidth=150;
        Bitmap bp = BitmapFactory.decodeFile(path,op);
        if(bp==null){
            holder.albumArt.setImageResource(R.drawable.album_art);
        }else{
            holder.albumArt.setImageBitmap(bp);
        }

        holder.albumArt.setTag(AlbumId.get(position));
        holder.albumName.setText(AlbumName.get(position));

    }

    @Override
    public int getItemCount() {
        return AlbumId.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ArrayList<String> songName=new ArrayList<>();
        ArrayList<String> songPath=new ArrayList<>();
        ArrayList<String> albumArtId=new ArrayList<>();
        Context cx;
        ImageView albumArt;
        TextView albumName;
        View mainView;


        public ImageViewHolder(View itemView, Context cx, View mainView) {
            super(itemView);
            albumArt= (ImageView) itemView.findViewById(R.id.album_Art);
            albumName= (TextView) itemView.findViewById(R.id.album_name);
            this.cx=cx;
            this.mainView=mainView;
            albumArt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            songName.clear();
            songPath.clear();
            albumArtId.clear();
            String album_id_al = (String)v.getTag();
            Cursor cursor = cx.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Audio.Media.ALBUM_ID +"=?",
                    new String[] {album_id_al},
                    null);
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
            Intent intent=new Intent(cx,albumSongs.class);
            intent.putStringArrayListExtra("songName",songName);
            intent.putStringArrayListExtra("songPath",songPath);
            intent.putStringArrayListExtra("albumId",albumArtId);
            cx.startActivity(intent);

        }


    }

}
