package com.example.mytesting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class CreateCreation extends AppCompatActivity {

    TextView mSelectedDate;
    private DatePickerDialog.OnDateSetListener mDatteSetListner;

    Button mTop;
    Button mBottom;
    Button mBoots;
    Button mAccesories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_creation);

        mSelectedDate = findViewById(R.id.selectDat);

        mTop = findViewById(R.id.selected1cl);
        mBottom = findViewById(R.id.selected2cl);


        mTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageGallery();
            }
        });

        mBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageGallery();
            }
        });



        mSelectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CreateCreation.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDatteSetListner,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.MAGENTA));
                dialog.show();
            }
        });

        mDatteSetListner = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = month + "/" + day + "/" + year;
                mSelectedDate.setText(date);
            }
        };

    }

    void openImageGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
    }
}