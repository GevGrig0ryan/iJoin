package com.example.ijoin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class PostStep2 extends AppCompatActivity {
    Button youthMentor, gardenGuru, petPal, techTutor, ecoHelper,foodHelper,onlineHelper, communityChamp, sportsCoach, redCrossYouth, other;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Button post;
    String name, description;
    int cnt = 0;
    Uri imageUri;
    boolean youth = false, garden = false, pet = false, tech = false, eco = false, food = false, online = false, community = false, sports = false, red = false, others = false;
    HashMap<String,String> voltypes = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_step2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
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
        post = findViewById(R.id.post);
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("postName");
        description = bundle.getString("description");
        //imageUri = bundle.getParcelable("imageUri");
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
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt == 0){
                    Toast.makeText(PostStep2.this, "Please choose a type of volunteering", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser currentUser = mAuth.getCurrentUser();
                DatabaseReference postRef = database.getReference().child("posts");
                //StorageReference imageRef = storage.getReference().child("post_images").child(currentUser.getUid()).child(name);
                Post post = new Post();
                post.setPostName(name);
                post.setDescription(description);
                post.setVolTypes(voltypes);
                post.setComapnyUid(currentUser.getUid());
                DatabaseReference db = database.getReference().child("users");
                db.child(currentUser.getUid()).child("posts").child(name).setValue(post);
                //post.setImageUri(imageUri.toString());
                /*imageRef.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Image uploaded successfully
                            // Get the download URL and save it to the Realtime Database
                            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                String downloadUrl = uri.toString();
                                postRef.child(currentUser.getUid()).child(name).child("imageUrl").setValue(downloadUrl);
                            }).addOnFailureListener(e -> {
                                // Handle failure to get download URL
                                Toast.makeText(PostStep2.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        })
                        .addOnFailureListener(e -> {
                            // Handle upload failure
                            Toast.makeText(PostStep2.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });*/
                for(Map.Entry<String, String> entry : voltypes.entrySet()){
                    String value = entry.getValue();
                    postRef.child(value).child(name).setValue(post);
                }
                Intent intent = new Intent(PostStep2.this, CompActivity.class);
                startActivity(intent);
            }
        });
    }
}