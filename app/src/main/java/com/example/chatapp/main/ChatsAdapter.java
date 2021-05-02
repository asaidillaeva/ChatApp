package com.example.chatapp.main;

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
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> {


    LayoutInflater inflater;
    List<Chat> list;
    OnItemClickListener onItemClick;

    public ChatsAdapter(Context context, List<Chat> list) {
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClick = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        TextView lastMessageTime;
        TextView lastMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(getAdapterPosition());
                }
            });
            name = itemView.findViewById(R.id.nameOfChat);
            imageView = itemView.findViewById(R.id.avatarka);
            lastMessageTime = itemView.findViewById(R.id.lastMessageTime);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }


        public void bind(final Chat chat) {
            FirebaseFirestore.getInstance().collection("chats")
                    .document(chat.getId())
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot snapshot) {
                    if(snapshot!=null) {
//                        Message m = snapshot.getDocuments().get(snapshot.size() - 1).toObject(Message.class);
//                        SimpleDateFormat df = new SimpleDateFormat("hh:mm");
//                        Date time = m.getTime().toDate();
//                        lastMessageTime.setText(df.format(time));
//                        lastMessage.setText(m.getText());
                    }
                }
            });

            FirebaseFirestore.getInstance().collection("users")
                    .document(chat.getUserIds().get(0))
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            name.setText(user.getDisplayName());
                            if (user.getPhotoUrl() != null) {
                                Glide.with(imageView.getContext())
                                        .load(user.getPhotoUrl())
                                        .override(90, 90)
                                        .circleCrop()
                                        .into(imageView);
                            }
                        }
                    });

        }
    }
}
