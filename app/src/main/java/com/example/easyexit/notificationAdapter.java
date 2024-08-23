package com.example.easyexit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.ViewHolder> {

    ArrayList<notification_data> list;
    Context context;
    public notificationAdapter(ArrayList<notification_data> list,Context context){
        this.list= list;
        this.context=context;
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
            holder.menue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "showing options", Toast.LENGTH_SHORT).show();
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
        TextView product_name,description;
        ImageView product_poster,menue;
        @SuppressLint("SetTextI18n")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name=itemView.findViewById(R.id.product_name);
            product_poster=itemView.findViewById(R.id.product_img);
            description=itemView.findViewById(R.id.description_item);
            menue=itemView.findViewById(R.id.delete_product);
        }
    }
}
