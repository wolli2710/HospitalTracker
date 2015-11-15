package com.wv.hospitaltracker;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends Activity {
    MediaRecorder mRecorder = null;

    ToggleButton rhymeButton;
    Runnable rhymeButtonRunnable;
    Handler rhymeButtonHandler;
    boolean childAwakeAsleep = true;
    boolean isRecording = true;

    public static  TextView audioTextView;

    Button errorButton;

    Button menuButton;

    Button childAwake;
    Button childAsleep;

    HashMap csvEntries = new HashMap<String, Long >();
    CSVHandler csv;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rhymeButton = (ToggleButton)findViewById(R.id.toggleButtonRhyme);

        errorButton = (Button)findViewById(R.id.error);
        menuButton = (Button)findViewById(R.id.buttonMenu);
        childAwake = (Button)findViewById(R.id.childAwake);
        childAsleep = (Button)findViewById(R.id.childAsleap);

        csv = new CSVHandler( "/storage/sdcard0/Android/data/com.hospitalTracker.wv" );

        audioTextView = (TextView)findViewById(R.id.textViewAudioSignal);

        mRecorder = new MediaRecorder();
        try{
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e){
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        toggleButtonHandler();
        buttonHandler();
        createAudioThread();
    }

    private void buttonHandler(){
        errorButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v, e);
                return false;
            }
        });

        childAwake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setAwakeAsleepButtonColors(v, childAsleep);
            }
        });

        childAsleep.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setAwakeAsleepButtonColors(v, childAwake);
            }
        });

        menuButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent e){
                System.exit(0);
                return false;
            }
        });
    }

    private void setAwakeAsleepButtonColors(View v, Button b){
        if(childAwakeAsleep) {
            v.setBackgroundColor(0xFF0000FF);
            b.setBackgroundColor(0xFFFFFFFF);
            buttonBehaviourHandler(v);
            childAwakeAsleep = false;
        }
    }

    private void toggleButtonHandler(){
        rhymeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            toggleButtonBehaviourHandler(v);
            boolean on = ((ToggleButton) v).isChecked();
            if (on) {
                ((GridLayout)findViewById(R.id.gridLayoutButtons)).setVisibility(View.VISIBLE);
                startBlinkingBehaviour(5, v);
            } else {
                ((GridLayout)findViewById(R.id.gridLayoutButtons)).setVisibility(View.GONE);
                stopBlinkingBehaviour();
            }
            }
        });
    }

    private void startBlinkingBehaviour(int t, View v){
        final int t1 = t;
        final View v1 = v;
        rhymeButtonHandler = new Handler();
        rhymeButtonHandler.postDelayed(
                rhymeButtonRunnable = new Runnable() {
                    @Override
                    public void run() {
                        int delay = 300;
                        changeColourOverTime(delay, v1, 0xFF00FF00, 0xFFFFFF00);
                    }
                }, getMinutes(t1)
        );
    }

    private void stopBlinkingBehaviour(){
        rhymeButtonHandler.removeCallbacks(rhymeButtonRunnable);
    }

    private void changeColourOverTime(int t, View v, int cPrev, int cNew){
        final int t1 = t;
        final View v1 = v;
        final int c1 = cNew;
        final int c2 = cPrev;
        rhymeButtonHandler = new Handler();
        rhymeButtonHandler.postDelayed(
            rhymeButtonRunnable = new Runnable() {
                @Override
                public void run() {
                v1.setBackgroundColor(c1);
                changeColourOverTime(t1, v1, c1, c2);
                }
            }, t
        );
    }

    public void buttonBehaviourHandler(View v){
        String name = getResources().getResourceEntryName(v.getId());
        Long ts = System.currentTimeMillis()/1000;
        String timeStamp = ts.toString();

        String dateTime = getDateTimeFromTimeStamp(new Date(ts));
        csv.writeToFile(name, timeStamp, dateTime);
    }

    public void buttonTouchBehaviourHandler(View v, MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_UP) {
            v.setBackgroundColor(Color.parseColor("#FF0000"));
        } else if(e.getAction() == MotionEvent.ACTION_DOWN) {
            buttonBehaviourHandler(v);
            v.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void toggleButtonBehaviourHandler(View v){
        boolean on = ((ToggleButton) v).isChecked();
        String name = getResources().getResourceEntryName(v.getId()).replace("toggleButton", "");
        Long ts = System.currentTimeMillis()/1000;
        String timeStamp = ts.toString();

        if(on){
            //started
            v.setBackgroundColor(0xFFFF0000);
            csvEntries.put(name, ts);
        }else{
            //stopped
            v.setBackgroundColor(0xFF00FF00);
            Long startTimeStamp = (Long)csvEntries.get(name);

            String startDateTime = getDateTimeFromTimeStamp(new Date(startTimeStamp));
            String stopDateTime = getDateTimeFromTimeStamp(new Date(ts));

            csv.writeToFile(name, startTimeStamp.toString(), timeStamp, startDateTime, stopDateTime);
        }
    }

    private int getMinutes(int m){
        return (60*1000) * m;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onKeyDown(int keycode, KeyEvent e){
        switch (keycode){
            case KeyEvent.KEYCODE_BACK:
                showMessage("Aktion nicht erlaubt!");
                return true;
        }
        return super.onKeyDown(keycode, e);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getDateTimeFromTimeStamp(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    private int getAmplitude() {
        if (mRecorder != null){
            return  mRecorder.getMaxAmplitude();
        }else{
            Log.d("AudioActivity", "no MediaRecorder found!!");
            return 0;
        }
    }

    private void createAudioThread() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(isRecording){
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int cAmplitude = getAmplitude();
                            if(cAmplitude != 0) {
                                String currentAmplitude = new Integer(cAmplitude).toString();
                                audioTextView.setText(currentAmplitude);
                                setAudioBackgroundColor(cAmplitude);
                            }
                        }
                    });
                }
            }
        };

        new Thread(runnable).start();
    }

    private void setAudioBackgroundColor(int amplitude){
        if(amplitude < 500) {
            audioTextView.setBackgroundColor(Color.parseColor("#99FF00"));
        } else if(amplitude > 3000) {
            audioTextView.setBackgroundColor(Color.parseColor("#FF0000"));
        } else{
            audioTextView.setBackgroundColor(Color.parseColor("#FFCC00"));
        }
    }
}