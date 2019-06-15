package com.bramgussekloo.projectb.Activities;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class statistics extends AppCompatActivity {
    // Initialization of the attributes
    private DatabaseReference mDatabase;
    private FirebaseFirestore firebaseFirestore;
    private TextView users;
    private TextView products;
    private TextView reservations;
    private TextView lendItems;
    long ReservationSize = 0;
    long lendSize = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        getSupportActionBar().setTitle("Statistics"); // sets title for toolbar
        // assignment of all the attributes
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        users = findViewById(R.id.textBlok1);
        products = findViewById(R.id.textBlok2);
        reservations = findViewById(R.id.textBlok3);
        lendItems = findViewById(R.id.textBlok4);
        //users counts
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int max = (int) dataSnapshot.getChildrenCount();
                users.setText(""+dataSnapshot.getChildrenCount());
                animateTextView(0,max,users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //products count
        firebaseFirestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                long size = task.getResult().size();
                products.setText(""+size);
                animateTextView(0,(int)size,products);


            }
        });
        firebaseFirestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot Doc: task.getResult()){
                    if (Doc.exists()){
                        String productId = Doc.getId();
                        //reservation count
                        firebaseFirestore.collection("Products/" + productId + "/reservation").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.getResult().isEmpty()){
                                    ReservationSize = ReservationSize + task.getResult().size();
                                    reservations.setText(""+ReservationSize);
                                    animateTextView(0,(int) ReservationSize,reservations);
                                }
                            }
                        });
                        //lend items count
                        firebaseFirestore.collection("Products/" + productId + "/LendTo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (!task.getResult().isEmpty()){
                                    lendSize = lendSize + task.getResult().size();
                                    lendItems.setText(""+lendSize);
                                    animateTextView(0,(int) lendSize,lendItems);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    //Animate TextView to increase integer and stop at some point
    //bron : https://stackoverflow.com/questions/26502819/animate-textview-to-increase-integer-and-stop-at-some-point
    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 150) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(String.valueOf(finalCount));
                }
            }, time);
        }
    }
}
