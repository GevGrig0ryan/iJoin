package com.example.ijoin;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpAsVolStep2 extends AppCompatActivity {
    Button btnNext;
    EditText editText;
    String name, surname,  email, password, usertype, a;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_vol_step2);
        editText = findViewById(R.id.data_about_user);
        btnNext = findViewById(R.id.btn_next);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        surname = bundle.getString("surname");
        email = bundle.getString("email");
        password = bundle.getString("password");
        usertype = bundle.getString("usertype");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataAboutUser;
                dataAboutUser = String.valueOf(editText.getText());
                if(TextUtils.isEmpty(dataAboutUser)){
                    Toast.makeText(SignUpAsVolStep2.this, "Please, tell us something about yourself", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(SignUpAsVolStep2.this, SignUpAsVolStep3.class);
                intent.putExtra("name", name);
                intent.putExtra("surname", surname);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("usertype", usertype);
                intent.putExtra("dataAboutUser", dataAboutUser);
                startActivity(intent);
            }
        });
    }
}