package com.example.easyexit;

import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.temail;
import static com.example.easyexit.login.tname;
import static com.example.easyexit.login.tphone;
import static com.example.easyexit.login.tyear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button user,admin,security1;
    TextView help;
    public static String a="";
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;

    public static String user_email="";
    Intent i;
    ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (Button) findViewById(R.id.user);
        admin = (Button) findViewById(R.id.facalty);
        security1 = (Button) findViewById(R.id.security1);
        help = (TextView) findViewById(R.id.textView3);

        getSupportActionBar().hide();
        user.setOnClickListener(this);
        admin.setOnClickListener(this);
        help.setOnClickListener(this);
        security1.setOnClickListener(this);
        p= new ProgressDialog(this);
        FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
        if (muser != null) {
            p.setMessage("Please wait Checking For Login...");
            p.setTitle("Loading");
            p.setCanceledOnTouchOutside(false);
            p.show();
        user_email = muser.getEmail();
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference();
        if(Objects.equals(a, "")) {
            databaseReference.child("User Information").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            UserData1 i1 = ds.getValue(UserData1.class);
                            if (i1 != null && i1.getEmail().equals(user_email)) {
                                temail = i1.getEmail();
                                tname = i1.getName();
                                tbranch = i1.getBranch();
                                tphone = i1.getPhoneNumber();
                                tyear = i1.getYear();
                                i = new Intent(getApplicationContext(), User.class);
                                finish();
                                startActivity(i);
                                a = "finish";
                                p.dismiss();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    p.dismiss();
                }
            });
        }
            if(!Objects.equals(a, "finish")) {
                databaseReference.child("Faculty data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserData1 i1 = ds.getValue(UserData1.class);
                                if (i1 != null && i1.getEmail().equals(user_email)) {
                                    temail = i1.getEmail();
                                    tname = i1.getName();
                                    tbranch = i1.getBranch();
                                    tphone = i1.getPhoneNumber();
                                    tyear = i1.getYear();
                                    i = new Intent(getApplicationContext(), Admin.class);
                                    finish();
                                    startActivity(i);
                                    a="not_Finished";
                                    p.dismiss();
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
                        p.dismiss();
                    }
                });
            }
            if(!Objects.equals(a, "not_Finished") && !Objects.equals(a, "finish"))
            {
                databaseReference.child("Security data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserData1 i1 = ds.getValue(UserData1.class);
                                if (i1 != null && i1.getEmail().equals(user_email)) {
                                    temail = i1.getEmail();
                                    tname = i1.getName();
                                    tbranch = i1.getBranch();
                                    tphone = i1.getPhoneNumber();
                                    tyear = i1.getYear();
                                    i = new Intent(getApplicationContext(), security.class);
                                    finish();
                                    startActivity(i);
                                    a="not_Finished";
                                    p.dismiss();
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
                        p.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == user)
        {
            a = "user1";
            i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
           // finish();
        }
        if(view == admin)
        {
            a = "admin1";
            i = new Intent(getApplicationContext(), login.class);
            startActivity(i);
           // finish();
        }
        if(view == security1)
        {
            a="Security";
            i= new Intent(getApplicationContext(),login.class);
            startActivity(i);
        }
        if(view == help) {
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cmrcet.ac.in"));
            startActivity(i);

        }
    }
}