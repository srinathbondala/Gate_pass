package com.example.easyexit;

import static com.example.easyexit.User.flag;
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

public class security extends AppCompatActivity implements View.OnClickListener{

    TextView Email, RollNo, Section, Logout, lable;
    ImageView iv, permitions, list;
    // RecyclerView list;
    Intent i;
    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    ProgressDialog p;
    String a1, a2, a3, a4, a5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        lable = (TextView) findViewById(R.id.textView);
        Email = (TextView) findViewById(R.id.Email);
        RollNo = (TextView) findViewById(R.id.Roll_NO);
        Section = (TextView) findViewById(R.id.Section);
        Logout = (Button) findViewById(R.id.Logout);
        iv = (ImageView) findViewById(R.id.imageView);
        list = (ImageView) findViewById(R.id.list);
        permitions = (ImageView) findViewById(R.id.permitions);
        getSupportActionBar().hide();
        p = new ProgressDialog(this);
        Logout.setOnClickListener(this);
        list.setOnClickListener(this);
        permitions.setOnClickListener(this);

    /*    mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference();
        databaseReference.child("Faculty data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        UserData1 i1 = ds.getValue(UserData1.class);
                        if (i1 != null && i1.getEmail().equals(user_email)) {
                            a1 = i1.getEmail();
                            a2 = i1.getName();
                            a3 = i1.getBranch();
                            a4 = i1.getPhoneNumber();
                            a5 = i1.getYear();
                            Email.setText(a1);
                            RollNo.setText(a2);
                            Section.setText(a3);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        Email.setText(temail);
        RollNo.setText(tname);
        Section.setText(tbranch);
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
        if(view == permitions)
        {
            Toast.makeText(getApplicationContext(), "successfully logged out", Toast.LENGTH_LONG).show();
           /* i = new Intent(getApplicationContext(),ViewData.class);
            startActivity(i);*/
        }
        if(view == list)
        {
            flag = "false";
            bag = "Approved";
            i = new Intent(getApplicationContext(),ViewData.class);
            startActivity(i);
        }
    }
}