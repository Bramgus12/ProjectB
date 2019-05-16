package com.bramgussekloo.projectb.Activities.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Admin;
import com.bramgussekloo.projectb.Activities.Beheerder;
import com.bramgussekloo.projectb.Activities.User;
import com.bramgussekloo.projectb.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    protected void onStart() { // will automatically run in background
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){ // if user hasn't login, redirect to login
            Intent LoginActivity = new Intent(getApplicationContext(), com.bramgussekloo.projectb.Activities.Login.LoginActivity.class);
            startActivity(LoginActivity);
            finish();
        }
        if(currentUser != null){ // if user has logged in
            if (currentUser.isEmailVerified()){
                userInformation();
            } else {
                currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mAuth.signOut();
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        }

    }

    private void userInformation(){
        FirebaseDatabase database = FirebaseDatabase.getInstance(); // get the database
        final DatabaseReference mRootRef = database.getReference(); // ref to the database
        FirebaseUser CurrentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // get info about the user that is trying to log in
        final String Uid = CurrentFirebaseUser.getEmail().replace(".", "").replace("@", ""); // get the UID of the user
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
