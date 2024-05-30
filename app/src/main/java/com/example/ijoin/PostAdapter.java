package com.example.ijoin;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ijoin.Post;
import com.example.ijoin.R;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private List<Post> posts;
    private Context context;
    DatabaseReference databaseReference;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (posts != null) {

            Post post = posts.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date currentDate = new Date();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("requests");
            DatabaseReference db = reqRef.child(post.getComapnyUid()).child(post.getPostName());
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        holder.applyButton.setVisibility(View.GONE);
                        holder.applied.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Log.d("PostAdapter", "Post Name: " + post.getPostName());
            Log.d("PostAdapter", "Description: " + post.getDescription());
            Log.d("PostAdapter", "Volunteering Types: " + post.getVolTypes().toString());
            //Log.d("PostAdapter", "Company name: " + post.getCompanyName().toString());
            // Load profile picture using Glide

            Glide.with(context)
                    .load(post.getProfilePictureUrl()) // Assuming you have a method to get the profile picture URL
                    .placeholder(R.drawable.baseline_person_24) // Placeholder image
                    .into(holder.companyProfilePicture);
            holder.postName.setText(post.getPostName());
            holder.description.setText(post.getDescription());
            holder.companyname.setText(post.getCompanyName());
            holder.compid.setText(post.getComapnyUid());
            holder.applyButton.setOnClickListener(v -> applyForVolunteer(post, position, holder.applyButton, holder.applied));
        }
    }
    private void applyForVolunteer(Post post, int position, AppCompatButton applyButton, AppCompatButton applied) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.R)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Handle the data snapshot
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String surname = dataSnapshot.child("surname").getValue(String.class);
                        String nameSurname = name + " " + surname;
                        String urlString = dataSnapshot.child("profileImageUrl").getValue(String.class);
                        String companyuid = post.getComapnyUid().toString();
                        Apply apply= new Apply();
                        apply.setUserId(userId);
                        apply.setUserName(nameSurname);
                        apply.setUserProfileImageUrl(urlString);
                        apply.setStatus("pending");
                        apply.setPostName(post.getPostName());
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("requests");
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        db.child(companyuid).child(post.getPostName().toString()).child(currentUser.getUid()).setValue(apply)
                                .addOnSuccessListener(aVoid -> {
                                    applyButton.setVisibility(View.GONE);
                                    applied.setVisibility(View.VISIBLE);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                        db1.child("requests").child(companyuid).child(post.getPostName()).child(currentUser.getUid()).setValue(apply);
                    } else {
                        Log.e("Firebase", "Data snapshot does not exist");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                    Log.e("Firebase", "Error reading user data", databaseError.toException());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return posts == null ? 0 : posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView companyProfilePicture;
        TextView postName;
        TextView description, companyname, compid;
        AppCompatButton applyButton, applied, passed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyProfilePicture = itemView.findViewById(R.id.company_profile_picture);
            postName = itemView.findViewById(R.id.post_name);
            description = itemView.findViewById(R.id.post_description);
            companyname = itemView.findViewById(R.id.companyName);
            compid = itemView.findViewById(R.id.compId);
            applyButton = itemView.findViewById(R.id.apply);
            applied = itemView.findViewById(R.id.applied);
            passed = itemView.findViewById(R.id.passedDate);
        }
    }
}
