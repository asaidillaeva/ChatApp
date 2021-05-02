package com.example.chatapp.allNumbers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.chat.ChatActivity;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllNumbersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public AllNumbersAdapter adapter;
    private List<User> list = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_numbers);
        recyclerView = findViewById(R.id.recyclerViewAllNumbers);
        progressBar = findViewById(R.id.allProgressBar);

        getContacts();
        initList();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new AllNumbersAdapter(this, list);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent =new Intent(AllNumbersActivity.this, ChatActivity.class);
                intent.putExtra("user", list.get(position));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getContacts() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        for (DocumentSnapshot snapshot : snapshots) {
                            User user = snapshot.toObject(User.class);
                            if (user != null) {
                                user.setId(snapshot.getId());
                                list.add(user);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
        progressBar.setVisibility(View.GONE);
    }
}
