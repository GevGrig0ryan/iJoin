package com.example.ijoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchedActivity extends AppCompatActivity {
    ImageView profilePic;
    TextView name_surname, interests, emaiL;
    private RecyclerView recyclerView;
    private CompanyProfilePostAdapter adapter;
    private List<Post> posts;
    private FileAdapter fileAdapter;
    private List<Upload> uploadList;
    LinearLayout cert;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched);
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String name = bundle.getString("name");
        String imageUrl = bundle.getString("profileImageUrl");
        String dataAboutuser = bundle.getString("dataAboutUser");
        String userType = bundle.getString("usertype");
        String userId = bundle.getString("userId");
        cert = findViewById(R.id.cert);
        profilePic = findViewById(R.id.profile_image_view);
        name_surname = findViewById(R.id.name_surname);
        interests = findViewById(R.id.interests);
        emaiL = findViewById(R.id.email);
        recyclerView = findViewById(R.id.post);
        Log.e("SearchedActivity", "onCreate: " + imageUrl);
        if(imageUrl != null){
            Glide.with(this)
                    .load(imageUrl)
                    .into(profilePic);
        }
        name_surname.setText(name);
        interests.setText(dataAboutuser);
        emaiL.setText(email);
        Log.e("SearchedActivity ", "CompanyID " + bundle.getString("userId"));
        if(Objects.equals(userType, "Comp")){
            posts = new ArrayList<>();
            cert.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CompanyProfilePostAdapter(posts);
            recyclerView.setAdapter(adapter);
            loadCompanyPosts(userId);
        }/*
        else {
            uploadList = new ArrayList<>();
            fileAdapter = new FileAdapter(uploadList);
            fileContainerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            fileContainerRecyclerView.setAdapter(fileAdapter);

            loadUploadedFiles(userId);
        }*/
    }
    private void loadCompanyPosts(String companyId) {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("users").child(companyId).child("posts");
        postsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                posts.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                        Log.e("SearchedActivity", "Post: " + post);
                    }
                }
                adapter.setPosts(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ProfileFragment", "Error loading posts", databaseError.toException());
            }
        });
    }
    private void loadUploadedFiles(String userId) {
        DatabaseReference userFilesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("files");
        userFilesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadList.clear();
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = fileSnapshot.getValue(Upload.class);
                    if (upload != null) {
                        uploadList.add(upload);
                    }
                }
                fileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SearchedActivity", "Error loading files", databaseError.toException());
            }
        });
    }
}