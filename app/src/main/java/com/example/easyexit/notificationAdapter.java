package com.example.easyexit;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.ViewHolder> {

    ArrayList<notification_data> list;
    private String role;
    Context context;
    public notificationAdapter(ArrayList<notification_data> list,Context context,String role){
        this.list= list;
        this.context=context;
        this.role=role;
    }
    @NonNull
    @Override
    public notificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull notificationAdapter.ViewHolder holder, int position) {
        try {
            notification_data pdf = list.get(position);
            holder.product_name.setText(pdf.getTitle() +"\n"+ "(" + pdf.getFaculty() + ")");
            Picasso.get().load(pdf.getUrl()).into(holder.product_poster);
            holder.description.setText(pdf.getDescription());
            Gson gson = new Gson();
            UserData1 userData = null;
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserData", MODE_PRIVATE);
            String userDataJson = sharedPreferences.getString("userData", null);
            if (userDataJson != null) {
                userData = gson.fromJson(userDataJson, UserData1.class);
            }
            if(pdf.getTime()!=null){
                holder.time.setText(pdf.getTime());
            }
            else{
                holder.time.setText("");
            }
            if(!Objects.equals(role, "admin")|| !Objects.equals(Objects.requireNonNull(userData).getName(), pdf.getFaculty())){
                holder.menue.setVisibility(View.GONE);
            }
            holder.menue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(view.getContext(), "showing options", Toast.LENGTH_SHORT).show();
                    if (context instanceof Activity && !((Activity) context).isFinishing()) {
                        new AlertDialog.Builder(context)
                                .setTitle("Delete Notification")
                                .setMessage("Are you sure you want to delete this notification?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    deleteNotification(pdf.getTitle(), pdf.getFaculty(), pdf.getUrl(), pdf.getRange());
                                    dialog.dismiss();
                                })
                                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                                .show();
                    } else {
                        Toast.makeText(context, "Unable to show dialog", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e)
        {
            Toast.makeText(context, "error occurred please try later", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView product_name,description,time;
        ImageView product_poster,menue;
        @SuppressLint("SetTextI18n")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name=itemView.findViewById(R.id.product_name);
            product_poster=itemView.findViewById(R.id.product_img);
            description=itemView.findViewById(R.id.description_item);
            menue=itemView.findViewById(R.id.delete_product);
            time=itemView.findViewById(R.id.uploadtime);
        }
    }
    private void deleteNotification(String title, String faculty, String imageUrl, String Range) {

        NotificationManagerClass notificationManager = new NotificationManagerClass(context);
        notificationManager.deleteNotification(faculty, title, imageUrl,Range);

        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().equals(title) && list.get(i).getFaculty().equals(faculty)) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

}
