package com.example.easyexit;

import static com.example.easyexit.MainActivity.a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText name,phno,email,pass,section,ryear;
    Button bt;
    Intent i;
    ProgressDialog p;
    String emailPat = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emp = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";
    String str;
    UserData1 ud=null;

    FirebaseAuth mAuth;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name1);
        phno = (EditText) findViewById(R.id.phno1);
        email=(EditText) findViewById(R.id.email1);
        pass=(EditText) findViewById(R.id.Password1);
        ryear=(EditText) findViewById(R.id.ryear);
        section=(EditText) findViewById(R.id.section);
        bt = (Button) findViewById(R.id.submit1);
        getSupportActionBar().hide();
        bt.setOnClickListener(this);
        p= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        muser = mAuth.getCurrentUser();
        mdata = FirebaseDatabase.getInstance();
        ud = new UserData1();
        if(a.equals("user1"))
            databaseReference = mdata.getReference().child("User Information");
        if(a.equals("admin1"))
            databaseReference = mdata.getReference().child("Faculty data");
        if(a.equals("Security"))
            databaseReference = mdata.getReference().child("Security data");

    }

    @Override
    public void onClick(View view) {
        perforAuth();
    }

    private void perforAuth() {
        String a = phno.getText().toString();
        String b = email.getText().toString();
        String c = pass.getText().toString();
        String d = name.getText().toString();
        String e = section.getText().toString();
        String f = ryear.getText().toString();
        // String user=ud.getName();
        if(!b.matches(emailPat)&&!b.matches(emp))
        {
            email.setError("enter correct email");
        }
        else if(d.equals(""))
            name.setError("enter name");
        else if(a.equals("")||a.length()<10)
            phno.setError("enter valid phno");
        else if(b.equals(""))
            email.setError("enter email");
        else if(e.equals(""))
            email.setError("enter Branch and Section");
        else if(f.equals(""))
            email.setError("enter year");
        else if(c.equals("")||c.length()<6)
            pass.setError("enter valid password");
        else
        {
            ud.setPhoneNumber(a);
            ud.setEmail(b);
            ud.setPassword(c);
            ud.setName(d);
            ud.setBranch(e);
            ud.setYear(f);
            p.setMessage("Please wait Registering...");
            p.setTitle("Registration");
            p.setCanceledOnTouchOutside(false);
            p.show();

            mAuth.createUserWithEmailAndPassword(ud.getEmail(), ud.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        databaseReference.child(ud.getName()).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                p.dismiss();
                                Toast.makeText(getApplicationContext(),"Registration completed",Toast.LENGTH_SHORT).show();
                                i= new Intent(getApplicationContext(), login.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        p.dismiss();
                    }
                }
            });
        }
    }
}