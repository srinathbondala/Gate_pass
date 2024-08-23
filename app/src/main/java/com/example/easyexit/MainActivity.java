package com.example.easyexit;

import static com.example.easyexit.login.tbranch;
import static com.example.easyexit.login.temail;
import static com.example.easyexit.login.tname;
import static com.example.easyexit.login.tphone;
import static com.example.easyexit.login.tyear;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    FirebaseFunctions firebaseFunctions;
    Button user,admin,security1;
    ImageView insta,google,whatsapp;
    AdapterViewFlipper flipper_item1;
    TextView help;
    public static String a="";
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private ArrayList<notification_data> data;
    private notificationAdapter adapterClass;
    int[] images ={R.drawable.img_12,R.drawable.cmric};

    public static String user_email="";
    Intent i;
    ProgressDialog p;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (Button) findViewById(R.id.user);
        admin = (Button) findViewById(R.id.facalty);
        security1 = (Button) findViewById(R.id.security1);
        help = (TextView) findViewById(R.id.Easy_Exit);
        google = (ImageView)findViewById(R.id.google);
        whatsapp = (ImageView)findViewById(R.id.whatsapp);
        insta = (ImageView)findViewById(R.id.insta);
        flipper_item1 = (AdapterViewFlipper) findViewById(R.id.flipper_item);
        CustomAdapter customAdapter = new CustomAdapter(this,images);
        flipper_item1.setAdapter(customAdapter);
        flipper_item1.setFlipInterval(3000);
        flipper_item1.setAutoStart(true);
        recyclerView = findViewById(R.id.events_recyclerView);

        getSupportActionBar().hide();
        user.setOnClickListener(this);
        admin.setOnClickListener(this);
        help.setOnClickListener(this);
        security1.setOnClickListener(this);
        insta.setOnClickListener(this);
        google.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        p= new ProgressDialog(this);
        firebaseFunctions = new FirebaseFunctions();
        FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
        if (muser != null) {
            p.setMessage("Please wait Checking For Login...");
            p.setTitle("Loading");
            p.setCanceledOnTouchOutside(false);
            p.show();
            user_email = muser.getEmail();
            mdata = FirebaseDatabase.getInstance();
            databaseReference = mdata.getReference();
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            String userJson = sharedPreferences.getString("userData", null);
            UserData1 userData1 = gson.fromJson(userJson,UserData1.class);
            String userRole1 = sharedPreferences.getString("userRole", null);;
            if(userData1==null){
                try {
                    if(Objects.equals(a,"")){
                        firebaseFunctions.loginData("User Information", muser.getEmail(), new DataCallback() {
                            @Override
                            public void onDataFetched(UserData1 userData) {
                                if(userData !=null){
                                    i = new Intent(getApplicationContext(), User.class);
                                    updateValues(userData,"User Information");
                                    a = "finish";
                                    p.dismiss();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), "No user found with the given email.", Toast.LENGTH_SHORT).show();
                                p.dismiss();
                            }
                        });
                    }
                    if(!Objects.equals(a, "finish")){
                        firebaseFunctions.loginData("Faculty data", muser.getEmail(), new DataCallback() {
                            @Override
                            public void onDataFetched(UserData1 userData) {
                                if(userData!=null){
                                    i = new Intent(getApplicationContext(), Admin.class);
                                    updateValues(userData,"Faculty data");
                                    a="not_Finished";
                                    p.dismiss();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), "No user found with the given email.", Toast.LENGTH_SHORT).show();
                                p.dismiss();
                            }
                        });
                    }
                    if(!Objects.equals(a, "not_Finished") && !Objects.equals(a, "finish")){
                        firebaseFunctions.loginData("Security data", muser.getEmail(), new DataCallback() {
                            @Override
                            public void onDataFetched(UserData1 userData) {
                                if(userData!=null){
                                    i = new Intent(getApplicationContext(), security.class);
                                    updateValues(userData,"Security data");
                                    p.dismiss();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "No user found with the given email.", Toast.LENGTH_SHORT).show();
                                    p.dismiss();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), "No user found with the given email.", Toast.LENGTH_SHORT).show();
                                p.dismiss();
                            }
                        });

                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                    p.dismiss();
                }
            }
            else{
                if(Objects.equals(userRole1, "Security data"))
                    i = new Intent(getApplicationContext(),security.class);
                else if(Objects.equals(userRole1, "User Information"))
                    i = new Intent(getApplicationContext(), User.class);
                else
                    i = new Intent(getApplicationContext(),Admin.class);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        temail = userData1.getEmail();
                        tname = userData1.getName();
                        tbranch = userData1.getBranch();
                        tphone = userData1.getPhoneNumber();
                        tyear = userData1.getYear();
                        startActivity(i);
                        finish();
                    }
                }, 2000);
            }
        }
        else{
            loadNotifications();
        }
    }

    private void loadNotifications() {
        FirebaseFunctions firebaseFunctions = new FirebaseFunctions();
        firebaseFunctions.loadGlobalNotifications(this, new FirebaseDataCallback() {
            @Override
            public void onDataFetched(ArrayList<notification_data> data) {
                if (data != null && !data.isEmpty()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapterClass = new notificationAdapter(data,getApplicationContext());
                    recyclerView.setAdapter(adapterClass);
                } else {
                    Toast.makeText(getApplicationContext(), "No notifications found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateValues(UserData1 resultData,String role){
        if(resultData!=null) {
            Gson gson = new Gson();
            SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
            String userVal = gson.toJson(resultData);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userData", userVal);
            editor.putString("userRole",role);
            editor.apply();
            temail = resultData.getEmail();
            tname = resultData.getName();
            tbranch = resultData.getBranch();
            tphone = resultData.getPhoneNumber();
            tyear = resultData.getYear();
            finish();
            startActivity(i);
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
        if(view == help || view == google) {
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cmrcet.ac.in"));
            startActivity(i);
        }
        if(view == insta)
        {
            Uri uri = Uri.parse("http://instagram.com/cmrcet_official");
            Intent i= new Intent(Intent.ACTION_VIEW,uri);

            i.setPackage("com.instagram.android");

            try {
                startActivity(i);
            } catch (ActivityNotFoundException e) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/cmrcet_official/?next=%2F")));

            }
        }
        if(view == whatsapp)
        {
            Uri uri = Uri.parse("http://linkedin.com/cmrcetofficia");


            Intent i= new Intent(Intent.ACTION_VIEW,uri);

            i.setPackage("com.linkedin.android");

            try {
                startActivity(i);
            } catch (ActivityNotFoundException e) {

                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://in.linkedin.com/school/cmrcetofficial/?original_referer=https%3A%2F%2Fwww.bing.com%2F")));

            }
        }
    }
}