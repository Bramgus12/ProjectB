package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;

public class ReturnActivity extends AppCompatActivity {
    private static final String TAG = "ReturnActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        Intent intent = getIntent();
        Lend lend = intent.getParcelableExtra("item");
        Log.d(TAG, "onCreate: " + lend.getProduct());
    }
}
