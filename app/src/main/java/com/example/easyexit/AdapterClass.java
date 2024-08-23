package com.example.easyexit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder>{

    ArrayList<UserData2> list;
    AlertDialog.Builder builder;
    int cnt=0;
    long mill = System.currentTimeMillis();

    FirebaseDatabase mdata;
    DatabaseReference databaseReference;
    UserData2 ud=null;
    Context context;
    String role;
    boolean option;
    public AdapterClass(ArrayList<UserData2> list, Context context,String role,boolean option)
    {
        this.list = list;
        this.context=context;
        this.role = role;
        this.option = option;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<UserData2> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items,viewGroup,false);
        return new MyViewHolder(view);
    }
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int i) {
        holder.id.setText(String.valueOf(list.get(i).getRollno()));
        holder.desc.setText(String.valueOf(list.get(i).getTime()));
        holder.reason.setText("  "+ list.get(i).getReason());
        mdata = FirebaseDatabase.getInstance();
        databaseReference = mdata.getReference().child("Out Data");
        Date date = new Date(mill);

       if(String.valueOf(list.get(i).getStatus()).equals("Approved")){
            holder.approve.setText("Approved");}
        else if(String.valueOf(list.get(i).getStatus()).equals("Rejected")){
            holder.approve.setText("Rejected");
        }
        else{
            holder.approve.setText("Waiting");
        }

        if(list.get(i).getOutTime()==null)
        {
            holder.status.setText("Not Yet Exited");
            holder.status.setTextColor(R.color.red);
        }
        else
        {
            holder.status.setText("Checked Out");
        }
        try {
//            String listDateTimeString = String.valueOf(list.get(i).getTime());
//            String formattedListDate = getDate(listDateTimeString);
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//            String formattedTodayDate = dateFormat.format(date);
            if(role.equals("Faculty data") && option && list.get(i).getOutTime()==null) {
                holder.profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (String.valueOf(list.get(i).getStatus()).equals("waiting") || String.valueOf(list.get(i).getStatus()).equals("Rejected")|| String.valueOf(list.get(i).getStatus()).equals("Approved")) {

                            ud = new UserData2();

                            ud.setNumber(list.get(i).getNumber());
                            ud.setTime(list.get(i).getTime());
                            ud.setRollno(list.get(i).getRollno());
                            ud.setReason(list.get(i).getReason());
                            ud.setStatus(list.get(i).getStatus());

                            builder = new AlertDialog.Builder(view.getContext());

                           builder.setMessage(" Reason :" + ud.getReason() + "  " + "Grant permission..?");
                            builder.setTitle("Permission");
                            builder.setCancelable(true);
                            builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                ud.setStatus("Approved");
                                databaseReference.child(String.valueOf(date)).child(list.get(i).getRollno()).setValue(ud);
                                holder.approve.setText("Approved");
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
            if(role.equals("Security data")  && option){
                holder.profile.setOnClickListener(new View.OnClickListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        holder.profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cnt=1;
//                Intent i = new Intent(view.getContext(),permission.class);
//                view.getContext().startActivity(i);
//            }
//        });
    }

    private String getDate(String listDateTimeString) {
        StringBuilder sub= new StringBuilder();
        Map<String, String> monthMap = new HashMap<String, String>() {{
            put("jan", "01");
            put("feb", "02");
            put("mar", "03");
            put("apr", "04");
            put("may", "05");
            put("jun", "06");
            put("jul", "07");
            put("aug", "08");
            put("sep", "09");
            put("oct", "10");
            put("nov", "11");
            put("dec", "12");
        }};
        String[] parts = listDateTimeString.split(" ");
        if(parts.length<3){
            return "";
        }
        sub.append(parts[0]).append("-");
        sub.append(monthMap.get(parts[1].toLowerCase())).append("-");
        sub.append(parts[2]);
        return sub.toString();
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
