package com.example.easyexit;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
