package com.example.ijoin.ui.profilevol;

import static com.yalantis.ucrop.UCrop.REQUEST_CROP;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ijoin.CompanyPostAdapter;
import com.example.ijoin.CompanyProfilePostAdapter;
import com.example.ijoin.Post;
import com.example.ijoin.PostStep1;
import com.example.ijoin.R;
import com.example.ijoin.SignIn;
import com.example.ijoin.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    TextView name_surname, interests;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ImageView profilePic, editName, editInterest;
    EditText editNameSurname, editInterests;
    private static final int PICK_IMAGE = 2;
    private static final int REQUEST_CODE_PICK_FILE = 1;

    Button sign_out, addPost;
    private LinearLayout fileContainerLayout;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    LinearLayout cert;
    private RecyclerView recyclerView;
    private CompanyProfilePostAdapter adapter;
    private List<Post> posts;
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
        View view = inflater.inflate(R.layout.fragment_profilevol, container, false);
        name_surname = (TextView) view.findViewById(R.id.name_surname);
        interests = (TextView) view.findViewById(R.id.interests);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        profilePic = view.findViewById(R.id.profile_image_view);
        editName = view.findViewById(R.id.edit_nameSurname);
        editInterest = view.findViewById(R.id.edit_interests);
        editNameSurname = view.findViewById(R.id.edittext_namesurname);
        editInterests = view.findViewById(R.id.edittext_interests);
        fileContainerLayout = view.findViewById(R.id.file_container_layout);
        ImageView uploadButton = view.findViewById(R.id.add_certificate);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        cert = view.findViewById(R.id.cert);
        recyclerView = view.findViewById(R.id.post);
        sign_out = view.findViewById(R.id.sign_out);
        addPost = view.findViewById(R.id.addPost);
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostStep1.class);
                startActivity(intent);
            }
        });
        sign_out.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), SignIn.class);
            startActivity(intent);
        });
        uploadButton.setOnClickListener(v -> onUploadButtonClick());
        retrieveFiles();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_surname.getVisibility() == View.VISIBLE) {
                    name_surname.setVisibility(View.GONE);
                    editNameSurname.setVisibility(View.VISIBLE);
                    editNameSurname.setText(name_surname.getText());
                } else {
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
                if (interests.getVisibility() == View.VISIBLE) {
                    interests.setVisibility(View.GONE);
                    editInterests.setVisibility(View.VISIBLE);
                    editInterests.setText(interests.getText());
                } else {
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
                    String surname = "";
                    if(Objects.equals(dataSnapshot.child("userType").getValue(String.class), "Vol")){
                        surname = dataSnapshot.child("surname").getValue(String.class);
                    }
                    else {
                        posts = new ArrayList<>();
                        cert.setVisibility(View.GONE);
                        addPost.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter = new CompanyProfilePostAdapter(posts);
                        recyclerView.setAdapter(adapter);
                        String userId = currentUser.getUid();
                        loadCompanyPosts(userId);
                    }
                    String interest = dataSnapshot.child("dataAboutUser").getValue(String.class);
                    String nameSurname = name + " " + surname;
                    name_surname.setText(nameSurname);
                    interests.setText(interest);
                    String urlString = dataSnapshot.child("profileImageUrl").getValue(String.class);
                    if(urlString != null) {
                        Glide.with(requireContext())
                                .load(urlString)
                                .into(profilePic);
                    }
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
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            startCropActivity(imageUri);
        } else if (requestCode == REQUEST_CROP && resultCode == Activity.RESULT_OK && data != null) {
            handleCropResult(data);
        } else if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            String mimeType = getActivity().getContentResolver().getType(fileUri);
            if (mimeType != null && mimeType.startsWith("image/")) {
                startCropActivity(fileUri);  // Only crop if the selected file is an image
            } else {
                uploadFile(fileUri);
            }
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
        if (getContext() == null) {
            // Handle the case where the Context is null
            Log.e("ProfileFragment", "Context is null");
            return;
        }
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef.child("users").child(userId).child("profileImageUrl").setValue(imageUrl)
                .addOnSuccessListener(aVoid -> {
                    if (getContext() != null) {
                        databaseRef.child("users").child(userId).child("profileImageUrl").setValue(imageUrl);
                        Toast.makeText(getContext(), "Profile image uploaded successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("ProfileFragment", "Context is null when showing toast");
                    }
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Failed to save profile image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("ProfileFragment", "Context is null when showing toast");
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private void retrieveFiles() {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userFilesRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("files");
        userFilesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    Upload upload = fileSnapshot.getValue(Upload.class);
                    if (upload != null) {
                        createFileButton(upload.getName(), upload.getUrl());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving files", error.toException());
            }
        });
    }

    private void createFileButton(String fileName, String fileUrl) {
        Button fileButton = new Button(getContext());
        fileButton.setText(fileName);

        fileButton.setOnClickListener(v -> openFile(fileUrl) );
        fileContainerLayout.addView(fileButton); // Add button to the layout
    }
        private void openFile(String fileUrl) {
            String fileName = Uri.parse(fileUrl).getLastPathSegment();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            try {
                File localFile = File.createTempFile("tempFile", getFileExtension(fileName));
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUrl);

                storageRef.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            Uri fileUri = FileProvider.getUriForFile(requireContext(),
                                    requireContext().getPackageName() + ".provider", localFile);
                            openDownloadedFile("user_files/" + userId, fileName);
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Error downloading file", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error creating temp file", Toast.LENGTH_SHORT).show();
            }
        }

    private void openDownloadedFile(String directory, String fileName) {
        File file = new File(directory, fileName);
        Uri fileUri = FileProvider.getUriForFile(requireContext(),
                requireContext().getPackageName() + ".provider",
                file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        String mimeType = getMimeType(file);
        if (mimeType == null || mimeType.equals("*/*")) {
            mimeType = "application/*";
        }
        intent.setDataAndType(fileUri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Exception e) {
            showFallbackMessage();
        }
    }
    private String getMimeType(File file) {
        Uri fileUri = Uri.fromFile(file);
        String mimeType = null;
        ContentResolver contentResolver = getContext().getContentResolver();

        if (fileUri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            mimeType = contentResolver.getType(fileUri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }

        return mimeType;
    }
    private void onUploadButtonClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types to be selected
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }
    private String getFileExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex != -1) {
            return fileName.substring(lastIndex + 1);
        } else {
            return ""; // Handle the case where the file does not have an extension
        }
    }
    private void showFallbackMessage() {
        Toast.makeText(getContext(), "No app found to open this file", Toast.LENGTH_SHORT).show();
    }
    private void uploadFile(Uri fileUri) {
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("user_files/" + userId + "/" + fileUri.getLastPathSegment());
        storageRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String fileUrl = uri.toString();
                            String fileName = fileUri.getLastPathSegment();
                            DatabaseReference userFilesRef = FirebaseDatabase.getInstance().getReference()
                                    .child("users").child(userId).child("files").push();
                            userFilesRef.setValue(new Upload(fileName, fileUrl))
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getContext(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
                                        // After successful upload, create a button to open the file
                                        createFileButton(fileName, fileUrl);
                                    })
                                    .addOnFailureListener(e -> Log.e("Firebase", "Error saving file info", e));
                        }))
                .addOnFailureListener(e -> Log.e("Firebase", "Error uploading file", e));
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
                        Log.e("ProfileFragment", "Post: " + post);
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
}
