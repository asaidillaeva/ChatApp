package com.example.chatapp.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.PhoneActivity;
import com.example.chatapp.R;
import com.example.chatapp.allNumbers.AllNumbersActivity;
import com.example.chatapp.chat.ChatActivity;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabContacts;
    RecyclerView recyclerView;
    ChatsAdapter adapter;
    private List<Chat> list = new ArrayList<>();
    private User user;
    private String TAG = "MainClass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, PhoneActivity.class));
            finish();
            return;
        }
        fabContacts = findViewById(R.id.fabNewChat);
        fabContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllNumbersActivity.class));
                finish();
            }
        });
        recyclerView = findViewById(R.id.recyclerViewChats);
        getChats();
        initList();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ChatsAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chat", list.get(position));
                startActivity(intent);
            }
        });
    }

    private void getChats() {
        String myId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("chats")
                .whereArrayContains("userIds", myId)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
//                        for (DocumentChange change : snapshots.getDocumentChanges()) {
//                            switch (change.getType()) {
//                                case ADDED:
//                                    Chat chat = change.getDocument().toObject(Chat.class);
//                                    if (chat != null) {
//                                        chat.setId(change.getDocument().getId());
//                                        getUserById(chat.getUserIds().get(0));
//                                        list.add(chat);
//                                    }
//                                    adapter.notifyDataSetChanged();
//                                    break;
//                            }
//                        }
//                    }
//                });
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        for (DocumentSnapshot snapshot : snapshots) {
                            Chat chat = snapshot.toObject(Chat.class);
                            if (chat != null) {
                                chat.setId(snapshot.getId());
                                list.add(chat);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void onClickSignOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out?");
        builder.setMessage("After signing out you should sign in one more time");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, PhoneActivity.class));
                finish();
            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
