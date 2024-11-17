package com.example.easyexit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.Objects;

public class Attendance extends AppCompatActivity implements View.OnClickListener {

    private TextView qr_code,lit_qr,details,lit_details;
    ImageView back_menue;
    private ImageView option2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Objects.requireNonNull(getSupportActionBar()).hide();
        qr_code= findViewById(R.id.user_qr_code);
        lit_qr = findViewById(R.id.lit_qr);
        details = findViewById(R.id.user_details);
        lit_details = findViewById(R.id.lit_details);
        back_menue = findViewById(R.id.back_menue);
        option2 = (ImageView) findViewById(R.id.option2);
        qr_code.setOnClickListener(this);
        details.setOnClickListener(this);
        back_menue.setOnClickListener(this);
        option2.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_profile, new Acadamic()).commit();
    }

    @Override
    public void onClick(View v) {
        if(v==back_menue){
            finish();
        }
        if(v==qr_code)
        {
            qr_code.setTextColor(Color.parseColor("#00ddff"));
            lit_qr.setBackgroundColor(Color.parseColor("#00ddff"));
            details.setTextColor(Color.parseColor("#000000"));
            lit_details.setBackgroundColor(0);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.swipe_in_left, R.anim.swipe_out_right);
            transaction.replace(R.id.main_profile, new Acadamic()).commit();
        }
        if(v==details)
        {
            details.setTextColor(Color.parseColor("#00ddff"));
            lit_details.setBackgroundColor(Color.parseColor("#00ddff"));
            qr_code.setTextColor(Color.parseColor("#000000"));
            lit_qr.setBackgroundColor(0);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.swipe_in_right, R.anim.swipe_out_left);
            transaction.replace(R.id.main_profile, new permission_data()).commit();
        }
        if(v==option2){
            Intent i = new Intent(getApplicationContext(), Admin.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}