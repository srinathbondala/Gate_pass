package com.example.easyexit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class FirebaseFunctions {
    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    UserData1 userData1 = null;

    public FirebaseFunctions() {
    }

    public void loginData(String role, String userEmail, DataCallback callback) {
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference();
        Query query = databaseReference.child(role).orderByChild("email").equalTo(userEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot firstChild = snapshot.getChildren().iterator().next();
                    UserData1 i1 = firstChild.getValue(UserData1.class);
                    if (i1 != null) {
                        callback.onDataFetched(i1);
                    } else {
                        callback.onError("No user found with the given email");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Error occurred while fetching data: " + error.getMessage());
            }
        });
    }

    ArrayList<notification_data> data;
    String userName="";
    public void loadNotifications(String name, Context context,FirebaseDataCallback callback) {
        try {
            mdata = FirebaseDatabase.getInstance();
            data = new ArrayList<>();
            Query query = mdata.getReference().child("Notification");

            // Set the value event listener
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.clear();
                    try {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String key = ds.getKey();
                            if (key != null) {
                                if (key.equals("Global")) {
                                    for (DataSnapshot notificationSnapshot : ds.getChildren()) {
                                        notification_data i = notificationSnapshot.getValue(notification_data.class);
                                        if (i != null) {
                                            data.add(i);
                                        }
                                    }
                                } else if (key.equals("Local")) {
                                    for (DataSnapshot notificationSnapshot : ds.getChildren()) {
                                        notification_data i = notificationSnapshot.getValue(notification_data.class);
                                        if (i != null && i.getFaculty() != null && i.getFaculty().equalsIgnoreCase(name)) {
                                            data.add(i);
                                        }
                                        else{
                                            Toast.makeText(context, ""+userName+" "+i.getFaculty(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        }
                        callback.onDataFetched(data);
                    } catch (Exception e) {
                        Log.d("error", "" + e.getMessage());
                        callback.onDataFetched(data);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onDataFetched(data);
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            callback.onDataFetched(data);
        }
    }

    ArrayList<notification_data> data1;
    public void loadGlobalNotifications( Context context,FirebaseDataCallback callback) {
        try {
            mdata = FirebaseDatabase.getInstance();
            data1 = new ArrayList<>();
            Query query = mdata.getReference().child("Notification").child("Global");

            // Set the value event listener
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data1.clear();
                    try {
                        if(snapshot.exists()) {
                            for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                                notification_data i = notificationSnapshot.getValue(notification_data.class);
                                if (i != null) {
                                    data1.add(i);
                                }
                            }
                        }
                        callback.onDataFetched(data1);
                    } catch (Exception e) {
                        Log.d("error", "" + e.getMessage());
                        callback.onDataFetched(data1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onDataFetched(data1);
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            callback.onDataFetched(data1);
        }
    }

    // Get Students who are not assigned to any faculty
    ArrayList<UserData1> userdata;
    public void loadStudentData( Context context,String faculty,getStudentsDetails callback) {
        try {
            mdata = FirebaseDatabase.getInstance();
            data1 = new ArrayList<>();
            Query query = mdata.getReference().child("User Information");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userdata.clear();
                    try {
                        if(snapshot.exists()) {
                            for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                                UserData1 i = studentSnapshot.getValue(UserData1.class);
                                if (i != null && i.getFaculty()==null || Objects.requireNonNull(i).getFaculty().isEmpty() || Objects.equals(i.getFaculty(), faculty)) {
                                    userdata.add(i);
                                }
                            }
                        }
                        callback.onDataFetched(userdata);
                    } catch (Exception e) {
                        Log.d("error", "" + e.getMessage());
                        callback.onDataFetched(userdata);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onDataFetched(userdata);
                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            callback.onDataFetched(userdata);
        }
    }
}














//    public void alternateMethod (){
        //            databaseReference.child("User Information").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            UserData1 i1 = ds.getValue(UserData1.class);
//                            if (i1 != null && i1.getEmail().equals(user_email)) {
//                                temail = i1.getEmail();
//                                tname = i1.getName();
//                                tbranch = i1.getBranch();
//                                tphone = i1.getPhoneNumber();
//                                tyear = i1.getYear();
//                                i = new Intent(getApplicationContext(), User.class);
//                                finish();
//                                startActivity(i);
//                                a = "finish";
//                                p.dismiss();
//                                break;
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    p.dismiss();
//                }
//            });
//        }
//    }

//        if(Objects.equals(a, "")) {
//            Query query = databaseReference.child("User Information").orderByChild("email").equalTo(user_email);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        // Get the first child of the snapshot
//                        DataSnapshot firstChild = snapshot.getChildren().iterator().next();
//                        UserData1 i1 = firstChild.getValue(UserData1.class);
//
//                        if (i1 != null) {
//                            temail = i1.getEmail();
//                            tname = i1.getName();
//                            tbranch = i1.getBranch();
//                            tphone = i1.getPhoneNumber();
//                            tyear = i1.getYear();
//                            i = new Intent(getApplicationContext(), User.class);
//                            finish();
//                            startActivity(i);
//                            a = "finish";
//                            p.dismiss();
//                        }
//                    } else {
//                        // Handle the case where no matching data was found
//                        Toast.makeText(getApplicationContext(), "No user found with the given email.", Toast.LENGTH_SHORT).show();
//                        p.dismiss();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    p.dismiss();
//                }
//            });
//        }
//            if(!Objects.equals(a, "finish")) {
//                databaseReference.child("Faculty data").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot ds : snapshot.getChildren()) {
//                                UserData1 i1 = ds.getValue(UserData1.class);
//                                if (i1 != null && i1.getEmail().equals(user_email)) {
//                                    temail = i1.getEmail();
//                                    tname = i1.getName();
//                                    tbranch = i1.getBranch();
//                                    tphone = i1.getPhoneNumber();
//                                    tyear = i1.getYear();
//                                    i = new Intent(getApplicationContext(), Admin.class);
//                                    finish();
//                                    startActivity(i);
//                                    a="not_Finished";
//                                    p.dismiss();
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
//                        p.dismiss();
//                    }
//                });
//            }
//            if(!Objects.equals(a, "not_Finished") && !Objects.equals(a, "finish"))
//            {
//                databaseReference.child("Security data").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            for (DataSnapshot ds : snapshot.getChildren()) {
//                                UserData1 i1 = ds.getValue(UserData1.class);
//                                if (i1 != null && i1.getEmail().equals(user_email)) {
//                                    temail = i1.getEmail();
//                                    tname = i1.getName();
//                                    tbranch = i1.getBranch();
//                                    tphone = i1.getPhoneNumber();
//                                    tyear = i1.getYear();
//                                    i = new Intent(getApplicationContext(), security.class);
//                                    finish();
//                                    startActivity(i);
//                                    a="not_Finished";
//                                    p.dismiss();
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(getApplicationContext(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
//                        p.dismiss();
//                    }
//                });
//            }
//        }
