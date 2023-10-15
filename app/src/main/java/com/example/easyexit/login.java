package com.example.easyexit;

import static com.example.easyexit.MainActivity.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import java.util.Objects;

public class login extends AppCompatActivity implements View.OnClickListener {

    EditText email1,pass1;
    Button loginbt;
    TextView text,title;
    String emailpatt="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emp = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    Intent i;
    public static String tname="",temail="",tbranch="",tphone="",tyear="",tfacaltyno="";

    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;

    ProgressDialog p;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        email1 = (EditText) findViewById(R.id.logemail);
        pass1 = (EditText) findViewById(R.id.loginpass);
        title = (TextView)findViewById(R.id.textView11);
        loginbt = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.textView4);
        loginbt.setOnClickListener(this);
        text.setOnClickListener(this);
        p= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference();
        if(a.equals("admin1"))
            title.setText("faculty");
        else if(a.equals("user1"))
                title.setText("User");
        else if(a.equals("Security"))
            title.setText("Security");
    }

    @Override
    public void onClick(View view) {
        if(view==text) {
            String email = email1.getText().toString();
            if(!email.matches(emailpatt)&&!email.matches(emp))
            {
                email1.setError("Enter Context Email");
            }
            else {
                p.setMessage("Please wait sending link...");
                p.setTitle("Password reset");
                p.setCanceledOnTouchOutside(false);
                p.show();
                FirebaseAuth.getInstance().sendPasswordResetEmail(email1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Check your Email", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "password reset Link Sent", Toast.LENGTH_LONG).show();
                        p.dismiss();
                    }
                });
            }
        }
        if(view==loginbt)
        {
            i= new Intent(getApplicationContext(), Register.class);
            Toast.makeText(getApplicationContext(),"logging",Toast.LENGTH_LONG).show();
            String email = email1.getText().toString();
            String password = pass1.getText().toString();
            if(email.equals("register@gmail.com") && password.equals("myregestration")){
                startActivity(i);
                email1.setText("");
                pass1.setText("");}
            if(!email.matches(emailpatt)&&!email.matches(emp))
            {
                email1.setError("Enter Context Email");
            }
            else if(password.isEmpty()||password.length()<6) {
                pass1.setError("Enter Proper password");
            }
            else {
                p.setMessage("Please wait Logging in");
                p.setTitle("Login");
                p.setCanceledOnTouchOutside(false);
                p.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           if(a.equals("user1"))
                           {
                               databaseReference.child("User Information").addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    UserData1 i1 = ds.getValue(UserData1.class);
                                                    if (i1 != null && i1.getEmail().equals(email)) {
                                                        temail = email;
                                                        tname = i1.getName();
                                                        tbranch = i1.getBranch();
                                                        tphone = i1.getPhoneNumber();
                                                        tyear = i1.getYear();
                                                        tfacaltyno =i1.getFacaltyno();
                                                        p.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Login Done", Toast.LENGTH_SHORT).show();
                                                        i = new Intent(getApplicationContext(), User.class);
                                                        finish();
                                                        startActivity(i);
                                                    }
                                                }
                                            }
                                       else  Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                   }
                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {
                                       Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                           else if(a.equals("admin1"))
                           {
                               databaseReference.child("Faculty data").addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if(snapshot.exists())
                                       {
                                           for (DataSnapshot ds : snapshot.getChildren()) {
                                               UserData1 i1 = ds.getValue(UserData1.class);
                                               if (i1 != null && i1.getEmail().equals(email)) {
                                                   temail = email;
                                                   tname = i1.getName();
                                                   tbranch = i1.getBranch();
                                                   tphone = i1.getPhoneNumber();
                                                   tyear = i1.getYear();
                                                   p.dismiss();
                                                   Toast.makeText(getApplicationContext(), "Login Done", Toast.LENGTH_SHORT).show();
                                                   i = new Intent(getApplicationContext(), Admin.class);
                                                   finish();
                                                   startActivity(i);
                                               }
                                           }
                                       }
                                       else  Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                   }
                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {
                                       Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                           else if(a.equals("Security"))
                           {
                               databaseReference.child("Security data").addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if(snapshot.exists())
                                       {
                                           for (DataSnapshot ds : snapshot.getChildren()) {
                                               UserData1 i1 = ds.getValue(UserData1.class);
                                               if (i1 != null && i1.getEmail().equals(email)) {
                                                   temail = email;
                                                   tname = i1.getName();
                                                   tbranch = i1.getBranch();
                                                   tphone = i1.getPhoneNumber();
                                                   tyear = i1.getYear();
                                                   p.dismiss();
                                                   Toast.makeText(getApplicationContext(), "Login Done", Toast.LENGTH_SHORT).show();
                                                   i = new Intent(getApplicationContext(), security.class);
                                                   finish();
                                                   startActivity(i);
                                               }
                                               else Toast.makeText(getApplicationContext(), "failed to Login", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                       else  Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                   }
                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {
                                       Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                           else
                               Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                            p.dismiss();
                        }
                    }
                });
            }
        }
    }
}