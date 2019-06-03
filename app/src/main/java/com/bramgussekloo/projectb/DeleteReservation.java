package com.bramgussekloo.projectb;

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
import com.bramgussekloo.projectb.Activities.ReturnActivity;
import com.bramgussekloo.projectb.models.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class DeleteReservation extends AppCompatActivity {

    // Initialization of the attributes
    private TextView name;
    private TextView ReservationDate;
    private TextView product;
    private Intent intent;
    private Button DeleteButton;
    private Reservation reservation;
    private String ReservationDateString;
    private static final String TAG = "ReturnActivity";






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
        name.setText(reservation.NameId);
        //long millisecond = reservation.getTimestamp().getTime();
        //ReservationDateString = DateFormat.format("dd/MM/yy",new Date(millisecond)).toString();
        //ReservationDate.setText(ReservationDateString);
        //Log.d(TAG, "onCreate: "+ millisecond);


        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("Products/" + reservation.getProduct() + "/reservation").document(reservation.NameId)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        });


    }

}
