package com.bramgussekloo.projectb.Activities.EditProduct;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.bramgussekloo.projectb.Activities.EditProduct.EditProduct;
import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseProduct extends AppCompatActivity {
    private Button button;
    private Spinner Spinner;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_product);
        Spinner = findViewById(R.id.spinnerProductName);
        setSpinner();
        chooseProduct();

    }

    private void chooseProduct(){
        Spinner = findViewById(R.id.spinnerProductName);
        button = findViewById(R.id.ChooseProductButton);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }
                    Log.d("QuerySnapshot", list.toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getBaseContext(),
                            android.R.layout.simple_spinner_item,
                            list
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner.setAdapter(adapter);
                } else {
                    Log.d("QuerySnapshot", "Error getting documents: ", task.getException());
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditProduct.class);
                intent.putExtra("productID",Spinner.getSelectedItem().toString());
                startActivity(intent);
            }
        });
    }
    private void setSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.placeholder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(adapter);
    }
}
