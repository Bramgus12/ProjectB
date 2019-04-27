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

import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.Single;

public class EditProduct extends AppCompatActivity {
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
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
        ProductID = getIntent().getExtras().getString("productID");
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
                    Log.w("snapshotListener", "listen failed" , e);
                    return;
                } if (documentSnapshot != null && documentSnapshot.exists()){
                    Log.d("snapshotListener", "onEvent: " + documentSnapshot.getData());
                    Map<String, Object> object_data = documentSnapshot.getData();
                    String quantityValue = object_data.get("quantity").toString();
                    String categoryValue = object_data.get("category").toString();
                    String descriptionValue = object_data.get("desc").toString();
                    String image_urlValue = object_data.get("image_url").toString();
                    String titleValue = object_data.get("title").toString();
                    title.setText(titleValue);
                    description.setText(descriptionValue);
                    quantity.setText(quantityValue);
                    Glide.with(getBaseContext()).load(image_urlValue).into(image);

                } else {
                    Log.d("snapshotListener", "onEvent: Null");
                }
            }
        });
    }
}
