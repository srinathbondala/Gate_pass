package com.example.easyexit;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Objects;

//import java.util.HashMap;

public class Admin extends AppCompatActivity implements View.OnClickListener {
    public static String bag = "";
    UserData1 userData;
    TextView Email,RollNo,Section,Logout,permit,lable;
    private ImageView iv,permitions,list,explore,notification_view,notification_add,profile,reload;
   // RecyclerView list;
    Intent i;
    FirebaseAuth mAuth;
    FirebaseUser muser;
    private FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    ProgressDialog p;
    private notificationAdapter adapterClass;
    private RecyclerView recyclerView;
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
        profile = (ImageView) findViewById(R.id.option1);
        explore = (ImageView)findViewById(R.id.exploredata);
        permitions = (ImageView) findViewById(R.id.attedance);
        notification_view= (ImageView) findViewById(R.id.notification_view);
        notification_add = (ImageView) findViewById(R.id.notification_add);
        recyclerView = findViewById(R.id.recyclerView);
        reload = findViewById(R.id.reload);
        Objects.requireNonNull(getSupportActionBar()).hide();
        p= new ProgressDialog(this);
        Logout.setOnClickListener(this);
        permit.setOnClickListener(this);
        list.setOnClickListener(this);
        permitions.setOnClickListener(this);
        reload.setOnClickListener(this);
        explore.setOnClickListener(this);
        notification_view.setOnClickListener(this);
        notification_add.setOnClickListener(this);
        profile.setOnClickListener(this);
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

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[] {Color.GREEN, Color.RED});
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
        loadNotification();
    }

    private void loadNotification() {
        FirebaseFunctions firebaseFunctions = new FirebaseFunctions();
        firebaseFunctions.loadNotifications(userData.getName(), this, new FirebaseDataCallback() {
            @Override
            public void onDataFetched(ArrayList<notification_data> data) {
                if (data != null && !data.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterClass = new notificationAdapter(data,getApplicationContext());
                    recyclerView.setAdapter(adapterClass);
                } else {
                    Toast.makeText(getApplicationContext(), "No notifications found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
int no=1;
    @Override
    public void onClick(View view) {
        if(view==Logout) {
            p.setMessage("Please wait Signing out...");
            p.setTitle("Loading");
            p.setCanceledOnTouchOutside(false);
            p.show();
            FirebaseAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userData");
            editor.remove("userRole");
            editor.apply();
            Toast.makeText(getApplicationContext(), "successfully logged out", Toast.LENGTH_LONG).show();
            i = new Intent(getApplicationContext(), MainActivity.class);
            finish();
            startActivity(i);
        }
        if(view==permit)
        {
            i=new Intent(getApplicationContext(), Register.class);
            startActivity(i);
        }
        if(view == permitions)
        {
            i = new Intent(getApplicationContext(), Attendance.class);
            i.putExtra("data","student");
            i.putExtra("faculty",userData.getName());
            startActivity(i);
        }
        if(view == list)
        {
            Intent i = new Intent(getApplicationContext(),ViewData.class);
            i.putExtra("data","AdminData");
            startActivity(i);
        }
        if(view == explore)
        {
            i =new Intent(Admin.this,prevView.class);
            startActivity(i);
        }
        if(view == notification_view){
            Toast.makeText(this, "view notification", Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.swipe_in_left, R.anim.swipe_out_right);
        }
        if(view == notification_add){
//            Toast.makeText(this, "add notification", Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.swipe_in_left, R.anim.swipe_out_right);
            i = new Intent(getApplicationContext(),notification_add.class);
            startActivity(i);

        }
        if(view == profile){
            overridePendingTransition(R.anim.swipe_in_left, R.anim.swipe_out_right);
            i = new Intent(getApplicationContext(),profile.class);
            startActivity(i);
        }
        if(view == reload){
            if(no==1) {
                no=0;
                p.show();
                loadNotification();
                p.dismiss();
                no=1;
            }
            else{
                Toast.makeText(this, "Please Wait", Toast.LENGTH_SHORT).show();
            }
        }
    }
}