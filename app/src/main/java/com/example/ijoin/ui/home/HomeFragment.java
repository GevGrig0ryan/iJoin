package com.example.ijoin.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ijoin.CompanyPostAdapter;
import com.example.ijoin.Post;
import com.example.ijoin.PostStep1;
import com.example.ijoin.R;
import com.example.ijoin.SignIn;
import com.example.ijoin.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "CompanyHomeFragment";
    private FragmentHomeBinding binding;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private CompanyPostAdapter adapter;
    private List<Post> posts;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        posts = new ArrayList<>();
        adapter = new CompanyPostAdapter(posts);
        recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.e(TAG, "onCreateView: "  + userId);
            loadCompanyPosts(userId);
        }

        return view;
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
                        Log.e(TAG, "Post: " + post);
                    }
                }
                adapter.setPosts(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error loading posts", databaseError.toException());
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}