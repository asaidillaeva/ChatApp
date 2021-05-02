package com.example.chatapp;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private EditText nameEditText;
    private ImageView imageView;
    private EditText statusEditText;
    private String phoneNumber;
    private StorageReference mStorageRef;
    private static final int REQUEST_CODE = 2;
    private Uri photoPath;
    private String photoUrl;
    private UploadTask uploadtask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_avtivity);
        nameEditText = findViewById(R.id.nameEditText);
        imageView = findViewById(R.id.profileImage);
        statusEditText = findViewById(R.id.statusEditText);
        phoneNumber = getIntent().getStringExtra("number");
        mStorageRef = FirebaseStorage.getInstance().getReference("profilePhotos");
    }

    public void onSaveName(View view) {
        String name = nameEditText.getText().toString();
        if (name.trim().isEmpty()) {
            nameEditText.setText("Type your name");
            return;
        }
        String status = statusEditText.getText().toString().trim();
        uploadPhoto(photoPath);

        Map<String, Object> map = new HashMap<>();
        map.put("displayName", name);
        map.put("phoneNumber", phoneNumber);
        map.put("status", status);
        map.put("photoUrl", photoUrl);

        String userId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Retry saving", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    public void onClickImage(View view) {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.CATEGORY_APP_GALLERY);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data.getData() != null) {
            photoPath = data.getData();
            imageView.setImageURI(photoPath);
        }
    }

    public String getExtensions(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void uploadPhoto(Uri uri) {
        final StorageReference ref = mStorageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + "." + getExtensions(uri));
        uploadtask = ref.putFile(uri);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoUrl =uri.toString();
            }
        });


    }

}
