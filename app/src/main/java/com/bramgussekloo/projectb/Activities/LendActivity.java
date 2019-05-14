package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bramgussekloo.projectb.R;

public class LendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend);
        Intent intent = getIntent();
        String ProductID = intent.getStringExtra("ProductID");
        Log.d("ProductID", "This is product: " + ProductID);
    }

}
