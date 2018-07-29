package com.example.chathura.proximitysensor;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class playMusic{
    MediaPlayer ring;
    MediaPlayer ring2;
    String tone;
    String y;
    Context c;
    boolean ringing = false;
    boolean ringing2 = false;


    public playMusic(){

    }

        public void play(String x, Context context, Resources res, String pn){
            tone = x;

            int id = res.getIdentifier(tone, "raw", pn);
            ring = MediaPlayer.create(context, id);
            ring.start();
            ringing = true;
        }

        public void playSd(String x) {
            try {
                ring2.setDataSource(x);
                ring2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                ring2.prepare();
                ring2.start();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        public void pause() {
        if(ringing == true) {
            ring.stop();
        }
        if(ringing2 == true){
            ring2.stop();
        }
        }


}
