package com.example.easyexit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class requested extends AppCompatActivity {

    ArrayList<UserData2> lists;
    AdapterClass adapterClass;
    SearchView search;
    RecyclerView list;
    SwipeRefreshLayout swipeRefreshLayout;
    UserData2 ud;

    FirebaseUser muser;
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    java.sql.Date date;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested);
        search = (SearchView) findViewById(R.id.search);
        list = (RecyclerView) findViewById(R.id.recycle);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.cont4);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
        lists = new ArrayList<>();
        adapterClass = new AdapterClass(lists);
        ArrayAdapter<UserData2> adapter = new ArrayAdapter<>(requested.this, R.layout.items, lists);
        list.setAdapter(adapterClass);
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");
        long millis = System.currentTimeMillis();
        date = new java.sql.Date(millis);
        databaseReference = databaseReference.child(String.valueOf(date));
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapterClass.notifyDataSetChanged();
            finish();
            startActivity(getIntent());
            swipeRefreshLayout.setRefreshing(false);
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
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
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            lists.clear();
            if (snapshot.exists()) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserData2 i = ds.getValue(UserData2.class);
                    lists.add(i);
                }
                adapterClass.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    private void search(String s) {
        ArrayList<UserData2> mylist = new ArrayList<>();
        for (UserData2 object : lists) {
            if (object.getRollno().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                mylist.add(object);
            }
        }
        AdapterClass adapterClass = new AdapterClass(mylist);
        list.setAdapter(adapterClass);
    }
}