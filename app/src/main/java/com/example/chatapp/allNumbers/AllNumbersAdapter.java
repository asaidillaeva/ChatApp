package com.example.chatapp.allNumbers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.model.User;

import java.util.List;

public class AllNumbersAdapter extends RecyclerView.Adapter<AllNumbersAdapter.MyViewHolder> {


    LayoutInflater inflater;
    List<User> list;
    OnItemClickListener onItemClick;

    public AllNumbersAdapter(Context context, List<User> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.contact_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClick = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
        ImageView imageView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(getAdapterPosition());
                }
            });
            name = itemView.findViewById(R.id.nameOfChat);
            status = itemView.findViewById(R.id.lastMessage);
            imageView = itemView.findViewById(R.id.avatarka);
        }


        public void bind(User user) {
            name.setText(user.getDisplayName());
            status.setText(user.getStatus());
            if (user.getPhotoUrl()!= null){
                Glide.with(imageView.getContext())
                        .load(user.getPhotoUrl())
                        .override(90, 90)
                        .centerCrop()
                        .into(imageView);
            }
        }
    }
}
