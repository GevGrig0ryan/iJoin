package com.example.ijoin.ui.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ijoin.R;
import com.example.ijoin.SearchedActivity;
import com.example.ijoin.UserAdapter;
import com.example.ijoin.Users;
import com.example.ijoin.databinding.FragmentSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment{

    private FragmentSearchBinding binding;
    private DatabaseReference mDatabase;
    private EditText mSearchField;
    private ImageView mSearchBtn;
    private RecyclerView mSearchResults;
    private UserAdapter mAdapter;
    List<Users> users = new ArrayList<>();

    @SuppressLint("WrongViewCast")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mSearchField = view.findViewById(R.id.search_field);
        mSearchBtn = view.findViewById(R.id.search_btn);
        mSearchResults = view.findViewById(R.id.search_results);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearchField.getText().toString().trim();
                firebaseUserSearch(searchText);
            }
        });

        return view;
    }
    private void firebaseUserSearch(String searchText) {
        Query query = mDatabase.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    users.add(user);
                }
                displaySearchResults(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void displaySearchResults(List<Users> users) {
        UserAdapter adapter = new UserAdapter(users);
        mSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchResults.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> {
            // Handle item click here
            Users user = users.get(position);
            // For example, show a toast with the user's name
            Log.e("SearchFragment", "user: " + user.getUserType());
            Intent intent = new Intent(getContext(), SearchedActivity.class);
            intent.putExtra("usertype", user.getUserType());
            String name = user.getName().toString();
            if(user.getUserType() == "Vol"){
                name = name + " " + user.getSurname().toString();
            }
            intent.putExtra("name", name);
            intent.putExtra("email", user.getEmail());
            intent.putExtra("profileImageUrl",user.getprofileImageUrl());
            intent.putExtra("dataAboutUser", user.getDataAboutUser());
            intent.putExtra("userId", user.getUid1());
            Log.e("SearchFragment", "Uid: " + user.getUid1());
            startActivity(intent);
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}