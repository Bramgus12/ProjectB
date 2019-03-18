package com.bramgussekloo.projectb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.bramgussekloo.projectb.models.Product;

public class ProductEditActivity extends AppCompatActivity {

    private static final String TAG = "ProductEditActivity";

    // UI
    private EditText mEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;


    // Variabelen
    private boolean mIsNewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        mEditText = findViewById(R.id.product_text);
        mEditTitle = findViewById(R.id.product_edit_title);
        mViewTitle = findViewById(R.id.product_text_title);

        if(getIncomingIntent()){
            // new product -> (edit mode)
        }
        else{
            // not new product -> (view mode)
        }



        


    }

    // Checks if its a new product
    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_product")){
            Product incomingProduct = getIntent().getParcelableExtra("selected_product");


            mIsNewProduct = false;

            return false;
        }
        mIsNewProduct = true;
        return true;
    }
}
