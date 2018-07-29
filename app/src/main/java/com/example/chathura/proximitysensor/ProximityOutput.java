package com.example.chathura.proximitysensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ProximityOutput extends AppCompatActivity implements SensorEventListener{

    public TextView Advice;
    public TextView output;
    public Spinner tones;
    public SensorManager proximitySensorOutput;
    public Sensor proximitySensor;
    public static final int sensorActivity = 4;
    String tone;
    ArrayList<String> list;
    String[] x = new String[ 3 ];
    boolean choose = false;
    boolean playing = false;
    playMusic pm;
    public Button select;
    public static boolean selecrPress = false;
    String sdSong;
    public static MediaPlayer ring3 = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_output);

        Advice = (TextView) findViewById(R.id.Advice);
        output = (TextView) findViewById(R.id.output);
        tones = (Spinner) findViewById(R.id.tones);
        select = (Button) findViewById(R.id.btnSelect);


        proximitySensorOutput = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = proximitySensorOutput.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        listRaw();

        pm = new playMusic();

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProximityOutput.this , SelectSongs.class);
                startActivity(intent);
                selecrPress = true;
            }
        });

        if(selecrPress == true){
            tones.setEnabled(false);
            sdSong = getIntent().getExtras().getString("Song");
            //sdSong = sdSong.concat(".mp3");
        }


        tones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> listAdapter, View view, int position, long id) {
                tone = tones.getSelectedItem().toString();
                choose = true;
                Toast.makeText(listAdapter.getContext(), "Selected : " + tone, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        proximitySensorOutput.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        proximitySensorOutput.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[ 0 ] >= -sensorActivity && event.values[ 0 ] <= sensorActivity) {
                if(selecrPress == false){
                    output.setText("It's near to me");
                    Advice.setText("Get your hand out from sensor");
                    pm.play(tone, getApplicationContext(), getResources(), getPackageName());
                    playing = true;
                }
                if(selecrPress == true){
                    output.setText("It's near to me");
                    Advice.setText("Get your hand out from sensor");
                    playing = true;
                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/Favourite/"+sdSong;
                    Toast.makeText(ProximityOutput.this, filePath , Toast.LENGTH_LONG).show();
                    pm.playSd(filePath);



                }
            }
            else {
                if (playing == true && selecrPress == false) {
                    output.setText("It's far from me");
                    Advice.setText("Get your hand near to sensor");
                    pm.pause();
                    playing = false;
                }
                if(playing == true && selecrPress == true){
                    output.setText("It's far from me");
                    Advice.setText("Get your hand near to sensor");
                    pm.pause();
                    playing = false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void listRaw() {
        list = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (int count = 0; count < fields.length; count++) {
            x[ count ] = fields[ count ].getName();

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, x);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tones.setAdapter(adapter);

    }
}
