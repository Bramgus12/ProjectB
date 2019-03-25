package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bramgussekloo.projectb.R;

public class Beheerder extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beheerder);
        Buttons();
    }
    private Button ProductButton;


    public void Buttons() {
        ProductButton = findViewById(R.id.productbutton);
        ProductButton.setOnClickListener(
                new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ProductIntent = new Intent(getApplicationContext(), ProductActivity.class);
                    startActivity(ProductIntent);
                }
            });
        }

    }

