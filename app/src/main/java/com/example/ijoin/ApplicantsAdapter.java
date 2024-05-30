package com.example.ijoin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ViewHolder> {

    private List<Apply> applicants;
    private Context context;

    public ApplicantsAdapter(List<Apply> applicants, Context context) {
        this.applicants = applicants;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.applicant_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Apply applicant = applicants.get(position);
        holder.applicantName.setText(applicant.getUserName());
        Glide.with(context)
                .load(applicant.getUserProfileImageUrl())
                .placeholder(R.drawable.baseline_person_24)
                .into(holder.applicantProfileImage);
        DatabaseReference req = FirebaseDatabase.getInstance().getReference().child("requests")
                .child(currentUser.getUid())
                .child(applicant.getPostName())
                .child(applicant.getUserId());
        req.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.equals(snapshot.child("status").getValue(String.class), "accepted")){
                    holder.acceptButton.setVisibility(View.GONE);
                    holder.declineButton.setVisibility(View.GONE);
                    holder.state.setText("Accepted!");
                    holder.state.setTextColor(R.color.bc_green);
                    holder.state.setVisibility(View.VISIBLE);
                }else if (Objects.equals(snapshot.child("status").getValue(String.class), "declined")){
                    holder.acceptButton.setVisibility(View.GONE);
                    holder.declineButton.setVisibility(View.GONE);
                    holder.state.setText("Declined!");
                    holder.state.setTextColor(R.color.red);
                    holder.state.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.acceptButton.setOnClickListener(v -> handleAccept(applicant, "accepted"));
        holder.declineButton.setOnClickListener(v -> handleDecline(applicant, "declined"));
    }

    private void handleAccept(Apply applicant, String status) {
        // Handle acceptance logic
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sendNotification(applicant.getUserId(), "Congratulations! 🎉", "Dear " + applicant.getUserName() + ",\n\nWe are thrilled to inform you that your application for the " +
                        applicant.getPostName() + " position has been accepted! 🌟\n\nWelcome to our team of dedicated volunteers. We are excited to have you on board and look forward to making a " +
                        "positive impact together.\n\nPlease check your email for further details and next steps.\n\nBest regards,\n" + snapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("users").child(applicant.getUserId()).child(currentUser.getUid());
        DatabaseReference reqRef = db1.child(applicant.getPostName());


        DatabaseReference db = FirebaseDatabase.getInstance().getReference("requests")
                .child(currentUser.getUid())
                .child(applicant.getPostName())
                .child(applicant.getUserId());
        db.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    db.child("status").setValue("accepted");
                    reqRef.child("status").setValue("accepted");
                    Toast.makeText(context, "Application " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        Toast.makeText(context, "Accepted " + applicant.getUserName(), Toast.LENGTH_SHORT).show();
    }

    private void handleDecline(Apply applicant, String status) {
        // Handle decline logic
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("users").child(applicant.getUserId()).child(currentUser.getUid());
        DatabaseReference reqRef = db1.child(applicant.getPostName());
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("requests")
                .child(currentUser.getUid())
                .child(applicant.getPostName())
                .child(applicant.getUserId());
        db.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    db.child("status").setValue("declined");
                    reqRef.child("status").setValue("declined");
                    Toast.makeText(context, "Application " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        Toast.makeText(context, "Declined " + applicant.getUserName(), Toast.LENGTH_SHORT).show();
        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sendNotification(applicant.getUserId(), "Application Declined", "Dear " + applicant.getUserName() + ",\n\nWe regret to inform you that your application for the " + applicant.getPostName() + " position has been declined. We appreciate your interest and encourage you to apply for future opportunities.\n\nBest regards,\n" + snapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void sendNotification(String userId, String title, String message) {
            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("notifications")
                    .child(userId)
                    .push();
            Notification notification = new Notification(title, message);
            notificationRef.setValue(notification)
                    .addOnSuccessListener(aVoid -> {
                        // Notification sent successfully
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to send notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

    }
    @Override
    public int getItemCount() {
        return applicants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView applicantProfileImage;
        TextView applicantName, state;
        ImageView acceptButton, declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantProfileImage = itemView.findViewById(R.id.applicant_profile_picture);
            applicantName = itemView.findViewById(R.id.applicant_name);
            acceptButton = itemView.findViewById(R.id.accept_button);
            declineButton = itemView.findViewById(R.id.decline_button);
            state = itemView.findViewById(R.id.state);
        }
    }
}
