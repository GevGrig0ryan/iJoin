package com.example.ijoin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Welcome extends AppCompatActivity {
    LinearLayout linearLayout;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoInternetDialog();
        }
        linearLayout = findViewById(R.id.welcome);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    // Initialize Firebase Realtime Database reference
                    Intent intent = new Intent(Welcome.this, SignIn.class);
                    startActivity(intent);
                    finish();
                } else {
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
                                    Intent intent = new Intent(Welcome.this, VolActivity.class);
                                    startActivity(intent);
                                }
                                else if(Objects.equals(userType, "Comp")){
                                    Intent intent = new Intent(Welcome.this, CompActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(Welcome.this, AdminActivity.class);
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
            }
        });
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please check your internet connection and try again.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open device settings to enable internet
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancellation
                finish(); // Optionally, you can finish the activity or take other actions
            }
        });
        builder.setCancelable(false); // Prevent dismissing the dialog by clicking outside

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}