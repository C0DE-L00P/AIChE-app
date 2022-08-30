package com.secondary.aiche.Knowledge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.secondary.aiche.R;

import androidx.annotation.Nullable;


// Choose Tech or Non-Tech Courses


public class CoursesType extends Activity {

    public static int choice;
    Spinner spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_type);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.languages,R.layout.spinner_courses_layout);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1){//Arabic Courses
                    choice = 2;
                }else if(position == 2){
                    choice = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void makeChoice1(View View){
        choice = 1; //Technical course
        Intent inetnt = new Intent(CoursesType.this, CoursesList.class);
        startActivity(inetnt);
    }
    public void makeChoice2(View View){
        //Non Technical course
        if (spinner.getSelectedItemPosition() == 0){
            Toast.makeText(this,"Please Choose which Language", Toast.LENGTH_SHORT).show();
        }else {
            Intent inetnt = new Intent(CoursesType.this, CoursesList.class);
            startActivity(inetnt);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        spinner.setSelection(0);
    }
}