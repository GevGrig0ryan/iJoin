package com.example.ijoin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpAsCompStep1 extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextCompName, editTextConfirmPassword;
    Button btnNext;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_comp_step1);
        editTextCompName = findViewById(R.id.company_name);
        editTextEmail = findViewById(R.id.company_email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirm_password);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String compName, email, password, confPassword;
                compName = String.valueOf(editTextCompName.getText());
                email =String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confPassword = String.valueOf(editTextConfirmPassword.getText());
                if(TextUtils.isEmpty(compName)){
                    Toast.makeText(SignUpAsCompStep1.this, "Please, enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpAsCompStep1.this, "Please, enter the email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpAsCompStep1.this, "Please, enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6){
                    Toast.makeText(SignUpAsCompStep1.this, "Password must contain at least 6 symbols", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confPassword)){
                    Toast.makeText(SignUpAsCompStep1.this, "Please, confirm the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!confPassword.equals(password)){
                    Toast.makeText(SignUpAsCompStep1.this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SignUpAsCompStep1.this, SignUpAsCompStep2.class);
                intent.putExtra("name", compName);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("usertype", "Comp");
                startActivity(intent);
            }
        });
    }
}