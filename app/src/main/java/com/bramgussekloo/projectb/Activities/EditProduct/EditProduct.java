package com.bramgussekloo.projectb.Activities.EditProduct;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Login.MainActivity;
import com.bramgussekloo.projectb.Activities.NewProductActivity;
import com.bramgussekloo.projectb.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import id.zelory.compressor.Compressor;


public class EditProduct extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private EditText quantity;
    private EditText description;
    private EditText title;
    private ImageView image;
    private String ProductID;
    private Spinner Category;
    private Button EditProductButton;
    private Button DeleteProductBtton;
    private ProgressBar progressbar;
    private Uri productImageUri = null;
    private StorageReference storageReference;
    private Bitmap compressedImageFile;
    private String image_urlValue;
    private String downloadUri;
    private String thumbDownloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        ProductID = getIntent().getExtras().getString("productID");
        ItemsFromDatabase();
        Buttons();
        customizeActionBar();
    }

    private void ItemsFromDatabase() {
        setSpinner();
        title = findViewById(R.id.edit_product_title);
        description = findViewById(R.id.edit_product_desc);
        quantity = findViewById(R.id.edit_product_quantity);
        image = findViewById(R.id.edit_product_image);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Products").document(ProductID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("snapshotListener", "listen failed", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d("snapshotListener", "onEvent: " + documentSnapshot.getData());
                    Map<String, Object> object_data = documentSnapshot.getData();
                    String quantityValue = object_data.get("quantity").toString();
                    String categoryValue = object_data.get("category").toString();
                    String descriptionValue = object_data.get("desc").toString();
                    image_urlValue = object_data.get("image_url").toString();
                    String titleValue = object_data.get("title").toString();
                    title.setText(titleValue);
                    ArrayAdapter myAdap = (ArrayAdapter) Category.getAdapter();
                    int spinnerPosition = myAdap.getPosition(categoryValue);
                    Category.setSelection(spinnerPosition);
                    description.setText(descriptionValue);
                    quantity.setText(quantityValue);
                    Glide.with(getBaseContext()).load(image_urlValue).into(image);
                } else {
                    Log.d("snapshotListener", "onEvent: Null");
                }
            }
        });
    }

    private void Buttons() {
        quantity = findViewById(R.id.edit_product_quantity);
        title = findViewById(R.id.edit_product_title);
        description = findViewById(R.id.edit_product_desc);
        Category = findViewById(R.id.spinnerCategoryProduct);
        EditProductButton = findViewById(R.id.EditProductButton);
        DeleteProductBtton = findViewById(R.id.DeleteProductButton);
        image = findViewById(R.id.edit_product_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressbar = findViewById(R.id.edit_product_progress);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });
        DeleteProductBtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                builder.setTitle("Are you sure ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteProduct();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog ad =builder.create();
                ad.show();
            }
        });
        EditProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Products").document(ProductID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString()) && !TextUtils.isEmpty(quantity.getText().toString())) { // checks if fields aren't aempty
                            final Integer quantityValue = Integer.parseInt(quantity.getText().toString());
                            if (documentSnapshot != null && productImageUri == null){
                                Map<String, Object> snapshot = documentSnapshot.getData();
                                thumbDownloadUri = snapshot.get("thumb_url").toString();
                                downloadUri = snapshot.get("image_url").toString();
                                Map<String, Object> productMap = new HashMap<>();
                                productMap.put("image_url", downloadUri);
                                productMap.put("thumb_url", thumbDownloadUri);
                                productMap.put("title", title.getText().toString());
                                productMap.put("desc", description.getText().toString());
                                productMap.put("quantity", quantityValue);
                                productMap.put("category", Category.getSelectedItem());
                                firebaseFirestore.collection("Products").document(title.getText().toString()).set(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Product is Edited", Toast.LENGTH_LONG).show();
                                            sendToMain();
                                        } else {
                                            String errorMessage = task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                        }
                                        progressbar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                            else if (productImageUri != null) {
                                progressbar.setVisibility(View.VISIBLE);
                                StorageReference filePath = storageReference.child("product_images").child(title.getText().toString() + ".jpg");
                                filePath.putFile(productImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
                                        while (!urlTask.isSuccessful()) ;
                                        downloadUri = urlTask.getResult().toString();
                                        if (task.isSuccessful()) {
                                            File newImageFile = new File(productImageUri.getPath());
                                            try {
                                                compressedImageFile = new Compressor(EditProduct.this)
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
                                            UploadTask uploadTask = storageReference.child("product_images/thumbs").child(title.getText().toString() + ".jpg").putBytes(data);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Task<Uri> urlTaskThumb = taskSnapshot.getStorage().getDownloadUrl();
                                                    while (!urlTaskThumb.isSuccessful()) ;
                                                    thumbDownloadUri = urlTaskThumb.getResult().toString();
                                                    Map<String, Object> productMap = new HashMap<>();
                                                    productMap.put("image_url", downloadUri);
                                                    productMap.put("thumb_url", thumbDownloadUri);
                                                    productMap.put("title", title.getText().toString());
                                                    productMap.put("desc", description.getText().toString());
                                                    productMap.put("quantity", quantityValue);
                                                    productMap.put("category", Category.getSelectedItem());
                                                    firebaseFirestore.collection("Products").document(title.getText().toString()).set(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Product is Edited", Toast.LENGTH_LONG).show();
                                                                emptyInputEditText();
                                                                sendToMain();
                                                            } else {
                                                                String errorMessage = task.getException().getMessage();
                                                                Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                                            }
                                                            progressbar.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    String errorMessage = e.getMessage();
                                                    Toast.makeText(EditProduct.this, "Error : " + errorMessage, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            progressbar.setVisibility(View.INVISIBLE);
                                            String errorMessage = task.getException().getMessage();
                                            Toast.makeText(getApplicationContext(), "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Log.d("Snapshot: ", "Something went wrong");
                            }
                        } else {
                            progressbar.setVisibility(View.INVISIBLE);
                            Log.d("Onclick", "Fields are empty");
                        }
                    }
                });
            }

        });

    }

    private void deleteProduct() {
        firebaseFirestore.collection("Products").document(ProductID).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Product Deleted",Toast.LENGTH_LONG).show();
                            finish();
                            sendToMain();


                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // checks result of given picture
        image = findViewById(R.id.edit_product_image);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                productImageUri = result.getUri(); // appends the uri to given variable
                image.setImageURI(productImageUri); // appends the uri to newproductimage

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError(); // error if mistakes in image
                Log.d("Imageloader", "Error: " + error);
            }
        }
    }

    private void setSpinner(){
        Category = findViewById(R.id.spinnerCategoryProduct);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Category.setAdapter(adapter);
    }
    private void cropImage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ // checks if phone has right build
            if(ContextCompat.checkSelfPermission(EditProduct.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){ // checks if used phone has right permissions
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(EditProduct.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1); // requests read permission
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON) // sets guidelines for cropping images
                        .setMinCropResultSize(512, 512) // sets minimal image size
                        .setAspectRatio(255, 150) // sets aspect ratio
                        .start(EditProduct.this); // starts the crop image acivity
            }
        }
    }
    private void emptyInputEditText() { // empty input fields
        quantity.setText(null);
        description.setText(null);
        title.setText(null);
    }
    private void customizeActionBar(){
        getSupportActionBar().setTitle("Edit a product"); // sets title for toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void sendToMain(){
        Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(backIntent);
        finish();
    }
}
