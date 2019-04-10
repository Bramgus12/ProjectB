package com.bramgussekloo.projectb.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bramgussekloo.projectb.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProduct extends AppCompatActivity {
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private Spinner spinnerProduct = findViewById(R.id.spinnerProduct);
    private Spinner spinnerCategory = findViewById(R.id.spinnerCategoryProduct);
    private Button editProdctButton = findViewById(R.id.EditProductButton);
    private EditText quantity = findViewById(R.id.edit_product_quantity);
    private EditText description= findViewById(R.id.edit_product_desc);
    private EditText title = findViewById(R.id.edit_product_title);
    private ImageView image = findViewById(R.id.edit_product_image);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        ItemsFromDatabase();
    }

    private void ItemsFromDatabase(){
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);

    }
}
