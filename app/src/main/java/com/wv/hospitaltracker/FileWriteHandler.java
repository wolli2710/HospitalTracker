package com.wv.hospitaltracker;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wolfgangvogl on 31/05/15.
 */
public abstract class FileWriteHandler {

    File file;
    String storage_state;

    protected static String FILE_NAME;
    protected static String AUDIO_FILE_NAME;
    protected static String APPLICATION_DIRECTORY;

    FileWriteHandler(){
        storage_state = Environment.getExternalStorageState();
    }

    void createFile(String directory, String filename, String prefix){
        file = new File(directory +"/"+ prefix+filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void createFile(String directory, String filename){
        if( !new File(directory).exists() ) {
            writeDirectory();
        }
        file = new File(directory + "/" + filename);
        try {
            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeDirectory(String dir){
        if( checkStorageState() ) {
            File newDir = new File(dir);
            newDir.mkdirs();
        } else {
            Log.d("writeDirectory", "No External Storage Device found");
        }
    }

    protected void writeDirectory(){
        if( checkStorageState() ) {
            File newDir = new File(APPLICATION_DIRECTORY);
            newDir.mkdirs();
        } else {
            Log.d("writeDirectory", "No External Storage Device found");
        }
    }

    public boolean checkStorageState()
    {
        return (Environment.MEDIA_MOUNTED.equals(storage_state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(storage_state)) ? true : false;
    }

}
