package com.example.easyexit;

import static com.example.easyexit.MainActivity.user_email;
import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.temail;
import static com.example.easyexit.login.tname;
import static com.example.easyexit.login.tphone;
import static com.example.easyexit.login.tyear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

public class permission extends AppCompatActivity implements View.OnClickListener{
    Button back,submit;
    EditText rollno,phno,reason,time,branch,year;
    UserData2 ud;
    ProgressDialog p;
    String a1,a2,a3,a4,a5;

    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    java.sql.Date date;
    java.util.Date date1 = new java.util.Date();
    String currentDateTimeString;

    Intent i;
    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permition);
        back = (Button) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit2);
        rollno = (EditText) findViewById(R.id.rollno);
        phno = (EditText) findViewById(R.id.phone);
        time = (EditText) findViewById(R.id.time1);
        branch = (EditText) findViewById(R.id.branch);
        year = (EditText) findViewById(R.id.year);
        reason = (EditText) findViewById(R.id.editTextTextMultiLine);
        p= new ProgressDialog(this);
        ud = new UserData2();
        time.setText(String.valueOf(date1));
        time.setEnabled(false);

      /*  mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference();
        databaseReference.child("User Information").addListenerForSingleValueEvent(new ValueEventListener() {
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

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        rollno.setText(tname);
        phno.setText(tphone);
        branch.setText(tbranch);
        year.setText(tyear);

        getSupportActionBar().hide();
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");
        long millis = System.currentTimeMillis();
         date = new java.sql.Date(millis);
         currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
       // Toast.makeText(getApplicationContext(),"permission started",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(view==back)
        {
            finish();
            Toast.makeText(getApplicationContext(),"Back pressed",Toast.LENGTH_SHORT).show();
        }
        if(view == submit)
        {
            submition();
        }
    }

    private void submition() {
        String a = rollno.getText().toString();
        String b = String.valueOf(currentDateTimeString);
        String c = reason.getText().toString();
        String d =phno.getText().toString();
        if(a.equals("") )
        {
            rollno.setError("enter Roll no");
        }
        else if(c.equals(""))
        {
            reason.setError(" enter reason");
        }
        else if(d.equals("")|d.length()<10)
        {
            phno.setError("enter valid phone number");
        }
        else
        {
            Toast.makeText(getApplicationContext(),"permission started",Toast.LENGTH_SHORT).show();
            ud.setRollno(a);
            ud.setTime(b);
            ud.setReason(c);
            ud.setNumber(d);
            ud.setStatus(null);

            p.setMessage("Please wait uploading...");
            p.setTitle("Registration");
            p.setCanceledOnTouchOutside(false);
            p.show();
            databaseReference.child(String.valueOf(date)).child(ud.getRollno()).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Uploaded successfully",Toast.LENGTH_SHORT).show();
                    p.dismiss();
                    finish();
                }
            });
        }
    }
}