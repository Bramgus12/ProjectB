package com.bramgussekloo.projectb.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

public class NewProductActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private ImageView newProductImage;
    private EditText newProductDesc;
    private EditText newProductTitle;
    private Button newProductBttn;

    private Uri productImageUri = null;

    private ProgressBar newProductProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        getSupportActionBar().setTitle("Add New Product"); // sets title for toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newProductImage = findViewById(R.id.new_product_image);
        newProductTitle = findViewById(R.id.new_product_title);
        newProductDesc = findViewById(R.id.new_product_desc);
        newProductBttn = findViewById(R.id.post_bttn);
        newProductProgress = findViewById(R.id.new_product_progress);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //
        newProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // button to choose and crop picture

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ // checks if phone has right build
                    if(ContextCompat.checkSelfPermission(NewProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ // checks if used phone has right permissions
                        Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(NewProductActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1); // requests read permission
                    } else {

                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON) // sets guidelines for cropping images
                                .setMinCropResultSize(512, 512) // sets minimal image size
                                .setAspectRatio(255, 150) // sets aspect ratio
                                .start(NewProductActivity.this); // starts the crop image acivity

                    }
                }


            }
        });

        newProductBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // will append data to firebase


                final String title = newProductTitle.getText().toString();
                final String description = newProductDesc.getText().toString();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && productImageUri != null) { // checks if fields aren't aempty

                    newProductProgress.setVisibility(View.VISIBLE);

                    StorageReference filePath = storageReference.child("product_images").child(title + ".jpg");
                    filePath.putFile(productImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if(task.isSuccessful()){

                                Task<Uri> result = task.getResult().getMetadata().getReference().getDownloadUrl();
                                String downloaduri = result.toString();

                                Map<String, Object> productMap = new HashMap<>();
                                productMap.put("image_url", downloaduri);
                                productMap.put("title", title);
                                productMap.put("desc", description);


                                firebaseFirestore.collection("Products").add(productMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if(task.isSuccessful()){

                                            Toast.makeText(getApplicationContext(), "Product is added", Toast.LENGTH_LONG).show();
                                            Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(backIntent);
                                            finish();

                                        } else {

                                            String errorMessage = task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                        }

                                        newProductProgress.setVisibility(View.INVISIBLE);

                                    }
                                });



                            } else {

                                newProductProgress.setVisibility(View.INVISIBLE);


                            }
                        }
                    });


                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // checks result of given picture
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                productImageUri = result.getUri(); // appends the uri to given variable
                newProductImage.setImageURI(productImageUri); // appends the uri to newproductimage

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError(); // error if mistakes in image
            }
        }
    }


}