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
import com.bramgussekloo.projectb.models.Reservation;
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

public class DeleteReservation extends AppCompatActivity {

    // Initialization of the attributes
    private TextView name;
    private TextView ReservationDate;
    private TextView product;
    private Intent intent;
    private Button DeleteButton;
    private Reservation reservation;
    private FirebaseFirestore firebaseFirestore;
    private String returnDate;
    private long millisecond;
    private String reservationDateString;
    private DatabaseReference mRootRef;


    private static final String TAG = "deleteActivity";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_reservation);
        name = findViewById(R.id.DeleteNameFilled);
        ReservationDate = findViewById(R.id.ReservationDateFilled);
        product = findViewById(R.id.DeleteProductFilled);
        DeleteButton = findViewById(R.id.DeleteButton);
        intent = getIntent();
        reservation = intent.getParcelableExtra("item");
        product.setText(reservation.getProduct());
        millisecond = reservation.getTimestamp().getTime();
        reservationDateString = DateFormat.format("dd/MM/yy", new Date(millisecond)).toString();
        ReservationDate.setText(reservationDateString);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.child("users").child(reservation.NameId).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Products/" + reservation.getProduct() + "/reservation").document(reservation.NameId)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            FirebaseFirestore.getInstance().collection("Products/").document(reservation.product).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()){
                                            Map<String, Object> object_data = document.getData();
                                            String quantityValue = object_data.get("quantity").toString();
                                            int Qtt = Integer.parseInt(quantityValue);
                                            Qtt = Qtt +1;
                                            final Map<String, Object> ProductMap = task.getResult().getData();
                                            ProductMap.put("quantity", Qtt);
                                            Log.d(TAG, "onComplete: "+quantityValue);
                                            firebaseFirestore.collection("Products").document(reservation.product).set(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(DeleteReservation.this, "Reservation is deleted.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }

                                }
                            });

                        }
                    }
                });
            }
        });


    }

}
