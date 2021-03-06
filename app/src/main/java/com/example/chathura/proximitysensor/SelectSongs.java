package com.example.chathura.proximitysensor;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectSongs extends AppCompatActivity {
    Context context;
    public static final int RUNTIME_PERMISSION_CODE = 7;
    String[] ListElements = new String[] { };
    ListView listView;
    List<String> ListElementsArrayList ;
    ArrayAdapter<String> adapter ;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_songs);
        listView = (ListView) findViewById(R.id.listView1);
        context = getApplicationContext();
        ListElementsArrayList = new ArrayList<>(Arrays.asList(ListElements));
        adapter = new ArrayAdapter<String>
                (SelectSongs.this, android.R.layout.simple_list_item_1, ListElementsArrayList);

        AndroidRuntimePermission();


        GetAllMediaMp3Files();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SelectSongs.this, ProximityOutput.class);
                intent.putExtra("Song" , parent.getAdapter().getItem(position).toString());
                ProximityOutput.selecrPress = true;
                startActivity(intent);

                // TODO Auto-generated method stub
                Toast.makeText(SelectSongs.this,parent.getAdapter().getItem(position).toString(),Toast.LENGTH_LONG).show();
                SelectSongs.this.finish();
            }
        });

    }


    public void GetAllMediaMp3Files(){
        contentResolver = context.getContentResolver();
        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
        );

        if (cursor == null) {
            Toast.makeText(SelectSongs.this,"Something Went Wrong.", Toast.LENGTH_LONG);
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(SelectSongs.this,"No Music Found on SD Card.", Toast.LENGTH_LONG);
        }
        else {
            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);

            do {
                String SongTitle = cursor.getString(Title);
                ListElementsArrayList.add(SongTitle);

            } while (cursor.moveToNext());
        }
    }


    public void AndroidRuntimePermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(SelectSongs.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    SelectSongs.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel",null);
                    AlertDialog dialog = alert_builder.create();
                    dialog.show();
                }
                else {
                    ActivityCompat.requestPermissions(
                            SelectSongs.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode){
            case RUNTIME_PERMISSION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }
                else {
                }
            }
        }
    }
}
