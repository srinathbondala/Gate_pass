package com.example.easyexit;

import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.tname;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;

public class User extends AppCompatActivity implements View.OnClickListener {
    TextView Email,RollNo,Section,View,view2;
    ImageView iv,request,viewdata,mydata,viewAttendance;
    Intent i;
    private RecyclerView recyclerView;
    private notificationAdapter adapterClass;
    Button Logout;
    private UserData1 userData1;
    public static String reqroll=" ";
    ProgressDialog p;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Email = (TextView) findViewById(R.id.Email);
        RollNo = (TextView) findViewById(R.id.Roll_NO);
        Section = (TextView) findViewById(R.id.Section);
        Logout =(Button)findViewById(R.id.Logout);
        iv = (ImageView) findViewById(R.id.profile_icon);
        mydata = (ImageView)findViewById(R.id.mydata);
        request = (ImageView) findViewById(R.id.request);
        viewdata = (ImageView) findViewById(R.id.Viewdata);
        View = (TextView) findViewById(R.id.view);
        view2 = (TextView) findViewById(R.id.view2);
        viewAttendance = (ImageView) findViewById(R.id.viewAttendance);
        recyclerView =findViewById(R.id.recyclerView);

        getSupportActionBar().hide();
        Logout.setOnClickListener(this);
        iv.setOnClickListener(this);
        viewdata.setOnClickListener(this);
        View.setOnClickListener(this);
        view2.setOnClickListener(this);
        mydata.setOnClickListener(this);
        request.setOnClickListener(this);
        viewAttendance.setOnClickListener(this);
        p= new ProgressDialog(this);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userData", null);
        userData1 = gson.fromJson(userJson,UserData1.class);
//        Email.setText(temail);
//        RollNo.setText(tname);
//        Section.setText(tbranch);
        try {
            Email.setText(userData1.getEmail());
            RollNo.setText(userData1.getName());
            Section.setText(userData1.getBranch());
        }catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            Section.setText(tbranch);
        }
        reqroll = tname;
        loadNotification();
    }

    private void loadNotification() {
        FirebaseFunctions firebaseFunctions = new FirebaseFunctions();
        firebaseFunctions.loadNotifications(userData1.getFaculty(), this, new FirebaseDataCallback() {
            @Override
            public void onDataFetched(ArrayList<notification_data> data) {
                if (data != null && !data.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterClass = new notificationAdapter(data,getApplicationContext(),"user");
                    recyclerView.setAdapter(adapterClass);
                } else {
                    Toast.makeText(getApplicationContext(), "No notifications found", Toast.LENGTH_SHORT).show();
                    recyclerView.setBackground(ContextCompat.getDrawable(User.this,R.drawable.back12));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view==iv) {
            Toast.makeText(getApplicationContext(), "Welcome to Easy Exit", Toast.LENGTH_LONG).show();
        }
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
       if(view == viewdata || view == View)
       {
           Toast.makeText(getApplicationContext(), "viewData", Toast.LENGTH_SHORT).show();
           i=new Intent(User.this,ViewData.class);
           i.putExtra("data","userdata");
           startActivity(i);
       }
       if(view == request || view == view2)
       {
           Toast.makeText(getApplicationContext(), "request", Toast.LENGTH_SHORT).show();
           i=new Intent(User.this,permission.class);
           startActivity(i);
       }
       if(view == mydata)
       {
           Toast.makeText(getApplicationContext(), "settings", Toast.LENGTH_SHORT).show();
       }
       if(view == viewAttendance){
           Toast.makeText(this, "Show User Attendance", Toast.LENGTH_SHORT).show();
       }
    }
}