package com.example.ijoin.ui.homevol;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ijoin.Post;
import com.example.ijoin.PostAdapter;
import com.example.ijoin.R;
import com.example.ijoin.SignIn;
import com.example.ijoin.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment-N";

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homevol, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.postview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Post> posts = new ArrayList<>();
        PostAdapter adapter = new PostAdapter(posts);
        recyclerView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users user = dataSnapshot.getValue(Users.class);
                    Log.i(TAG, "line: 68; DataSnapshot: " + dataSnapshot + "; User: " + user + ";");

                    if (user != null && "Vol".equals(user.getUserType())) {

                        List<String> volTypes = new ArrayList<>(user.getVolTypes().values());

                        if (!volTypes.isEmpty()) {

                            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

                            postsRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.i(TAG, "line: 84; dataSnapshot: " + dataSnapshot);

                                    for (DataSnapshot kindSnapshot : dataSnapshot.getChildren()) {
                                        for (DataSnapshot postSnapshot : kindSnapshot.getChildren()) {
                                            String postName = postSnapshot.child("postName").getValue(String.class);
                                            String description = postSnapshot.child("description").getValue(String.class);

                                            // Get volTypes from dataSnapshot // Bug found VolTypes type is HashMap instead of Arraylist
//                                            GenericTypeIndicator<HashMap<String, String>> typeIndicator = new GenericTypeIndicator<HashMap<String, String>>() {};
//                                            HashMap<String, String> volTypesMap = postSnapshot.child("volTypes").getValue(typeIndicator);
//                                            if (volTypesMap == null)
//                                                volTypesMap = new HashMap<>();
//                                            Log.i(TAG, "Line 96:  onDataChange: volTypesMap:" + volTypesMap);

                                            // Fetch profile picture URL from the corresponding user
                                            String companyUid = postSnapshot.child("comapnyUid").getValue(String.class);
                                            if (companyUid == null)
                                                companyUid = "NULL COMPANY NAME";
                                            Log.e("Homefragment", "CompantUid: " + companyUid);
                                            DatabaseReference companyRef = FirebaseDatabase.getInstance().getReference("users").child(companyUid);
                                            boolean f = true; // change to True by Narek to bypass volType check caused by invalid type of bug VolTypes default was false;
                                            for (String volType : volTypes) {
//                                                if(volTypesMap.contains(volType)){ // hashmap dont has contains method comment to dont cause bugs
//                                                    f = true;
//                                                    break;
//                                                }
                                            }
                                            if (f) {
                                                companyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        // Create a Post object and add to the list
                                                        Post post = new Post();
                                                        post.setPostName(postName);
                                                        post.setDescription(description);
                                                        post.setVolTypes(new HashMap<>()); // todo add volTypes
                                                        String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                                                        String companyname = dataSnapshot.child("name").getValue(String.class);
                                                        String companyuid = postSnapshot.child("comapnyUid").getValue(String.class);
                                                        if (profileImageUrl != null) {
                                                            post.setProfilePictureUrl(profileImageUrl);
                                                        }
                                                        boolean f1 = true;
                                                        post.setCompanyName(companyname);
                                                        post.setComapnyUid(companyuid);
                                                        for(Post p : posts){
                                                            if (p.getComapnyUid() == post.getComapnyUid() && p.getPostName() == post.getPostName() && p.getDescription() == post.getDescription()){
                                                                f1 = false;
                                                                break;
                                                            }
                                                        }
                                                        if(f1)posts.add(post);
//                                                        adapter.notifyDataSetChanged();
                                                        adapter.setPosts(posts);

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // Handle errors
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle errors
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }

        return view;
    }
}
