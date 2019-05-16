package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LendActivity extends AppCompatActivity {

    private Product product;
    private String Title;
    private EditText EmailEditText;
    private String Email;
    private Button LendButton;
    private DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "LendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmailEditText = findViewById(R.id.EditTextEmailuser);
        LendButton = findViewById(R.id.LendActivityButton);
        setContentView(R.layout.activity_lend);
        Intent intent = getIntent();
        product = intent.getParcelableExtra("Product");
        Log.d(TAG, intent.getParcelableExtra("Product").toString());
        Title = product.getTitle();
        Buttons();
    }
    private void GetUserID(){

    }

    private void Buttons(){
        LendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailEditText == null){
                    Toast.makeText(LendActivity.this, "Please enter a email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(EmailEditText.getText().toString().trim()).matches()){
                    Toast.makeText(LendActivity.this, "Please enter a valid Email Address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                mRootref.child("users").child(EmailEditText.getText().toString()).child("uid").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Email = EmailEditText.getText().toString();
                        String UserID = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "onDataChange: " + UserID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
