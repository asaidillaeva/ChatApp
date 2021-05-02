package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText codeEditText;
    View getPhoneMode;
    View checkCodeMode;
    Button sendCode;
    Button checkCode;
    String phoneNumber;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String LOG_TAG = PhoneActivity.class.getSimpleName();
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        getPhoneMode = findViewById(R.id.getNumberMode);
        checkCodeMode = findViewById(R.id.checkCodeMode);
        codeEditText = findViewById(R.id.smsCodeEditText);
        phoneNumberEditText = findViewById(R.id.phoneEditText);
        sendCode = findViewById(R.id.sendCode);
        checkCode = findViewById(R.id.checkCode);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                showCodeView(forceResendingToken);
                verificationId = s;
            }
        };
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendCode();
                Log.e(LOG_TAG, "SendCode Button clicked");
            }
        });

        checkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckCode();
            }
        });

    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(PhoneActivity.this, ProfileActivity.class);
                    intent.putExtra("number", phoneNumber);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Authentication failed!" + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showCodeView(PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        getPhoneMode.setVisibility(View.GONE);
        checkCodeMode.setVisibility(View.VISIBLE);
    }

    public void onSendCode() {
        phoneNumber = phoneNumberEditText.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                60, TimeUnit.SECONDS, this, callbacks);
    }

    public void onCheckCode() {
        String code = codeEditText.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signIn(credential);
    }
}
