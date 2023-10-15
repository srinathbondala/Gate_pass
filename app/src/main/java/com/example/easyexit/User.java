package com.example.easyexit;

import static com.example.easyexit.Admin.bag;
import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.temail;
import static com.example.easyexit.login.tname;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User extends AppCompatActivity implements View.OnClickListener {
    TextView Email,RollNo,Section,View,view2;
    ImageView iv,request,viewdata,mydata;
    Intent i;
    Button Logout;
    String a1,a2,a3,a4,a5;
    public static String flag="";
    public static String reqroll=" ";
    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
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
        iv = (ImageView) findViewById(R.id.imageView);
        mydata = (ImageView)findViewById(R.id.mydata);
        request = (ImageView) findViewById(R.id.request);
        viewdata = (ImageView) findViewById(R.id.Viewdata);
        View = (TextView) findViewById(R.id.view);
        view2 = (TextView) findViewById(R.id.view2);

        getSupportActionBar().hide();
        Logout.setOnClickListener(this);
        iv.setOnClickListener(this);
        viewdata.setOnClickListener(this);
        View.setOnClickListener(this);
        view2.setOnClickListener(this);
        mydata.setOnClickListener(this);
        request.setOnClickListener(this);
        p= new ProgressDialog(this);
        Email.setText(temail);
        RollNo.setText(tname);
        Section.setText(tbranch);
        reqroll = tname;
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
          flag="";
           bag= "my";
           Toast.makeText(getApplicationContext(), "viewData", Toast.LENGTH_SHORT).show();
           i=new Intent(User.this,ViewData.class);
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
    }
}