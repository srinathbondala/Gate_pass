package com.example.easyexit;

import static com.example.easyexit.MainActivity.a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText name,phno,email,pass,section,ryear,rollnumber;
    Button bt;
    ImageView imageView7;
    TextView textView25;
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
        try{
            name = (EditText) findViewById(R.id.name1);
            phno = (EditText) findViewById(R.id.phno1);
            email=(EditText) findViewById(R.id.email1);
            pass=(EditText) findViewById(R.id.Password1);
            ryear=(EditText) findViewById(R.id.ryear);
            section=(EditText) findViewById(R.id.section);
            rollnumber = (EditText) findViewById(R.id.rollno);
            bt = (Button) findViewById(R.id.submit1);
            textView25 = findViewById(R.id.textView25);
            imageView7 = (ImageView) findViewById(R.id.imageView7);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            bt.setOnClickListener(this);
            imageView7.setOnClickListener(this);
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
            String reg = getIntent().getStringExtra("data");
            if(reg!=null) {
                if(reg.equals("admin")){
                    databaseReference = mdata.getReference().child("Faculty data");
                    textView25.setText("Admin Registration");
                }
                else if (reg.equals("student")) {
                    databaseReference = mdata.getReference().child("User Information");
                    textView25.setText("Student Registration");
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == imageView7){
            finish();
        }
        else if(view == bt){
            perforAuth();
        }
    }

    private void perforAuth() {
        String a1 = phno.getText().toString();
        String b = email.getText().toString();
        String c = pass.getText().toString();
        String d = name.getText().toString();
        String e = section.getText().toString();
        String f = ryear.getText().toString();
        String g = rollnumber.getText().toString();
        // String user=ud.getName();
        if(!b.matches(emailPat)&&!b.matches(emp))
        {
            email.setError("enter correct email");
        }
        else if(d.equals(""))
            name.setError("enter name");
        else if(a1.equals("")||a1.length()<10)
            phno.setError("enter valid phno");
        else if(b.equals(""))
            email.setError("enter email");
        else if(e.equals(""))
            email.setError("enter Branch and Section");
        else if(f.equals(""))
            email.setError("enter year");
        else if(c.equals("")||c.length()<6)
            pass.setError("enter valid password");
        else if(g.isEmpty())
            rollnumber.setError("enter roll number");
        else
        {
            ud.setPhoneNumber(a1);
            ud.setEmail(b);
            ud.setPassword(c);
            ud.setName(d);
            ud.setBranch(e);
            ud.setYear(f);
            ud.setRollno(g);
            String reg1 = getIntent().getStringExtra("faculty");
            String reg2 = getIntent().getStringExtra("facultyNo");
            if(reg1!=null && reg2!=null){
                ud.setFaculty(reg1);
                ud.setFacaltyno(reg2);
            }
            p.setMessage("Please wait Registering...");
            p.setTitle("Registration");
            p.setCanceledOnTouchOutside(false);
            p.show();

            mAuth.createUserWithEmailAndPassword(ud.getEmail(), ud.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        databaseReference.child(ud.getRollno()).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(reg1!=null){
                                    DatabaseReference db1 = mdata.getReference().child("Faculty data").child(ud.getName()).child("students_list");
                                    db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            List<String> students_list = new ArrayList<>();
                                            if(snapshot.exists()) {
                                                students_list = (List<String>) snapshot.getValue();
                                            }
                                            students_list.add(ud.getRollno());
                                            db1.setValue("students_list").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        p.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_SHORT).show();
                                                        i = new Intent(getApplicationContext(), login.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                    else{
                                                        rollbackUserData(ud.getRollno());
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            rollbackUserData(ud.getRollno());
                                        }
                                    });
                                }
                                else {
                                    p.dismiss();
                                    Toast.makeText(getApplicationContext(), "Registration completed", Toast.LENGTH_SHORT).show();
                                    i = new Intent(getApplicationContext(), login.class);
                                    startActivity(i);
                                    finish();
                                }
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
    private void rollbackUserData(String rollno) {
        databaseReference.child(rollno).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration failed, rollback successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Rollback failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
                p.dismiss();
            }
        });
    }
}