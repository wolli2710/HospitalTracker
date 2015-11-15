package com.wv.hospitaltracker;

import android.media.MediaRecorder;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wolfgangvogl on 29/05/15.
 */
public class AudioHandler extends FileWriteHandler{

    public static AudioHandler instance = null;
    public static int audioText = 0;
    private MediaRecorder mRecorder = null;

    private static Thread audioThread;
    private static boolean isRecording;
    private static String fileName = "/dev/null";

    public static AudioHandler getAudioActivity(){
        if(instance == null){
            instance = new AudioHandler();
        }
        return instance;
    }

    private AudioHandler(){
        super();

        Long currentTimeStamp = System.currentTimeMillis();
        AUDIO_FILE_NAME = "user_"+Data.userId+"_"+System.currentTimeMillis()/1000+".csv";
        createFile(APPLICATION_DIRECTORY, AUDIO_FILE_NAME, "audio_");
        writeToFile("userId", "");
        writeToFile(Data.userId, "");
        writeToFile("timestamp", "amplitude");

        setApplicationDirectory("user_"+Data.userId+"_"+System.currentTimeMillis()/1000+".3gp");

        mRecorder = new MediaRecorder();

        try{
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(fileName);

            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e){
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        getAmplitude();
        createAudioThread();
    }

    private void createAudioThread() {

        audioThread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    while(isRecording){
                        int currentAmplitude = getAmplitude();
//                        AudioHandler.audioText = currentAmplitude;
                        Log.d("amplitude", currentAmplitude+"");
                        MainActivity.audioTextView.setText(currentAmplitude+"");
//                        if(currentAmplitude > 400){

//                        Log.i("currentAmplitude", String.valueOf(currentAmplitude) );
//                        Log.i("audioHandler", String.valueOf(instance) );

                            long currentTimeStamp = System.currentTimeMillis();
                            writeToFile(currentTimeStamp+"", currentAmplitude+"");
//                        }
                        Thread.sleep(1000);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        audioThread.start();
    }

    public int getAmplitude() {
        if (mRecorder != null){
            return  mRecorder.getMaxAmplitude();
        }else{
            Log.d("AudioActivity", "no MediaRecorder found!!");
            return 0;
        }
    }

    public void stopRecording(){
        setIsRecording(false);
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        instance = null;
        audioThread = null;
    }

    public void startRecording(){
        setIsRecording(true);
    }

    private void setIsRecording(boolean value) {
        isRecording = value;
    }


    private void setApplicationDirectory(String _fileName){
        fileName = APPLICATION_DIRECTORY + "/" + _fileName;
    }

    public void writeToFile(String timeStamp, String amplitude){
        if( new File(APPLICATION_DIRECTORY).exists() ){
            try {
                FileWriter fw = new FileWriter(file, true);
                String entry = timeStamp + "," + amplitude + "\n";
                fw.append(entry);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}