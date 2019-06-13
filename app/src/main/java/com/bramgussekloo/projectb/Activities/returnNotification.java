package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bramgussekloo.projectb.Activities.Login.MainActivity;
import com.bramgussekloo.projectb.Adapter.AdminReservationsRecyclerAdapter;
import com.bramgussekloo.projectb.Adapter.NotificationRecyclerAdapter;
import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;
import com.bramgussekloo.projectb.models.fcm.Data;
import com.bramgussekloo.projectb.models.fcm.FCM;
import com.bramgussekloo.projectb.models.fcm.FirebaseCloudMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class returnNotification extends AppCompatActivity implements NotificationRecyclerAdapter.OnNotificationClickListener {

    private RecyclerView return_notifications;
    private List<Lend> lend_list;
    private Lend notificationLend;
    private FirebaseFirestore firebaseFirestore;
    private String email;
    private String token;
    private String serverKey;
    private NotificationRecyclerAdapter notificationRecyclerAdapter;
    private static final String TAG = "returnNotification";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static final String BASE_URL = "https://fcm.googleapis.com/fcm/";
    public static final String ALERT_TITLE = "You've forgot to return a product";
    public static final String ALERT_DESCRIPTION = "Please return the product as soon as possible, Wijnhaven 107 HR.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_notification);

        getServerKey();

        lend_list = new ArrayList<>();
        return_notifications = findViewById(R.id.return_notifications_recyclerview);
        firebaseFirestore = FirebaseFirestore.getInstance();
        notificationRecyclerAdapter = new NotificationRecyclerAdapter(lend_list, this);

        return_notifications.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        return_notifications.setAdapter(notificationRecyclerAdapter);

        firebaseFirestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String productId = doc.getDocument().getId();

                        firebaseFirestore.collection("Products/" + productId + "/LendTo").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        String nameId = doc.getDocument().getId();

                                        Lend lend = doc.getDocument().toObject(Lend.class).withId(nameId);

                                        Date date= new Date();
                                        long time = date.getTime();

                                        java.sql.Timestamp ts = new Timestamp(time);

                                        Log.d(TAG, "onEvent: date" + lend.getTimeOfReturn());

                                        if(lend.getTimeOfReturn().before(ts)){
                                            lend_list.add(lend);
                                            Log.d(TAG, "onEvent after comparison: " + lend);
                                        }

                                        notificationRecyclerAdapter.notifyDataSetChanged();


                                    }
                                }
                            }
                        });
                    }
                }
            }
        });


    }


    @Override
    public void onNotificationClick(int position) {
        notificationLend = lend_list.get(position);
        email = notificationLend.NameId;

        mRootRef.child("users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("messaging_token").getValue() != null){
                        token = dataSnapshot.child("messaging_token").getValue().toString();
                        Log.d(TAG, "onDataChange tokenq:" + token);
                        sendMessageToUser(ALERT_TITLE, ALERT_DESCRIPTION);
                    } else {
                        Toast.makeText(returnNotification.this, "user " + email + " does not have a FCM token currently.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else {
                    Toast.makeText(returnNotification.this, "user " + email + " does not exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(returnNotification.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendMessageToUser(String title, String message){
        Log.d(TAG, "sendMessageToUser: sending message to user");
        Log.d(TAG, "sendMessageToUser token: " + token);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FCM fcmAPI = retrofit.create(FCM.class);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "key="+serverKey);

        Data data = new Data();
        data.setMessage(message);
        data.setTitle(title);
        data.setData_type("return");
        FirebaseCloudMessage firebaseCloudMessage = new FirebaseCloudMessage();
        firebaseCloudMessage.setData(data);
        firebaseCloudMessage.setTo(token);

        Call<ResponseBody> call = fcmAPI.send(headers, firebaseCloudMessage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: Server Response" + response.toString());
                Toast.makeText(returnNotification.this, "Message is successfully sent to the user", Toast.LENGTH_SHORT).show();
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: Unable to send the message" + t.getMessage());

            }
        });



    }

    private void getServerKey(){
        Log.d(TAG, "getServerKey: retrieving server key");

        mRootRef.child("server").child("server_key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue() != null){
                        serverKey = dataSnapshot.getValue().toString();
                        Log.d(TAG, "onDataChange: serverkey : " + serverKey);
                    } else {
                        Toast.makeText(returnNotification.this, "Server Key doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
                Toast.makeText(returnNotification.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
