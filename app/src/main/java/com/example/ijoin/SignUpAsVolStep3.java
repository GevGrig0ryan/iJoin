package com.example.ijoin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpAsVolStep3 extends AppCompatActivity {
    Button youthMentor, gardenGuru, petPal, techTutor, ecoHelper,foodHelper,onlineHelper, communityChamp, sportsCoach, redCrossYouth, other;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    Button signUp;
    String email, password, name, surname, dataAboutUser, usertype;
    int cnt = 0;
    boolean youth = false, garden = false, pet = false, tech = false, eco = false, food = false, online = false, community = false, sports = false, red = false, others = false;
    HashMap <String,String> voltypes = new HashMap<String, String>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as_vol_step3);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        youthMentor = findViewById(R.id.youth_mentor);
        gardenGuru = findViewById(R.id.garden_guru);
        petPal = findViewById(R.id.pet_pal);
        techTutor = findViewById(R.id.tech_tutor);
        ecoHelper = findViewById(R.id.eco_helper);
        foodHelper = findViewById(R.id.food_helper);
        onlineHelper = findViewById(R.id.online_helper);
        communityChamp = findViewById(R.id.community_champ);
        sportsCoach = findViewById(R.id.sports_coach);
        redCrossYouth = findViewById(R.id.red_cross_youth);
        other = findViewById(R.id.other);
        signUp = findViewById(R.id.sign_up);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        surname = bundle.getString("surname");
        email = bundle.getString("email");
        password = bundle.getString("password");
        usertype = bundle.getString("usertype");
        dataAboutUser = bundle.getString("dataAboutUser");
        youthMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!youth){
                    youthMentor.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    youthMentor.setTextColor(getResources().getColor(R.color.white));
                    voltypes.put("youthMentor", "Youth Mentor");
                    cnt++;
                }
                else{
                    youthMentor.setBackgroundColor(getResources().getColor(R.color.white1));
                    youthMentor.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Youth Mentor"));
                }
                youth = !youth;
            }
        });
        gardenGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!garden){
                    gardenGuru.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    gardenGuru.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("gardenGuru", "Garden Guru");
                }
                else{
                    gardenGuru.setBackgroundColor(getResources().getColor(R.color.white1));
                    gardenGuru.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Garden Guru"));
                }
                garden = !garden;
            }
        });
        petPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pet){
                    petPal.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    petPal.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("petPal", "Pet Pal");
                }
                else{
                    petPal.setBackgroundColor(getResources().getColor(R.color.white1));
                    petPal.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Pet Pal"));
                }
                pet = !pet;
            }
        });
        techTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tech){
                    techTutor.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    techTutor.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("techTutor", "Tech Tutor");
                }
                else{
                    techTutor.setBackgroundColor(getResources().getColor(R.color.white1));
                    techTutor.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Tech Tutor"));
                }
                tech = !tech;
            }
        });
        ecoHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eco){
                    ecoHelper.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    ecoHelper.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("ecoHelper", "Eco Helper");
                }
                else{
                    ecoHelper.setBackgroundColor(getResources().getColor(R.color.white1));
                    ecoHelper.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Eco Helper"));
                }
                eco = !eco;
            }
        });
        foodHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!food){
                    foodHelper.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    foodHelper.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("foodHelper", "Food Helper");
                }
                else{
                    foodHelper.setBackgroundColor(getResources().getColor(R.color.white1));
                    foodHelper.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Food Helper"));
                }
                food = !food;
            }
        });
        onlineHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!online){
                    onlineHelper.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    onlineHelper.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("onlineHelper", "Online Helper");
                }
                else{
                    onlineHelper.setBackgroundColor(getResources().getColor(R.color.white1));
                    onlineHelper.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Online Helper"));
                }
                online = !online;
            }
        });
        communityChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!community){
                    communityChamp.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    communityChamp.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("communityChamp", "Community Champ");
                }
                else{
                    communityChamp.setBackgroundColor(getResources().getColor(R.color.white1));
                    communityChamp.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Community Champ"));
                }
                community = !community;
            }
        });
        sportsCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sports){
                    sportsCoach.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    sportsCoach.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("sportsCoach", "Sports Coach");
                }
                else{
                    sportsCoach.setBackgroundColor(getResources().getColor(R.color.white1));
                    sportsCoach.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Sports Coach"));
                }
                sports = !sports;
            }
        });
        redCrossYouth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!red){
                    redCrossYouth.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    redCrossYouth.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("redCrossYouth", "Red Cross Youth");
                }
                else{
                    redCrossYouth.setBackgroundColor(getResources().getColor(R.color.white1));
                    redCrossYouth.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Red Cross Youth"));
                }
                red = !red;
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!others){
                    other.setBackgroundColor(getResources().getColor(R.color.bc_green));
                    other.setTextColor(getResources().getColor(R.color.white));
                    cnt++;
                    voltypes.put("other", "Other");
                }
                else{
                    other.setBackgroundColor(getResources().getColor(R.color.white1));
                    other.setTextColor(getResources().getColor(R.color.bc_green));
                    cnt--;
                    voltypes.entrySet().removeIf(entry -> entry.getValue().equals("Other"));
                }
                others = !others;
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt == 0){
                    Toast.makeText(SignUpAsVolStep3.this, "Please choose a type of volunteering", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpAsVolStep3.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUpAsVolStep3.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpAsVolStep3.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                FirebaseUser currentUser = mAuth.getCurrentUser();
                DatabaseReference usersRef = database.getReference().child("users");
                Users users = new Users();
                users.setName(name);
                users.setSurname(surname);
                users.setUserType(usertype);
                users.setDataAboutUser(dataAboutUser);
                users.setPassword(password);
                users.setEmail(email);
                users.setVolTypes(voltypes);
                usersRef.child(currentUser.getUid()).setValue(users);
            }
        });
    }
}