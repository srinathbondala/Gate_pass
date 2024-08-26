package com.example.easyexit;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;


public class permission_data extends Fragment {
    private Button add_student;
    UserData1 userData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission_data, container, false);
        add_student = view.findViewById(R.id.register_user);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("UserData", MODE_PRIVATE);
        String userDataJson = sharedPreferences.getString("userData", null);
        if (userDataJson != null) {
            userData = gson.fromJson(userDataJson, UserData1.class);
        }
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(view.getContext(), Register.class);
                i.putExtra("data","student");
                i.putExtra("faculty",userData.getName());
                i.putExtra("facultyNo",userData.getFacaltyno());
                startActivity(i);
            }
        });
        return view;
    }
}