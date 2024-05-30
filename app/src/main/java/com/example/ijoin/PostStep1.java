package com.example.ijoin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Calendar;

public class PostStep1 extends AppCompatActivity {
    //ImageView postImage;
    EditText description, postName;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    Button btnNext;
    /*private static final int PICK_IMAGE = 1;
    private static final int REQUEST_CROP = 2; // Add this line
    private Uri imageUri;*/

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_step1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //postImage = findViewById(R.id.post_image_view);
        description = findViewById(R.id.description);
        btnNext = findViewById(R.id.next);
        postName = findViewById(R.id.postName);
        calendar = Calendar.getInstance();
        //postImage.setOnClickListener(v -> pickImageFromGallery());

        btnNext.setOnClickListener(v -> {
            String name = postName.getText().toString();
            String descript = description.getText().toString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(descript) /*imageUri == null*/) {
                Toast.makeText(PostStep1.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(PostStep1.this, PostStep2.class);
            intent.putExtra("postName", name);
            intent.putExtra("description", descript);
            //intent.putExtra("imageUri", imageUri.toString());
            startActivity(intent);
        });

    }

    /*private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            startCropActivity(imageUri);
        } else if (requestCode == REQUEST_CROP && resultCode == RESULT_OK && data != null) {
            handleCropResult(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void startCropActivity(Uri imageUri) {
        UCrop.of(imageUri, Uri.fromFile(new File(getCacheDir(), "cropped_image")))
                .withAspectRatio(1, 1)
                .start(this);
    }

    private void handleCropResult(Intent data) {
        Uri croppedImageUri = UCrop.getOutput(data);
        if (croppedImageUri != null) {
            Toast.makeText(this, "Cropped image URI: " + croppedImageUri, Toast.LENGTH_SHORT).show(); // Check if croppedImageUri is not null
            this.imageUri = croppedImageUri; // Use class variable instead of local variable
            postImage.setImageURI(croppedImageUri);
        } else {
            Toast.makeText(this, "Error: Unable to retrieve cropped image", Toast.LENGTH_SHORT).show();
        }
    }


    private void handleCropError(Intent data) {
        Throwable cropError = UCrop.getError(data);
        if (cropError != null) {
            Toast.makeText(this, "Crop failed: " + cropError.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unknown crop error", Toast.LENGTH_SHORT).show();
        }
    }*/
}
