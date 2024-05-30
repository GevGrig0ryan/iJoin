package com.example.ijoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApplicantsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ApplicantsAdapter adapter;
    List<Apply> applicants;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);
        recyclerView = findViewById(R.id.applicantsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        applicants = new ArrayList<>();
        adapter = new ApplicantsAdapter(applicants, this);
        recyclerView.setAdapter(adapter);

        String postId = getIntent().getStringExtra("postId");
        String companyUid = getIntent().getStringExtra("companyUid");

        loadApplicants(companyUid, postId);
    }

    private void loadApplicants(String companyUid, String postId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("requests").child(companyUid).child(postId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                applicants.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("ApplicantsActivity", "Snapshot: " + snapshot.toString()); // Log the snapshot to see what data is being retrieved
                    try {
                        Apply applicant = snapshot.getValue(Apply.class);
                        if (applicant != null) {
                            applicants.add(applicant);
                        } else {
                            Log.e("ApplicantsActivity", "Invalid data: " + snapshot.toString());
                        }
                    } catch (Exception e) {
                        Log.e("ApplicantsActivity", "Error parsing data: ", e);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ApplicantsActivity.this, "Failed to load applicants.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
