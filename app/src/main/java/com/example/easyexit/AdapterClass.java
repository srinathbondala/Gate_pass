package com.example.easyexit;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.easyexit.MainActivity.a;
import static com.example.easyexit.User.flag;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.type.Color;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder>{

    static String no,rno,time,status,reason;
    ArrayList<UserData2> list;
    AlertDialog.Builder builder;
    int cnt=0;
    long mill = System.currentTimeMillis();

    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    UserData2 ud=null;
    Intent intent;

    public AdapterClass(ArrayList<UserData2> list)
    {
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items,viewGroup,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int i) {
        holder.id.setText(String.valueOf(list.get(i).getRollno()));
        holder.desc.setText(String.valueOf(list.get(i).getTime()));
        holder.reason.setText("  "+String.valueOf(list.get(i).getReason()));
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");

       if(String.valueOf(list.get(i).getStatus()).equals("Approved")){
            holder.approve.setText("Approved");}
        else if(String.valueOf(list.get(i).getStatus()).equals("Rejected")){
            holder.approve.setText("Rejected");}
        else{
            holder.approve.setText("Waiting");}

        if(list.get(i).getOutTime()==null)
        {
            holder.status.setText("Not exited");
        }
        else
        {
            holder.status.setText("exited");
        }
        if(flag.equals("true")) {
            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (String.valueOf(list.get(i).getStatus()).equals("waiting") || String.valueOf(list.get(i).getStatus()).equals("Rejected")|| String.valueOf(list.get(i).getStatus()).equals("Approved")) {

                        java.util.Date date1 = new java.util.Date();
                        ud = new UserData2();

                        ud.setNumber(list.get(i).getNumber());
                        ud.setTime(list.get(i).getTime());
                        ud.setRollno(list.get(i).getRollno());
                        ud.setReason(list.get(i).getReason());
                        ud.setStatus(list.get(i).getStatus());

                        Date date = new Date(mill);
                        builder = new AlertDialog.Builder(view.getContext());

                       builder.setMessage(" Reason :" + ud.getReason() + "  " + "Grant permission..?");
                        builder.setTitle("Permission");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                            ud.setStatus("Approved");
                            databaseReference.child(String.valueOf(date)).child(list.get(i).getRollno()).setValue(ud);
                            holder.approve.setText("Approved");
                            //flag = "false";
                        });
                        builder.setNegativeButton("NO", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                            ud.setStatus("Rejected");
                            databaseReference.child(String.valueOf(date)).child(list.get(i).getRollno()).setValue(ud);
                            holder.approve.setText("Rejected");
                            Toast.makeText(view.getContext(), "Rejected", Toast.LENGTH_SHORT).show();
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });
        }
        if(flag.equals("false")){
            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (String.valueOf(list.get(i).getStatus()).equals("Approved")) {
                        java.util.Date date1 = new java.util.Date();
                        ud = new UserData2();

                        ud.setNumber(list.get(i).getNumber());
                        ud.setTime(list.get(i).getTime());
                        ud.setRollno(list.get(i).getRollno());
                        ud.setReason(list.get(i).getReason());
                        ud.setOutTime(String.valueOf(date1));
                        ud.setStatus(list.get(i).getStatus());

                        builder = new AlertDialog.Builder(view.getContext());
                        Date date = new Date(mill);
                        builder.setMessage("Did the person exit ?");
                        builder.setTitle("Alert !");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                            if (list.get(i).getOutTime() == null || String.valueOf(list.get(i).getOutTime()).equals(" ")) {
                                holder.status.setText("exited");
                                ud.setOutTime(String.valueOf(date1));
                                databaseReference.child(String.valueOf(date)).child(list.get(i).getRollno()).setValue(ud);
                                Toast.makeText(view.getContext(), "Time Registered successfully " + date1, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(view.getContext(), "Already Registered or status not updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("NO", (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.cancel();
                            Toast.makeText(view.getContext(), "Time did not Register ", Toast.LENGTH_SHORT).show();
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else
                        Toast.makeText(view.getContext(), "Not Approved", Toast.LENGTH_SHORT).show();
                }
            });
        }
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cnt=1;
                Intent i = new Intent(view.getContext(),permission.class);
                view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView id,desc,status,approve,reason;
        Button profile;
        @SuppressLint("SetTextI18n")
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id= itemView.findViewById(R.id.dealId);
            desc = itemView.findViewById(R.id.description);
            status = itemView.findViewById(R.id.status);
            approve = itemView.findViewById(R.id.approve);
            reason = itemView.findViewById(R.id.reason);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}
