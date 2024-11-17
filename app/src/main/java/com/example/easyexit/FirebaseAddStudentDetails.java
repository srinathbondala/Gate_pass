package com.example.easyexit;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAddStudentDetails {
    private final DatabaseReference mDatabaseReference;
    private final Context context;

    FirebaseAddStudentDetails(Context context){
        this.context = context;
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        this.mDatabaseReference = mDatabase.getReference().child("User Information");
    }

    public void updateStudentAcadamics(String Rollno, Acadamic_Info AcadamicData){
        mDatabaseReference.child(Rollno).child("info").setValue(AcadamicData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isCanceled()){
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Academic data updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
