package com.bramgussekloo.projectb.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chooseUser extends AppCompatActivity {
    private TextView textView;
    private DatabaseReference mDatabase;
    private Spinner Spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> users = new ArrayList<>();
                final HashMap<String,String> spinnerMap = new HashMap<>();
                for (DataSnapshot userSnapShot: dataSnapshot.getChildren()){
                    String userId = userSnapShot.getKey();
                    String userName = userSnapShot.child("Name").getValue(String.class);
                    String userRole = userSnapShot.child("Role").getValue(String.class);
                    if (!userRole.equals("Admin")){
                        users.add(userName);
                        spinnerMap.put(userName,userId);
                    }
                }
                Spinner  = findViewById(R.id.spinnerUserName);
                ArrayAdapter<String> UsersAdapter = new ArrayAdapter<>(chooseUser.this,android.R.layout.simple_spinner_dropdown_item,users);
                UsersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner.setAdapter(UsersAdapter);
                Button chooseUserButton  = findViewById(R.id.chooseUserButton);
                chooseUserButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String userName= Spinner.getSelectedItem().toString();
                        final String userId = spinnerMap.get(userName);
                        mDatabase.child("users").child(userId).child("Role").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Button ChangeRoleButton = findViewById(R.id.ChangeUserRole);
                                final String userRole = dataSnapshot.getValue().toString();
                                if (userRole!= null && userRole.equals("User")){
                                    ChangeRoleButton.setText("Change to Beheerder");
                                }else if (userRole!= null && userRole.equals("Beheerder")){
                                    ChangeRoleButton.setText("Change to User");
                                }else {
                                    Toast.makeText(chooseUser.this,"something goes wrong!",Toast.LENGTH_SHORT).show();
                                }
                                textView = findViewById(R.id.textRoleUser);
                                textView.setText(userName + " has a role as a " + userRole);
                                textView.setVisibility(View.VISIBLE);
                                ChangeRoleButton.setVisibility(View.VISIBLE);
                                ChangeRoleButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (userRole.equals("User")){
                                            mDatabase.child("users").child(userId).child("Role").setValue("Beheerder");
                                            Toast.makeText(chooseUser.this,"Role has been successfully changed",Toast.LENGTH_SHORT).show();

                                        }else if (userRole.equals("Beheerder")){
                                            mDatabase.child("users").child(userId).child("Role").setValue("User");
                                            Toast.makeText(chooseUser.this,"Role has been successfully changed",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
