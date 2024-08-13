package com.example.easyexit;

import static com.example.easyexit.User.flag;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

//import java.util.HashMap;

public class Admin extends AppCompatActivity implements View.OnClickListener {
    public static String bag = "";
    UserData1 userData;
    TextView Email,RollNo,Section,Logout,permit,lable;
    ImageView iv,permitions,list,explore;
   // RecyclerView list;
    Intent i;
    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    ProgressDialog p;
    String a1,a2,a3,a4,a5;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        lable = (TextView) findViewById(R.id.textView);
        Email = (TextView) findViewById(R.id.Email);
        RollNo = (TextView) findViewById(R.id.Roll_NO);
        Section = (TextView) findViewById(R.id.Section);
        Logout =(Button)findViewById(R.id.Logout);
        permit = (Button)findViewById(R.id.permition);
        iv = (ImageView) findViewById(R.id.profile_icon);
        list = (ImageView) findViewById(R.id.list);
        explore = (ImageView)findViewById(R.id.exploredata);
        permitions = (ImageView) findViewById(R.id.attedance);
        getSupportActionBar().hide();
        p= new ProgressDialog(this);
        Logout.setOnClickListener(this);
        permit.setOnClickListener(this);
        list.setOnClickListener(this);
        permitions.setOnClickListener(this);
        explore.setOnClickListener(this);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userDataJson = sharedPreferences.getString("userData", null);
        if (userDataJson != null) {
            userData = gson.fromJson(userDataJson, UserData1.class);
        }
        Email.setText(userData.getEmail());
        RollNo.setText(userData.getName());
        Section.setText(userData.getBranch());
        bag="";
        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(75, "Present"));
        entries.add(new PieEntry(25, "Absent"));

        PieDataSet dataSet = new PieDataSet(entries, "Attendance");
        dataSet.setColors(new int[] {Color.GREEN, Color.RED});
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }
    @Override
    public void onClick(View view) {
        if(view==Logout) {
            p.setMessage("Please wait Signing out...");
            p.setTitle("Loading");
            p.setCanceledOnTouchOutside(false);
            p.show();
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(), "successfully logged out", Toast.LENGTH_LONG).show();
            i = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(i);
        }
        if(view==permit)
        {
            i=new Intent(getApplicationContext(), Register.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(),"permission started",Toast.LENGTH_SHORT).show();
        }
        if(view == permitions)
        {
            i = new Intent(getApplicationContext(), Attendance.class);
            startActivity(i);
        }
        if(view == list)
        {
            flag = "true";
            bag = "Approved";
            i = new Intent(getApplicationContext(),ViewData.class);
            startActivity(i);
        }
        if(view == explore)
        {
            bag = "mydata";
            i =new Intent(Admin.this,prevView.class);
            startActivity(i);
        }
    }
}