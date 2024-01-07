package com.example.ijoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CompanyOrVolunteer extends AppCompatActivity {
    Button asvol, ascomp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_or_volunteer);
        asvol = findViewById(R.id.asVol);
        ascomp = findViewById(R.id.asComp);
        asvol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyOrVolunteer.this, SignIn.class);
                intent.putExtra("Usertype", "Vol");
                startActivity(intent);
            }
        });
        ascomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyOrVolunteer.this, SignIn.class);
                intent.putExtra("Usertype", "Comp");
                startActivity(intent);
            }
        });
    }
}