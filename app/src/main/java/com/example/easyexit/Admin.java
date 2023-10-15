package com.example.easyexit;

import static com.example.easyexit.User.flag;
import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.temail;
import static com.example.easyexit.login.tname;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.util.HashMap;

public class Admin extends AppCompatActivity implements View.OnClickListener {
    public static String bag = "";
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
        iv = (ImageView) findViewById(R.id.imageView);
        list = (ImageView) findViewById(R.id.list);
        explore = (ImageView)findViewById(R.id.exploredata);
        permitions = (ImageView) findViewById(R.id.permitions);
        getSupportActionBar().hide();
        p= new ProgressDialog(this);
        Logout.setOnClickListener(this);
        permit.setOnClickListener(this);
        list.setOnClickListener(this);
        permitions.setOnClickListener(this);
        explore.setOnClickListener(this);
        Email.setText(temail);
        RollNo.setText(tname);
        Section.setText(tbranch);
        bag="";
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
            bag= "waiting";
            flag = "true";
            i = new Intent(getApplicationContext(),ViewData.class);
            startActivity(i);
        }
        if(view == list)
        {
            flag = "true";
            bag = "";
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