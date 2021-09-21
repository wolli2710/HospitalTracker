package com.wv.hospitaltracker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by wolfgangvogl on 14/05/15.
 */
public class CSVHandler extends FileWriteHandler {

    Long currentTimeStamp;
    String userId;
    String userSex;
    String userAge;

    CSVHandler(String dir){
        super();

        currentTimeStamp = System.currentTimeMillis()/1000;
        userId = Data.userId;
        userSex = Data.userSex;
        userAge = Data.userAge;
        FILE_NAME = "user_"+userId+"_"+currentTimeStamp.toString()+".csv";
        APPLICATION_DIRECTORY = dir;
        createFile(APPLICATION_DIRECTORY, FILE_NAME);

        writeToFile("UserId", "User Sex", "User Age", "", "");
        writeToFile(userId, userSex, userAge, "", "");
        writeToFile("", "", "", "", "");
        writeToFile("Event", "Start Timestamp", "End Timestamp", "Start Zeit", "End Zeit");
    }

    public void writeToFile(String name, String startTimeStamp, String timeStamp, String startDateTime, String stopDateTime){
        if( new File(APPLICATION_DIRECTORY).exists() ){
            try {
                FileWriter fw = new FileWriter(file, true);
                String entry = name +","+ startTimeStamp +","+ timeStamp +","+ startDateTime +","+ stopDateTime +"\n";
                fw.append(entry);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToFile(String name, String startTimeStamp, String dateTime){
        if( new File(APPLICATION_DIRECTORY).exists() ){
            try {
                FileWriter fw = new FileWriter(file, true);
                String entry = name +","+ startTimeStamp + "," + dateTime + "\n";
                fw.append(entry);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}