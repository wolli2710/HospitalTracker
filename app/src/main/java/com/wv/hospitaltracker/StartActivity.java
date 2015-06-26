package com.wv.hospitaltracker;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;


public class StartActivity extends ActionBarActivity {

    RadioGroup genderButtons;
    RadioButton maleButton;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        createButtonHandler();
        createRadioButtonHandler();
    }

    private void createRadioButtonHandler() {
        maleButton = (RadioButton)findViewById(R.id.radioButtonMale);
        genderButtons.check(maleButton.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    public void createButtonHandler(){
        startButton = (Button)findViewById(R.id.buttonStart);
        genderButtons = (RadioGroup)findViewById(R.id.radioButtonsSex);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userId = (EditText)findViewById(R.id.editTextId);
//                EditText userSex = (EditText)findViewById(R.id.editTextSex);
                EditText userAge = (EditText)findViewById(R.id.editTextAge);

                RadioButton currentButton = (RadioButton)findViewById(genderButtons.getCheckedRadioButtonId());

                Data.userId = userId.getText().toString();
//                Data.userSex = userSex.getText().toString();
                Data.userSex = currentButton.getText().toString();
                Data.userAge = userAge.getText().toString();

                if(valid()) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(StartActivity.this, "Input is not valid!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean valid(){
        if(Data.userId.length() != 0 && Data.userSex.length() != 0 && Data.userAge.length() != 0){
            if(isNumerical(Data.userAge) ){
                return true;
            }
        }
        return false;
    }

    private boolean isNumerical(String num){
        return num.matches("[0-9]+");
    }

    private boolean isString(String str){
        return str.matches("[a-zA-Z]+");
    }

}