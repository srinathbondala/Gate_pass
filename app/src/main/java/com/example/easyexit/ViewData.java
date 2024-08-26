package com.example.easyexit;

import static com.example.easyexit.User.reqroll;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ViewData extends AppCompatActivity {

    ArrayList<UserData2> lists,rejList,waitList;
    AdapterClass adapterClass,reject_adapter,waiting_adapter;
    SearchView search;
    ImageView imageView;
    RecyclerView list,reject_list,waiting_list;
//    SwipeRefreshLayout swipeRefreshLayout;
    UserData2 ud;
    ProgressDialog p;
    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    java.sql.Date date;
    private String userRole;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        search = (SearchView) findViewById(R.id.search);
        list = (RecyclerView) findViewById(R.id.recycle);
        reject_list = (RecyclerView) findViewById(R.id.reject_recycle);
        waiting_list = (RecyclerView) findViewById(R.id.waiting_recycle);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.cont4);
        p= new ProgressDialog(this);
        p.setMessage("Please Wait Loading Data");
        p.setTitle("Getting Data");
        p.setCanceledOnTouchOutside(false);
        p.show();
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        waiting_list.setLayoutManager(new LinearLayoutManager(this));
        waiting_list.setHasFixedSize(true);
        reject_list.setLayoutManager(new LinearLayoutManager(this));
        reject_list.setHasFixedSize(true);
        Objects.requireNonNull(getSupportActionBar()).hide();
        lists = new ArrayList<>();
        rejList = new ArrayList<>();
        waitList = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userRole = sharedPreferences.getString("userRole", null);

        reject_adapter = new AdapterClass(rejList,getApplicationContext(),userRole,true);
        waiting_adapter = new AdapterClass(waitList,getApplicationContext(),userRole,true);
        adapterClass = new AdapterClass(lists,getApplicationContext(),userRole,true);
        list.setAdapter(adapterClass);
        waiting_list.setAdapter(waiting_adapter);
        reject_list.setAdapter(reject_adapter);
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");
        long millis = System.currentTimeMillis();
        date = new java.sql.Date(millis);
        databaseReference = databaseReference.child(String.valueOf(date));
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onRefresh() {
//                adapterClass.notifyDataSetChanged();
////                finish();
////                startActivity(getIntent());
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Query query;
        if(Objects.equals(getIntent().getStringExtra("data"), "userdata"))
        {
            try{
                waiting_list.setVisibility(View.GONE);
                reject_list.setVisibility(View.GONE);
                TextView textView = findViewById(R.id.textView16);
                TextView textView1 = findViewById(R.id.textView18);
                TextView textView2 = findViewById(R.id.textView19);
                textView1.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView.setText("View Status");
                lists.clear();
                query = mdata.getReference().child("Out Data").child(String.valueOf(date)).orderByChild("rollno").equalTo(String.valueOf(reqroll));
                //"2023-01-05"
                query.addListenerForSingleValueEvent(valueEventListener);
                if(lists.isEmpty()){
                    list.setBackground(ContextCompat.getDrawable(ViewData.this,R.drawable.no_data));
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show(); p.dismiss();}
        }
        else if(Objects.equals(getIntent().getStringExtra("data"), "AdminData")) {
            lists.clear();
            query = mdata.getReference().child("Out Data").child(String.valueOf(date));//.orderByChild("status").equalTo("Approved");
//            query.addListenerForSingleValueEvent(valueEventListener);
            query.addListenerForSingleValueEvent(getDataEvent);
        }
        else if(Objects.equals(getIntent().getStringExtra("data"), "securityData")){
            lists.clear();
            waiting_list.setVisibility(View.GONE);
            reject_list.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.textView16);
            TextView textView1 = findViewById(R.id.textView18);
            TextView textView2 = findViewById(R.id.textView19);
            textView1.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            textView.setText("Out Data");
            query = mdata.getReference().child("Out Data").child(String.valueOf(date)).orderByChild("status").equalTo("Approved");
//            query.addListenerForSingleValueEvent(valueEventListener);
            query.addListenerForSingleValueEvent(valueEventListener);
        }
        else {
            try{
                databaseReference.addListenerForSingleValueEvent(valueEventListener);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
                p.dismiss();
            }
        }
        if (search != null) {
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    ValueEventListener getDataEvent = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            lists.clear();
            rejList.clear();
            waitList.clear();
            try{
                if(snapshot.exists()){
                    for( DataSnapshot ds : snapshot.getChildren()){
                        UserData2 i = ds.getValue(UserData2.class);
                        assert i != null;
                        if(i.getStatus().equals("Approved")){
                            lists.add(i);
                        } else if (i.getStatus().equals("Rejected")) {
                            rejList.add(i);
                        } else{
                            waitList.add(i);
                        }
                    }
                }
                if(lists.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "No Approval data available", Toast.LENGTH_LONG).show();
                    list.setBackground(ContextCompat.getDrawable(ViewData.this,R.drawable.no_data));
                }
                if (rejList.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "No Rejection data available", Toast.LENGTH_LONG).show();
                    reject_list.setBackground(ContextCompat.getDrawable(ViewData.this,R.drawable.no_data));
                }
                if(waitList.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "No Waiting data available", Toast.LENGTH_LONG).show();
                    waiting_list.setBackground(ContextCompat.getDrawable(ViewData.this,R.drawable.no_data));
                }
                adapterClass.notifyDataSetChanged();
                reject_adapter.notifyDataSetChanged();;
                waiting_adapter.notifyDataSetChanged();
                p.dismiss();
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(),"error occurred",Toast.LENGTH_SHORT).show();
                p.dismiss();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            p.dismiss();
        }
    };

    ValueEventListener valueEventListener = new ValueEventListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot){
                lists.clear();
               try {
                   if (snapshot.exists()) {
                       for (DataSnapshot ds : snapshot.getChildren()) {
                           UserData2 i = ds.getValue(UserData2.class);
                           lists.add(i);
                       }
                       if(lists.isEmpty())
                       {
                           Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_LONG).show();
                       }
                       adapterClass.notifyDataSetChanged();
                   }
               }
                catch (Exception e) { Toast.makeText(getApplicationContext(),"error occurred",Toast.LENGTH_SHORT).show();}
               p.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            p.dismiss();
        }
    };


    private void search(String s) {
        ArrayList<UserData2> mylist = new ArrayList<>();
        /*if (Objects.equals(bag, "waiting")) {
            for (UserData2 object : lists1) {
                if (object.getRollno().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                    mylist.add(object);
                }
            }
        }
        else {*/
            for (UserData2 object : lists) {
                if (object.getRollno().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                    mylist.add(object);
                }
            }
        AdapterClass adapterClass = new AdapterClass(mylist,getApplicationContext(),userRole,true);
        list.setAdapter(adapterClass);
    }
}


