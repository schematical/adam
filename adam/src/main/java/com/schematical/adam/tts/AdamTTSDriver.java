package com.schematical.adam.tts;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.schematical.adam.AdamWorldActivity;


import java.util.Locale;

/**
 * Created by user1a on 10/5/13.
 */
public class AdamTTSDriver implements TextToSpeech.OnInitListener {

    private final TextToSpeech tts;

    public AdamTTSDriver(){
        AdamWorldActivity am = AdamWorldActivity.getInstance();
        tts = new TextToSpeech(am, this);


    }
    public void Speak(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

    }
    @Override
    public void onInit(int status) {


        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);
            //tts.setPitch(0.1f);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {



            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }
}
