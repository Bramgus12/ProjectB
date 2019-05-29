package com.bramgussekloo.projectb.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Lend;

import java.util.Date;

public class ReturnActivity extends AppCompatActivity {
    private static final String TAG = "ReturnActivity";
    private TextView name;
    private TextView date;
    private TextView lendDate;
    private TextView product;
    private String returnDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        name = findViewById(R.id.ReturnNameFilled);
        date = findViewById(R.id.ReturnDateFilled);
        lendDate = findViewById(R.id.ReturnLendDateFilled);
        product = findViewById(R.id.ReturnProductFilled);

        Intent intent = getIntent();
        Lend lend = intent.getParcelableExtra("item");
        Log.d(TAG, "onCreate: " + lend.getProduct());
        returnDate = lend.getDay() + "/" + lend.getMonth() + "/" + lend.getYear();
        long millisecond = lend.getTimeOfLend().getTime();
        String dateString = DateFormat.format("dd/MM/yy", new Date(millisecond)).toString();
        product.setText(lend.getProduct());
        name.setText(lend.NameId);
        date.setText(returnDate);
        lendDate.setText(dateString);

        Log.d(TAG, "onCreate: " + lend.NameId.toString());
        Log.d(TAG, "onCreate: " + lend.getTimeOfLend());
//        lendDate.setText(lend.getTimeOfLend().toString());
    }
}
