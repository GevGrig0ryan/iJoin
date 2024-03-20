package com.example.ijoin;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignIn extends AppCompatActivity {
    TextView textView, signUp, forgotPassword;
    Button btnLogin;
    EditText editTextEmail, editTextPassword;
    FirebaseAuth mAuth;
    String userType;
    DatabaseReference databaseReference;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signUp = findViewById(R.id.signup);
        textView = findViewById(R.id.login);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        mAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.forgot_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignIn.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignIn.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if (mAuth.getCurrentUser().isEmailVerified()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        Toast.makeText(getApplicationContext(), "Logged succesfully in", Toast.LENGTH_SHORT).show();
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                                        // Read user data
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.R)
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // Handle the data snapshot
                                                if (dataSnapshot.exists()) {
                                                    // Get user data
                                                    String userType = dataSnapshot.child("userType").getValue(String.class);// Add more fields as needed
                                                    if (Objects.equals(userType, "Vol")) {
                                                        Intent intent = new Intent(SignIn.this, VolActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(SignIn.this, CompActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                // Handle errors
                                                Log.e("Firebase", "Error reading user data", databaseError.toException());
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(SignIn.this, "Please, verify your email", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignIn.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, CompanyOrVolunteer.class);
                startActivity(intent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(SignIn.this, ForgottenPassword.class);
                    startActivity(intent);
            }
        });
    }
}