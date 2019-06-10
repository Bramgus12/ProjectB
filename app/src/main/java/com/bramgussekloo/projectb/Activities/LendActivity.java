package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Login.MainActivity;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.bramgussekloo.projectb.models.Reservation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.annotation.Nullable;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

public class LendActivity extends AppCompatActivity {

    private Product product;
    private Reservation reservation;
    private int quantity;
    private String Title;
    private EditText EmailEditText;
    private String Email;
    private DatePicker datePicker;
    private Map<String, Object> ProductMap;
    private Button LendButton;
    private TextView email;
    private DatabaseReference mRootref = FirebaseDatabase.getInstance().getReference();
    private Date date;
    private static final String TAG = "LendActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);
        datePicker = findViewById(R.id.datePicker1);
        EmailEditText = findViewById(R.id.EditTextEmailuser);
        email = findViewById(R.id.PlaceHolderEmail);
        LendButton = findViewById(R.id.LendActivityButton);
        Intent intent = getIntent();
        if (intent.getParcelableExtra("Product") != null) {
            product = intent.getParcelableExtra("Product");
            Log.d(TAG, intent.getParcelableExtra("Product").toString());
            Title = product.getTitle();
            Buttons();
        } else {
            reservation = intent.getParcelableExtra("Item");
            Title = reservation.getProduct();
            EmailEditText.setText(reservation.NameId);
            Buttons();
        }
    }

    private void Buttons(){
        LendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = EmailEditText.getText().toString().trim().toLowerCase().replace(".", "").replace("@", "");
                if (EmailEditText == null){
                    Toast.makeText(LendActivity.this, "Please enter a email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                mRootref.child("users").child(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            SetInDatabase();
                        } else {
                            Toast.makeText(LendActivity.this, "user " + Email + " does not exists.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError);
                        Toast.makeText(LendActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void SetInDatabase() {
        FirebaseFirestore.getInstance().collection("Products").document(Title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ProductMap = task.getResult().getData();
                    quantity = Integer.parseInt(ProductMap.get("quantity").toString());
                    Log.d(TAG, "onComplete: " + Email);
                    FirebaseFirestore.getInstance().collection("Products/" + Title + "/reservation").document(Email.replace(".", "").replace("@", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Log.e(TAG, "onComplete: " + task.getResult().getData() );
                            if (task.isSuccessful() && task.getResult().getData() != null){
                                FirebaseFirestore.getInstance().collection("Products/" + Title + "/LendTo").document(Email.replace("@", "").replace(".", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.getResult().getData() != null) {
                                            Map<String, Object> map = task.getResult().getData();
                                            int Qtt = Integer.parseInt(map.get("quantity").toString());
                                            Qtt = Qtt +1;
                                            Calendar combinedCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                                            combinedCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                            map.put("TimeOfLend", FieldValue.serverTimestamp());
                                            map.put("quantity", Qtt);
                                            map.put("year", datePicker.getYear());
                                            map.put("month", datePicker.getMonth());
                                            map.put("day", datePicker.getDayOfMonth());
                                            map.put("product", Title);
                                            map.put("TimeOfReturn", combinedCal.getTime());
                                            FirebaseFirestore.getInstance().collection("Products/" + Title + "/LendTo").document(Email.replace(".", "").replace("@", "")).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ProductMap.put("quantity", quantity);
                                                        FirebaseFirestore.getInstance().collection("Products").document(Title).set(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    FirebaseFirestore.getInstance().collection("Products/" + Title + "/reservation").document(Email.replace(".", "").replace("@", "")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                Toast.makeText(LendActivity.this, "Product is lend to: " + Email, Toast.LENGTH_SHORT).show();
                                                                                goToMain();
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    Log.e(TAG, "onComplete: Exception", task.getException());
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.e(TAG, "onComplete: Exception", task.getException());
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.e(TAG, "onComplete: Field does not exist", task.getException());
                                            Calendar combinedCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                                            combinedCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("quantity", 1);
                                            map.put("TimeOfLend", FieldValue.serverTimestamp());
                                            map.put("year", datePicker.getYear());
                                            map.put("month", datePicker.getMonth());
                                            map.put("day", datePicker.getDayOfMonth());
                                            map.put("product", Title);
                                            map.put("TimeOfReturn", combinedCal.getTime());
                                            FirebaseFirestore.getInstance().collection("Products/" + Title + "/LendTo").document(Email.replace(".", "").replace("@", "")).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ProductMap.put("quantity", quantity);
                                                        FirebaseFirestore.getInstance().collection("Products").document(Title).set(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    FirebaseFirestore.getInstance().collection("Products/" + Title + "/reservation").document(Email.replace(".", "").replace("@", "")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()){
                                                                                Toast.makeText(LendActivity.this, "Product is lend to: " + Email, Toast.LENGTH_SHORT).show();
                                                                                goToMain();
                                                                            }
                                                                        }
                                                                    });
                                                                } else {
                                                                    Log.e(TAG, "onComplete: Exception", task.getException());
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.e(TAG, "onComplete: Exception", task.getException());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            } else   if (quantity == 0){
                                Toast.makeText(LendActivity.this, "Product is not available anymore. Sorry.", Toast.LENGTH_SHORT).show();
                            } else {
                                quantity = quantity - 1;
                                FirebaseFirestore.getInstance().collection("Products/" + Title + "/LendTo").document(Email.replace(".", "").replace("@", "")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.getResult().getData() != null) {
                                            Map<String, Object> map = task.getResult().getData();
                                            Calendar combinedCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                                            combinedCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                            int Qtt = Integer.parseInt(map.get("quantity").toString());
                                            Qtt = Qtt +1;
                                            map.put("TimeOfLend", FieldValue.serverTimestamp());
                                            map.put("quantity", Qtt);
                                            map.put("year", datePicker.getYear());
                                            map.put("month", datePicker.getMonth());
                                            map.put("day", datePicker.getDayOfMonth());
                                            map.put("product", Title);
                                            map.put("TimeOfReturn", combinedCal.getTime());
                                            FirebaseFirestore.getInstance().collection("Products/" + Title + "/LendTo").document(Email.replace(".", "").replace("@", "")).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ProductMap.put("quantity", quantity);
                                                        FirebaseFirestore.getInstance().collection("Products").document(Title).set(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(LendActivity.this, "Product is lend to: " + Email, Toast.LENGTH_SHORT).show();
                                                                    goToMain();
                                                                } else {
                                                                    Log.e(TAG, "onComplete: Exception", task.getException());
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.e(TAG, "onComplete: Exception", task.getException());
                                                    }
                                                }
                                            });
                                        } else {
                                            Log.e(TAG, "onComplete: Field does not exist", task.getException());
                                            Map<String, Object> map = new HashMap<>();
                                            Calendar combinedCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
                                            combinedCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                            map.put("quantity", 1);
                                            map.put("TimeOfLend", FieldValue.serverTimestamp());
                                            map.put("year", datePicker.getYear());
                                            map.put("month", datePicker.getMonth());
                                            map.put("day", datePicker.getDayOfMonth());
                                            map.put("product", Title);
                                            map.put("TimeOfReturn", combinedCal.getTime());
                                            FirebaseFirestore.getInstance().collection("Products/" + Title + "/LendTo").document(Email.replace(".", "").replace("@", "")).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        ProductMap.put("quantity", quantity);
                                                        FirebaseFirestore.getInstance().collection("Products").document(Title).set(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(LendActivity.this, "Product is lend to: " + Email, Toast.LENGTH_SHORT).show();
                                                                    goToMain();
                                                                } else {
                                                                    Log.e(TAG, "onComplete: Exception", task.getException());
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Log.e(TAG, "onComplete: Exception", task.getException());
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }{
                            }
                        }
                    });
                } else {
                        Log.e(TAG, "onComplete: Exception", task.getException());
                }
            }
        });
    }
    private void goToMain(){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
