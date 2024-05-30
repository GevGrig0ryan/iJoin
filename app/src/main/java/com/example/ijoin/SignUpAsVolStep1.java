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

public class SignUpAsVolStep1 extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextName, editTextSurname, editTextConfirmPassword;
    Button btnNext;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_vol_step1);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextName = findViewById(R.id.name);
        editTextSurname = findViewById(R.id.surname);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirm_password);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, surname,  email, password, confirmPassword;
                name = String.valueOf(editTextName.getText());
                surname = String.valueOf(editTextSurname.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmPassword = String.valueOf(editTextConfirmPassword.getText());
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignUpAsVolStep1.this, "Please, enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(surname)){
                    Toast.makeText(SignUpAsVolStep1.this, "Please, enter your surname", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignUpAsVolStep1.this, "Please, enter the email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpAsVolStep1.this, "Please, enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length() < 6){
                    Toast.makeText(SignUpAsVolStep1.this, "Password must contain at least 6 symbols", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(SignUpAsVolStep1.this, "Please, confirm the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!confirmPassword.equals(password)){
                    Toast.makeText(SignUpAsVolStep1.this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SignUpAsVolStep1.this, SignUpAsVolStep2.class);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                if(email.equals("ijoin.ad@gmail.com")){
                    intent.putExtra("usertype", "Admin");
                }
                else intent.putExtra("usertype", "Vol");
                startActivity(intent);
            }
        });
    }
}