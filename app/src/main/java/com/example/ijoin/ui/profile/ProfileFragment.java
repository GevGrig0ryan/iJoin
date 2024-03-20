package com.example.ijoin.ui.profile;

import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ijoin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    TextView name_surname, interests;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ImageView profilePic, editName, editInterest;
    EditText editNameSurname, editInterests;
    private static final int PICK_IMAGE = 1;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        name_surname = (TextView) view.findViewById(R.id.name_surname);
        interests = (TextView) view.findViewById(R.id.interests);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        profilePic = view.findViewById(R.id.profile_image_view);
        editName = view.findViewById(R.id.edit_nameSurname);
        editInterest = view.findViewById(R.id.edit_interests);
        editNameSurname = view.findViewById(R.id.edittext_namesurname);
        editInterests = view.findViewById(R.id.edittext_interests);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_surname.getVisibility() == View.VISIBLE){
                    name_surname.setVisibility(View.GONE);
                    editNameSurname.setVisibility(View.VISIBLE);
                    editNameSurname.setText(name_surname.getText());
                }
                else{
                    name_surname.setVisibility(View.VISIBLE);
                    editNameSurname.setVisibility(View.GONE);
                    String edited = String.valueOf(editNameSurname.getText());
                    name_surname.setText(editNameSurname.getText());
                    String[] namesurname = edited.split(" ");
                    databaseReference.child("name").setValue(namesurname[0]);
                    databaseReference.child("surname").setValue(namesurname[1]);
                }
            }
        });
        editInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interests.getVisibility() == View.VISIBLE){
                    interests.setVisibility(View.GONE);
                    editInterests.setVisibility(View.VISIBLE);
                    editInterests.setText(interests.getText());
                }
                else{
                    interests.setVisibility(View.VISIBLE);
                    editInterests.setVisibility(View.GONE);
                    interests.setText(editInterests.getText());
                    databaseReference.child("dataAboutUser").setValue(String.valueOf(editInterests.getText()));
                }
            }
        });
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Handle the data snapshot
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String surname = dataSnapshot.child("surname").getValue(String.class);
                    String interest = dataSnapshot.child("dataAboutUser").getValue(String.class);
                    String nameSurname = name + " " + surname;
                    name_surname.setText(nameSurname);
                    interests.setText(interest);
                    String urlString = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    Glide.with(requireContext())
                            .load(urlString)
                            .into(profilePic);
                }
                else{
                    Log.e("Firebase", "Data snapshot does not exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Log.e("Firebase", "Error reading user data", databaseError.toException());
            }
        });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        return view;
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            startCropActivity(imageUri);
        } else if (requestCode == REQUEST_CROP && resultCode == getActivity().RESULT_OK && data != null) {
            handleCropResult(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }
    private void startCropActivity(Uri imageUri) {
        UCrop.of(imageUri, Uri.fromFile(new File(requireContext().getCacheDir(), "cropped_image")))
                .withAspectRatio(1, 1)
                .start(requireContext(), this);
    }
    private void handleCropResult(Intent data) {
        Uri croppedImageUri = UCrop.getOutput(data);
        if (croppedImageUri != null) {
            uploadImageToFirebase(croppedImageUri);
            profilePic.setImageURI(croppedImageUri);
        } else {
            Toast.makeText(getContext(), "Error: Unable to retrieve cropped image", Toast.LENGTH_SHORT).show();
        }
    }
    private void handleCropError(Intent data) {
        Throwable cropError = UCrop.getError(data);
        if (cropError != null) {
            Toast.makeText(getContext(), "Crop failed: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Unknown crop error", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri == null) {
            // Handle the case where imageUri is null
            Toast.makeText(getContext(), "Image URI is null", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String fileName = "profile_image.jpg";
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imageRef = storageRef.child(userId); // Specify file name
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        saveImageUrlToDatabase(downloadUrl);
                    }).addOnFailureListener(e -> {
                        // Handle failure to get download URL
                        Toast.makeText(getContext(), "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle upload failure
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef.child("users").child(userId).child("profileImageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}
