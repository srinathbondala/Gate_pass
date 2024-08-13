package com.example.easyexit;

import static com.example.easyexit.Admin.bag;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class prevView extends AppCompatActivity {
    Spinner spinner;
//    String[] dateValue={"2023-01-05","2023-01-06","2023-01-08","2023-01-09","2023-01-11","2023-01-12"};
    String[] dateValue={"1","2","3","4","5","6"};
    ArrayList<UserData2> lists;
    ArrayList<UserData2> lists1;
    AdapterClass adapterClass;
    SearchView search;
    RecyclerView list;
    SwipeRefreshLayout swipeRefreshLayout;
    UserData2 ud;
    ArrayAdapter arrayAdapter;
    ValueEventListener query;
    int js;

    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    java.sql.Date date;
    String date1;
    ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prev_view);
        search = (SearchView) findViewById(R.id.search);
        list = (RecyclerView) findViewById(R.id.recycle);
        spinner=(Spinner)findViewById(R.id.spinner);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.cont5);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        p= new ProgressDialog(this);
        getSupportActionBar().hide();
        lists = new ArrayList<>();
        lists1 = new ArrayList<>();
        adapterClass = new AdapterClass(lists);
        ArrayAdapter<UserData2> adapter = new ArrayAdapter<UserData2>(prevView.this, R.layout.items, lists);
        list.setAdapter(adapterClass);
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");
        p.setMessage("Please wait uploading...");
        p.setTitle("Registration");
        p.setCanceledOnTouchOutside(false);
        p.show();
      try{
          js=1;
          databaseReference.addValueEventListener(valueEventListener);
          query= databaseReference.limitToLast(6).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) { int j=0;
                        for (DataSnapshot ds : snapshot.getChildren()) {
                           if(j==6){break;}else {
                               String i = ds.getKey();
                               dateValue[j] = String.valueOf(i);
                               j++;
                           }
                        }
                        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, dateValue);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        spinner.setAdapter(arrayAdapter);
                    }
                }
                catch (Exception e) { Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
      catch (Exception e)
      {
          Toast.makeText(getApplicationContext(),""+e,Toast.LENGTH_SHORT).show();
      }
//        Query query;
//        query = mdata.getReference().child("Out Data").child(String.valueOf(dateValue[0]));
//        query.addListenerForSingleValueEvent(valueEventListener);
        long millis = System.currentTimeMillis();
        date = new java.sql.Date(millis);
        date1 = String.valueOf(date);
        try {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    date1 = dateValue[i];
                    Query query;
                   // Toast.makeText(getApplicationContext(), dateValue[i], Toast.LENGTH_LONG).show();
                    try{
                        lists.clear();
                        query = mdata.getReference().child("Out Data").child(String.valueOf(date1));
                        query.addListenerForSingleValueEvent(valueEventListener);}
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show(); }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show(); }
        databaseReference = databaseReference.child(String.valueOf(date));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                adapterClass.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        p.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query;
      if(Objects.equals(bag, "mydata"))
        {
            try{
                lists.clear();
                query = mdata.getReference().child("Out Data").child(String.valueOf(dateValue[0]));//.child(String.valueOf(date1));
                query.addListenerForSingleValueEvent(valueEventListener);}
            catch (Exception e){
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show(); }
        }
       else {
            try{
                databaseReference.addListenerForSingleValueEvent(valueEventListener);}
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
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
    ValueEventListener valueEventListener = new ValueEventListener() {
        @SuppressLint({"NotifyDataSetChanged", "UseCompatLoadingForDrawables"})
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot){
            lists.clear();
                    try {
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserData2 i = ds.getValue(UserData2.class);
                                lists.add(i);
                            }
                            if(lists.isEmpty()){
                                list.setBackground(getDrawable(R.drawable.help));
                            }
                            adapterClass.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
        AdapterClass adapterClass = new AdapterClass(mylist);
        list.setAdapter(adapterClass);
    }
}
