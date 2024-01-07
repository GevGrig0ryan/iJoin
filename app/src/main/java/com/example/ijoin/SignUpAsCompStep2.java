package com.example.ijoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpAsCompStep2 extends AppCompatActivity {
    Button btnSignUp;
    EditText editText;
    String name, email, password, usertype;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_comp_step2);
        Bundle bundle = getIntent().getExtras();
        database = FirebaseDatabase.getInstance();
        name = bundle.getString("name");
        email = bundle.getString("email");
        password = bundle.getString("password");
        usertype = bundle.getString("usertype");
        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.data_about_company);
        btnSignUp = findViewById(R.id.btn_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataAboutComp;
                dataAboutComp = String.valueOf(editText.getText());
                if(TextUtils.isEmpty(dataAboutComp)){
                    Toast.makeText(SignUpAsCompStep2.this, "Please, tell us something about your company.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpAsCompStep2.this, "Accont created", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpAsCompStep2.this, MainActivity2.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(SignUpAsCompStep2.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                FirebaseUser currentUser = mAuth.getCurrentUser();
                DatabaseReference usersRef = database.getReference().child("users");
                Users users = new Users();
                users.setName(name);
                users.setUserType(usertype);
                users.setDataAboutUser(dataAboutComp);
                users.setPassword(password);
                users.setEmail(email);
                usersRef.child(currentUser.getUid()).setValue(users);
            }
        });
    }
}