package com.bramgussekloo.projectb.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.SingleProduct;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

import io.reactivex.Single;

public class EditProduct extends AppCompatActivity {
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
//    private Spinner spinnerProduct = findViewById(R.id.spinnerProduct);
//    private Spinner spinnerCategory = findViewById(R.id.spinnerCategoryProduct);
//    private Button editProdctButton = findViewById(R.id.EditProductButton);
    private EditText quantity;
    private EditText description;
    private EditText title;
    private ImageView image;
    private String ProductID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        ProductID = "test";
        ItemsFromDatabase();
    }

    private void ItemsFromDatabase(){
        storageReference = FirebaseStorage.getInstance().getReference();
        title = findViewById(R.id.edit_product_title);
        description= findViewById(R.id.edit_product_desc);
        quantity = findViewById(R.id.edit_product_quantity);
        image = findViewById(R.id.edit_product_image);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        firebaseFirestore.collection("Products").document(ProductID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.w("snapshotlistener", "listen failed" , e);
                    return;
                } if (documentSnapshot != null && documentSnapshot.exists()){
                    Log.d("snapshotlistener", "onEvent: " + documentSnapshot.getData());
                    SingleProduct singleProduct = documentSnapshot.toObject(SingleProduct.class);
                    String quantityValue = singleProduct.getQuantity();
                    title.setText(singleProduct.getTitle());
                    description.setText(singleProduct.getDescription());
                    quantity.setText(quantityValue);
                    Glide.with(getBaseContext()).load(singleProduct.getImage()).into(image);
                    Log.d("Title", singleProduct.getTitle());
                } else {
                    Log.d("snapshotlistener", "onEvent: Null");
                }
            }
        });
    }
}
