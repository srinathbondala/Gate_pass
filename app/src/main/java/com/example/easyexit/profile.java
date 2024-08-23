package com.example.easyexit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.Objects;

public class profile extends AppCompatActivity implements View.OnClickListener  {
    private UserData1 data;
    private EditText name, email,address,phone,branch,year;
    private ProgressDialog p;
    private ImageView imageView13;
    private Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Objects.requireNonNull(getSupportActionBar()).hide();
        logout = (Button) findViewById(R.id.logoutbt);
        imageView13 = (ImageView) findViewById(R.id.imageView13);
        imageView13.setOnClickListener(this);
        logout.setOnClickListener(this);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String s = sharedPreferences.getString("userData",null);
        String role = sharedPreferences.getString("userRole",null);
        if(s != null){
            data= gson.fromJson(s,UserData1.class);
        }
        loadProfileData(data);
    }

    private void loadProfileData(UserData1 data) {
        name = (EditText) findViewById(R.id.name1);
        email = (EditText) findViewById(R.id.email1);
        address = (EditText) findViewById(R.id.address1);
        phone = (EditText) findViewById(R.id.phno1);
        branch = (EditText) findViewById(R.id.department);
        year = (EditText) findViewById(R.id.year1);
        name.setText(data.getName());
        email.setText(data.getEmail());
        address.setText((data.getAddress()!=null)?data.getAddress():"NAN");
        phone.setText(data.getPhoneNumber());
        branch.setText(data.getBranch());
        year.setText(data.getYear());
    }

    private void logout_fun(){
        Intent i;
        p= new ProgressDialog(this);
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

    @Override
    public void onClick(View v) {
        if(v==logout){
            logout_fun();
        }
        else if( v == imageView13){
            finish();
        }
    }
}