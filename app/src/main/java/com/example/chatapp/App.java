package com.example.chatapp;

import androidx.multidex.MultiDexApplication;

import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class App extends MultiDexApplication {
    public static User user;
    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static User getUserById(String s) {
        FirebaseFirestore.getInstance().collection("users")
                .document(s)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                    }
                });
        return user;
    }

    public static Map<String, Object> getLastMessage(String document) {
        final Map<String, Object> map = new HashMap<>();

        FirebaseFirestore.getInstance().collection("chats")
                .document(document)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        Message message = snapshots.getDocuments().get(snapshots.size() - 1).toObject(Message.class);
                        map.put("lastMessage", message.getText());
                        map.put("timestamp", message.getTime());
                    }
                });
        return map;
    }
}
