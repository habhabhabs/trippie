package com.example.eunicechu.trippie;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;



public class voiceActivity extends Activity {

//    private Button openMic;
//    private TextView showVoiceText;
//    private static final String TAG = "voiceActivity";
//    private SpeechRecognizer stt;
//    private Intent recognizer_intent;
//    private int id;
//    private String num;
//    private final int REQ_CODE_SPEECH_OUTPUT = 143;
//    private int m_interval = 5000;
//    private Handler m_handler;
//    private String message = "";
//    private boolean performingSpeechSetup;
    private static final  String TAG = voiceActivity.class.getName();
    protected PowerManager.WakeLock mWakeLock;
    private SpeechRecognizer mSpeechRecognizer;
    private Handler mHandler = new Handler();
    TextView responseText;
    Intent mSpeechIntent;
    boolean killCommand = false;

    private static final String[] VALID_COMMANDS = {
            "park",
            "train",
            "food",
            "mall",
            "parking"
    };
private static final int VALID_COMMAND_SIZE = VALID_COMMANDS.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        responseText = (TextView) findViewById(R.id.textvoice);


//        stt = SpeechRecognizer.createSpeechRecognizer(this);

//        openMic = (Button) findViewById(R.id.button);
//        showVoiceText = (TextView) findViewById(R.id.textvoice);

//        recognizer_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        recognizer_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"en");
//        recognizer_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.getPackageName());
//        recognizer_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
//        recognizer_intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
//        stt.startListening(recognizer_intent);

//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                btnToOpenMic();
//            }
//        },0,1000);


//        openMic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnToOpenMic();
//                btnToOpenMic();
//            }
//        });
    }

    @Override
    public void onStart(){
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(voiceActivity.this);
        SpeechListener mRecognitionListener = new SpeechListener();
        mSpeechRecognizer.setRecognitionListener(mRecognitionListener);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,"com.example.eunicechu.trippie");

        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 20);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);
        this.mWakeLock.acquire();
        mSpeechRecognizer.startListening(mSpeechIntent);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        return false;
    }
    private String getResponse(int command){
        String reString = "Try again";
        switch(command){
            case 0:
                reString = "Return park";
                break;
            case 1:
                reString = "Return train";
                break;
            case 2:
                reString = "Return food";
                break;
            case 3:
                reString = "Return mall";
                break;
            case 4:
                reString = "Return Parking";
                break;
            default:
                break;
        }
        return reString;
    }

    @Override
    protected void onPause(){
        if(mSpeechRecognizer != null){
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }
        this.mWakeLock.release();
        super.onPause();
    }

    private void processCommand(ArrayList<String> matchStrings){
        String response = "Try Again";
        int maxStrings = matchStrings.size();
        boolean resultFound = false;
        for(int i = 0; i < VALID_COMMAND_SIZE && !resultFound; i++){
            for(int j = 0; j < maxStrings && !resultFound; j++){
                if(StringUtils.getLevenshteinDistance(matchStrings.get(j), VALID_COMMANDS[i]) < (VALID_COMMANDS[i].length()/3)){
                    response = getResponse(i);
                }
            }
        }
        final String finalResponse = response;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                responseText.setText(finalResponse);
            }
        });
    }

    class SpeechListener implements RecognitionListener {
        public void onBufferReceived(byte[] buffer){
            Log.d(TAG, "buffer Received");
        }
        public void onError (int error){
            if(error == SpeechRecognizer.ERROR_CLIENT || error == SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS){
                Log.d(TAG, "client error");
            }
            else{
                Log.d(TAG, "other error");
                mSpeechRecognizer.startListening(mSpeechIntent);
            }
        }
        public void onEvent (int eventType, Bundle params){
            Log.d(TAG, "onEvent");
        }
        public void onPartialResults(Bundle partialResults){
            Log.d(TAG, "on ready for speech");
        }
        public void onResults(Bundle results){
            Log.d(TAG, "on results");
            ArrayList<String> matches = null;
            if(results != null){
                matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null){
                    Log.d(TAG, "Results are " + matches.toString());
                    final ArrayList<String> matchesStrings = matches;
                    processCommand(matchesStrings);
                    if(!killCommand){
                        mSpeechRecognizer.startListening(mSpeechIntent);
                    } else{
                        finish();
                    }
                }
            }
        }

        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        public void onBeginningOfSpeech(){
            Log.d(TAG, "Speech beginning");
        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        public void onEndOfSpeech(){
            Log.d(TAG, "Speech done");
        }
    };

//    private void btnToOpenMic(){
//        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//
//        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
//
//        try{
//            startActivityForResult(i, REQ_CODE_SPEECH_OUTPUT);
//        }
//        catch
//        (ActivityNotFoundException tim){
//
//        }
//    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch(requestCode){
//            case REQ_CODE_SPEECH_OUTPUT:{
//                if(resultCode == RESULT_OK && null != data){
//                    ArrayList<String> voiceInText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
////                    showVoiceText.setText(voiceInText.get(0));
//                }
//                break;
//            }
//        }
//    }
//    public void start(){
//        performingSpeechSetup = true;
//        stt.startListening(recognizer_intent);
//    }
//


}
