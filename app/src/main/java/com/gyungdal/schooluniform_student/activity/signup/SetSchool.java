package com.gyungdal.schooluniform_student.activity.signup;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.gyungdal.schooluniform_student.R;
import com.gyungdal.schooluniform_student.school.SchoolData;

import java.util.ArrayList;

/**
 * Created by GyungDal on 2016-09-06.
 */
public class SetSchool extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = SetSchool.class.getName();
    private Spinner stateList;
    private Spinner cityList;
    private Spinner schoolTypeList;
    private Spinner schoolNameList;
    private Button nextButton;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_set_school);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        stateList = (Spinner)findViewById(R.id.stateList);
        cityList = (Spinner)findViewById(R.id.cityList);
        schoolTypeList = (Spinner)findViewById(R.id.schoolTypeList);
        schoolNameList = (Spinner)findViewById(R.id.schoolNameList);
        nextButton = (Button)findViewById(R.id.schoolSetButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(stateList.getSelectedItem().toString().isEmpty() ||
                        schoolNameList.getSelectedItem().toString().isEmpty())){
                    new AlertDialog.Builder(SetSchool.this)
                            .setTitle(getString(R.string.confirm))
                            .setMessage(getString(R.string.state) + " : " + stateList.getSelectedItem().toString() + "\r\n"
                                + getString(R.string.city) + " : " + cityList.getSelectedItem().toString() + "\r\n" +
                                    getString(R.string.name) + " : " + schoolNameList.getSelectedItem().toString() + "\r\n")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent(SetSchool.this, SignUp.class);
                                    intent.putExtra("name", (String)schoolNameList.getSelectedItem());
                                    intent.putExtra("area", (String)stateList.getSelectedItem());
                                    startActivity(intent);
                                    SetSchool.this.finish();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
        setStateList();
        //setPrompt();
    }

    //prompt를 지정해줘도 그냥 바로 삭제됨... Prompt 삭제....
    private void setPrompt(){
        stateList.setPrompt(getString(R.string.state_spinner_prompt));
        cityList.setPrompt(getString(R.string.detail_spinner_prompt));
        schoolTypeList.setPrompt(getString(R.string.school_type_spinner_prompt));
        schoolNameList.setPrompt(getString(R.string.school_name_spinner_prompt));
    }
    private void setStateList(){
        ArrayList<String> result = SchoolData.getInstance().getState();
        if(result == null || result.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빔", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, result);
        stateList.setAdapter(adapter);
        stateList.setOnItemSelectedListener(this);
    }

    private void setCityList(String state){
        ArrayList<String> result = SchoolData.getInstance().getCity(state);
        if(result == null || result.isEmpty()) {
            Toast.makeText(getApplicationContext(), "빔", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, result);
        cityList.setAdapter(adapter);
        cityList.setOnItemSelectedListener(this);
    }

    private void setSchoolTypeList(){
        ArrayList<String> result = SchoolData.getInstance().getSchoolType();
        if(result == null || result.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, result);
        schoolTypeList.setAdapter(adapter);
        schoolTypeList.setOnItemSelectedListener(this);
    }


    private void setSchoolNameList(){
        ArrayList<String> result =
                SchoolData.getInstance().getSchoolName(
                        (String)stateList.getSelectedItem(),
                        (String)cityList.getSelectedItem(),
                        (String)schoolTypeList.getSelectedItem());
        if(result == null || result.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Not ready school data...\nplz wait...", Toast.LENGTH_SHORT).show();
            SetSchool.this.finish();
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, result);
        schoolNameList.setAdapter(adapter);
        schoolNameList.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.stateList:
                setCityList((String)stateList.getSelectedItem());
                break;
            case R.id.cityList:
                setSchoolTypeList();
                break;
            case R.id.schoolTypeList:
                setSchoolNameList();
                break;
            case R.id.schoolNameList:
                break;
            default:
                Log.wtf(TAG, "What??? I don't know spinner that have this id");
                Log.wtf(TAG, view.toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
