package com.example.chatapp.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Message> list;

    public MessageAdapter(Context context, List<Message> list) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView anotherTextView;
        TextView myTextView;
        TextView anotherTime;
        TextView myTime;
        View another;
        View my;
        TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.myTV);
            anotherTextView = itemView.findViewById(R.id.anotherTV);
            myTime = itemView.findViewById(R.id.myTime);
            anotherTime = itemView.findViewById(R.id.anotherTime);
            my = itemView.findViewById(R.id.my);
            another = itemView.findViewById(R.id.another);
            date = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(Message message) {
            Date d = message.getTime().toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            String time = sdf.format(d);

            if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
                myTime.setText(time);
                my.setVisibility(View.VISIBLE);
                another.setVisibility(View.GONE);
                myTextView.setText(message.getText());
            } else {
                anotherTime.setText(time);
                another.setVisibility(View.VISIBLE);
                my.setVisibility(View.GONE);
                anotherTextView.setText(message.getText());
            }
        }
    }
}
