package com.bramgussekloo.projectb.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Login.MainActivity;
import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import io.grpc.Context;

public class NewProductActivity extends AppCompatActivity{
    private Spinner spinner;
    private ImageView newProductImage;
    private EditText newProductId;
    private EditText newProductDesc;
    private EditText newProductTitle;
    private EditText newProductQuantity;
    private Button newProductBttn;

    private Uri productImageUri = null;

    private ProgressBar newProductProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        newProductImage = findViewById(R.id.new_product_image);
        newProductId = findViewById(R.id.new_ProductID);
        newProductTitle = findViewById(R.id.new_product_title);
        newProductDesc = findViewById(R.id.new_product_desc);
        newProductQuantity = findViewById(R.id.new_product_quantity);
        newProductBttn = findViewById(R.id.post_bttn);
        newProductProgress = findViewById(R.id.new_product_progress);
        spinner = findViewById(R.id.spinner);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        customizeActionBar();

        setSpinner();


        //
        newProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // button to choose and crop picture

               cropImage();

            }
        });

        newProductBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // will append data to firebase

                final String id = newProductId.getText().toString();
                final String title = newProductTitle.getText().toString();
                final String description = newProductDesc.getText().toString();
                final String quantityCheck = newProductQuantity.getText().toString();
                final String category = spinner.getSelectedItem().toString();

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(quantityCheck) && productImageUri != null) { // checks if fields aren't aempty

                    final Integer quantity = Integer.parseInt(newProductQuantity.getText().toString());

                    newProductProgress.setVisibility(View.VISIBLE);

                    StorageReference filePath = storageReference.child("product_images").child(title + ".jpg");

                    filePath.putFile(productImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            final String downloadUri = urlTask.getResult().toString();

                            if(task.isSuccessful()){

                                File newImageFile = new File(productImageUri.getPath());

                                try {
                                    compressedImageFile = new Compressor(NewProductActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(newImageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();



                                UploadTask uploadTask = storageReference.child("product_images/thumbs").child(title + ".jpg").putBytes(data);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        Task<Uri> thumbUri = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                        final String thumbDownloadUri = thumbUri.toString();


                                        Map<String, Object> productMap = new HashMap<>();
                                        productMap.put("image_url", downloadUri);
                                        productMap.put("thumb_url", thumbDownloadUri);
                                        productMap.put("title", title);
                                        productMap.put("desc", description);
                                        productMap.put("quantity", quantity);
                                        productMap.put("category", category);


                                        firebaseFirestore.collection("Products").document(id).set(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    Toast.makeText(getApplicationContext(), "Product is added", Toast.LENGTH_LONG).show();

                                                    sendToMain();

                                                } else {

                                                    String errorMessage = task.getException().getMessage();

                                                    Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                                }

                                                newProductProgress.setVisibility(View.INVISIBLE);

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        String errorMessage = e.getMessage();

                                        Toast.makeText(NewProductActivity.this, "Error : " + errorMessage, Toast.LENGTH_SHORT).show();

                                    }
                                });






                            } else {

                                newProductProgress.setVisibility(View.INVISIBLE);

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                } else {

                    newProductProgress.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Error : Please fill in all fields", Toast.LENGTH_LONG).show();


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

    private void cropImage(){

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

    private void sendToMain(){
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
        finish();
    }

    private void customizeActionBar(){
        getSupportActionBar().setTitle("Add New Product"); // sets title for toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


}