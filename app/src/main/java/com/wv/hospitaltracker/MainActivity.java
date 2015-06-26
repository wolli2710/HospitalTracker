package com.wv.hospitaltracker;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends Activity {

    ToggleButton careButton;
    ToggleButton visitButton;
    ToggleButton therapyButton;
    ToggleButton wheelchairButton;
    ToggleButton actigraphieButton;
    ToggleButton outOfHouse;

    ToggleButton lightGlassesButton;
    Runnable lightGlassButtonRunnable;
    Handler lightGlassButtonHandler;

    Button medicineButton;
    Button foodButton;
    Button lightOnButton;
    Button lightOffButton;
    Button eyesOpenButton;
    Button eyesClosedButton;
    Button menuButton;

    HashMap csvEntries = new HashMap<String, Long >();
    CSVHandler csv;
    AudioHandler audioHandler;

    @Override
    protected void onPause() {
        super.onPause();
        audioHandler.stopRecording();
    }

    @Override
    protected void onResume() {
        super.onResume();
        audioHandler = AudioHandler.getAudioActivity();
        audioHandler.startRecording();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        careButton = (ToggleButton)findViewById(R.id.toggleButtonCare);
        wheelchairButton = (ToggleButton)findViewById(R.id.toggleButtonWheelChair);
        visitButton = (ToggleButton)findViewById(R.id.toggleButtonVisit);
        therapyButton = (ToggleButton)findViewById(R.id.toggleButtonTherapy);
        actigraphieButton = (ToggleButton)findViewById(R.id.toggleButtonActigraphie);
        outOfHouse = (ToggleButton)findViewById(R.id.toggleButtonOutOfHouse);

        lightGlassesButton = (ToggleButton)findViewById(R.id.toggleButtonLightGlasses);

        medicineButton = (Button)findViewById(R.id.buttonMedicine);
        foodButton = (Button)findViewById(R.id.buttonFood);
        lightOnButton = (Button)findViewById(R.id.buttonLightOn);
        lightOffButton = (Button)findViewById(R.id.buttonLightOff);
        eyesClosedButton = (Button)findViewById(R.id.buttonEyesClosed);
        eyesOpenButton = (Button)findViewById(R.id.buttonEyesOpen);
        menuButton = (Button)findViewById(R.id.buttonMenu);

        csv = new CSVHandler( "/storage/sdcard0/Android/data/com.hospitalTracker.wv" );
        audioHandler = AudioHandler.getAudioActivity();
        audioHandler.startRecording();

        toggleButtonHandler();
        buttonHandler();
    }

    private void buttonHandler(){
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        eyesOpenButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v, e);
                return false;
            }
        });

        eyesClosedButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v,e);
                return false;
            }
        });
        medicineButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v,e);
                return false;
            }
        });

        foodButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v,e);
                return false;
            }
        });

        lightOnButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v,e);
                return false;
            }
        });

        lightOffButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                buttonTouchBehaviourHandler(v,e);
                return false;
            }
        });
    }

    private void toggleButtonHandler(){
        outOfHouse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toggleButtonBehaviourHandler(v);
            }
        });
        actigraphieButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            toggleButtonBehaviourHandler(v);
            boolean on = ((ToggleButton) v).isChecked();
            if(on) {
                startBlinkingBehaviour(20, v);
            }else{
                stopBlinkingBehaviour();
            }
            }
        });
        wheelchairButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toggleButtonBehaviourHandler(v);
            }
        });
        careButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                toggleButtonBehaviourHandler(v);
            }
        });
        visitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            toggleButtonBehaviourHandler(v);
            }
        });
        therapyButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            toggleButtonBehaviourHandler(v);
            }
        });
        lightGlassesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            toggleButtonBehaviourHandler(v);
            boolean on = ((ToggleButton) v).isChecked();
            if(on) {
                startBlinkingBehaviour(60, v);
            }else{
                stopBlinkingBehaviour();
            }
            }
        });
    }

    private void startBlinkingBehaviour(int t, View v){
        final int t1 = t;
        final View v1 = v;
        lightGlassButtonHandler = new Handler();
        lightGlassButtonHandler.postDelayed(
            lightGlassButtonRunnable = new Runnable() {
                @Override
                public void run() {
                int delay = 300;
                changeColourOverTime(delay, v1, 0xFF00FF00, 0xFFFFFF00);
                }
            }, getMinutes(t1)
        );
    }

    private void stopBlinkingBehaviour(){
        lightGlassButtonHandler.removeCallbacks(lightGlassButtonRunnable);
    }

    private void changeColourOverTime(int t, View v, int cPrev, int cNew){
        final int t1 = t;
        final View v1 = v;
        final int c1 = cNew;
        final int c2 = cPrev;
        lightGlassButtonHandler = new Handler();
        lightGlassButtonHandler.postDelayed(
            lightGlassButtonRunnable = new Runnable() {
                @Override
                public void run() {
                    v1.setBackgroundColor(c1);
                    changeColourOverTime(t1, v1, c1, c2);
                }
            }, t
        );
    }

    private void buttonBehaviourHandler(View v){
        String name = getResources().getResourceEntryName(v.getId()).replace("button", "");
        Long ts = System.currentTimeMillis()/1000;
        String timeStamp = ts.toString();

        String dateTime = getDateTimeFromTimeStamp(new Date(ts));
        csv.writeToFile(name, timeStamp, dateTime);
    }

    public void buttonTouchBehaviourHandler(View v, MotionEvent e){
        if(e.getAction() == MotionEvent.ACTION_UP) {
            v.setBackgroundColor(0xFF0000FF);
        } else if(e.getAction() == MotionEvent.ACTION_DOWN) {
            buttonBehaviourHandler(v);
            v.setBackgroundColor(0xFF00FF00);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void showMenu(View view){

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu_main);

        popupMenu.getMenu().add(0, 0, 2, Data.userId );
        popupMenu.getMenu().add(0, 0, 4, Data.userSex);
        popupMenu.getMenu().add(0, 0, 6, Data.userAge);
        popupMenu.getMenu().add(0, 0, 7, "");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            if (item.getTitle().equals("Calibrate")) {
                Toast.makeText(MainActivity.this, "You calibrated the File successfully...", Toast.LENGTH_SHORT).show();

                Long ts = System.currentTimeMillis() / 1000;
                String timeStamp = ts.toString();

                String dateTime = getDateTimeFromTimeStamp(new Date(ts));
                csv.writeToFile("calibrate", timeStamp, dateTime);
            }
            if (item.getTitle().equals("Beenden")){
                //stop audio
                audioHandler.stopRecording();
                //stop app
                System.exit(0);
            }
            return true;
            }
        });
        popupMenu.show();
    }

    private String getDateTimeFromTimeStamp(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}