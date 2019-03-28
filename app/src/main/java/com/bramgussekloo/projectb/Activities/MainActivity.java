package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bramgussekloo.projectb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent LoginActivity = new Intent(getApplicationContext(), com.bramgussekloo.projectb.Activities.LoginActivity.class);
            startActivity(LoginActivity);
            finish();
        }
        if(currentUser != null){
            getUser();
        }


    }

    private void getUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // get the database
        final DatabaseReference mRootRef = database.getReference(); // ref to the database
        FirebaseUser CurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // get info about the user that is trying to log in
        final String Uid = CurrentFirebaseUser.getUid(); // get the UID of the user
        mRootRef.child("users").child(Uid).child("Role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String role = dataSnapshot.getValue(String.class); // get the value of the role
                if (role != null && role.equals("Admin")){
                    Intent intent = new Intent(getBaseContext(), Admin.class);
                    startActivity(intent); // check if the user is an admin
                } else if (role != null && role.equals("Beheerder")){
                    Intent intent = new Intent(getBaseContext(), Beheerder.class);
                    startActivity(intent); // check if the user is an beheerder
                } else {
                    Intent intent = new Intent(getBaseContext(), User.class);
                    startActivity(intent); // if no admin and no beheerder, its a user, so go to the user screen
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
