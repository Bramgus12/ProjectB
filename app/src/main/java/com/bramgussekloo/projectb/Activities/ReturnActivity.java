package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Login.MainActivity;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

public class ReturnActivity extends AppCompatActivity {
    // Initialization of the attributes
    private static final String TAG = "ReturnActivity";
    private TextView name;
    private TextView date;
    private TextView lendDate;
    private TextView product;
    private String returnDate;
    private Intent intent;
    private Button returnButton;
    private Lend lend;
    private String lendDateString;
    private long millisecond;
    private int Quantity;
    private int oldQuantity;
    private DatabaseReference mRootRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // assignment of all the attributes
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        name = findViewById(R.id.ReturnNameFilled);
        date = findViewById(R.id.ReturnDateFilled);
        lendDate = findViewById(R.id.ReturnLendDateFilled);
        product = findViewById(R.id.ReturnProductFilled);
        intent = getIntent();
        returnButton = findViewById(R.id.ReturnButton);
        lend = intent.getParcelableExtra("item");
        returnDate = lend.getDay() + "/" + lend.getMonth() + "/" + lend.getYear();
        millisecond = lend.getTimeOfLend().getTime();
        lendDateString = DateFormat.format("dd/MM/yy", new Date(millisecond)).toString();
        Quantity = lend.getQuantity();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        setLend(); // getting the values from the database and setting them in the textviews.
        Button(); // deleting the database entry.
    }
    private void setLend(){
        product.setText(lend.getProduct());
        mRootRef.child("users").child(lend.NameId).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        date.setText(returnDate);
        lendDate.setText(lendDateString);
    }

    private void Button(){
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Products/" + lend.getProduct() + "/LendTo").document(lend.NameId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseFirestore.getInstance().collection("Products").document(lend.getProduct()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> ProductMap = task.getResult().getData();
                                        oldQuantity = Integer.parseInt(ProductMap.get("quantity").toString());
                                        oldQuantity = oldQuantity + Quantity;
                                        ProductMap.put("quantity", oldQuantity);
                                        FirebaseFirestore.getInstance().collection("Products").document(lend.getProduct()).set(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(ReturnActivity.this, "Product is returned.", Toast.LENGTH_SHORT).show();
                                                    goToMain();
                                                } else {
                                                    Log.e(TAG, "onComplete: ", task.getException());
                                                    Toast.makeText(ReturnActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Log.e(TAG, "onComplete: ", task.getException());
                                        Toast.makeText(ReturnActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Log.e(TAG, "onComplete: ", task.getException());
                        }
                    }
                });
            }
        });
    }
    private void goToMain(){ //going back to the mainactivity
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
