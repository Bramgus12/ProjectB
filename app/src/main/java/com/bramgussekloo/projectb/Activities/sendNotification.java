package com.bramgussekloo.projectb.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.fcm.Data;
import com.bramgussekloo.projectb.models.fcm.FCM;
import com.bramgussekloo.projectb.models.fcm.FirebaseCloudMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class sendNotification extends AppCompatActivity {

    private static final String TAG = "sendNotification";
    private Button notificationButton;
    private String Email;
    private String notificationTitle;
    private String notificationMessage;
    private EditText emailEditText;
    private EditText titleEditText;
    private EditText messageEditText;
    private String token;
    private String serverKey;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static final String BASE_URL = "https://fcm.googleapis.com/fcm/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        emailEditText = findViewById(R.id.EditTextEmailuser);
        titleEditText = findViewById(R.id.PlaceHolderTitleNotification);
        messageEditText = findViewById(R.id.PlaceHolderMessageNotification);
        notificationButton = findViewById(R.id.NotificationActivityButton);
        getServerKey();
        Button();

    }

    private void Button(){
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email = emailEditText.getText().toString().trim().toLowerCase().replace(".", "").replace("@", "");
                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(sendNotification.this, "Please enter a email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                mRootRef.child("users").child(Email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("messaging_token").getValue() != null){
                                token = dataSnapshot.child("messaging_token").getValue().toString();
                                Log.d(TAG, "onDataChange: got messaging token: " + token);
                            } else {
                                Toast.makeText(sendNotification.this, "user " + Email + " does not have a FCM token currently.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } else {
                            Toast.makeText(sendNotification.this, "user " + Email + " does not exists.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError);
                        Toast.makeText(sendNotification.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                notificationTitle = titleEditText.getText().toString();
                if(TextUtils.isEmpty(notificationTitle)){
                    Toast.makeText(sendNotification.this, "Please enter a notification title", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "onClick: NotificationTitle: " + notificationTitle);

                notificationMessage = messageEditText.getText().toString();
                if(TextUtils.isEmpty(notificationMessage)){
                    Toast.makeText(sendNotification.this, "Please enter a notification title", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "onClick: Message " + notificationMessage);

                sendMessageToUser(notificationTitle, notificationMessage);






            }
        });
    }

    private void getServerKey(){
        Log.d(TAG, "getServerKey: retrieving server key.");

        mRootRef.child("server").child("server_key").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.getValue() != null){
                        serverKey = dataSnapshot.getValue().toString();
                        Log.d(TAG, "onDataChange: serverkey : " + serverKey);
                    } else {
                        Toast.makeText(sendNotification.this, "Server Key doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError);
                Toast.makeText(sendNotification.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessageToUser(String title, String message){
        Log.d(TAG, "sendMessageToUser: sending message to user");

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
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: Unable to send the message" + t.getMessage());

            }
        });



    }




}
