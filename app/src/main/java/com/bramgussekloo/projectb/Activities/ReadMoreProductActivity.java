package com.bramgussekloo.projectb.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bramgussekloo.projectb.R;
import com.bramgussekloo.projectb.models.Product;
import com.bumptech.glide.Glide;

public class ReadMoreProductActivity extends AppCompatActivity  {

    private static final String TAG = "ReadMoreProductActivity";
    private TextView productTitle;
    private TextView productDescription;
    private TextView productQuantity;
    private ImageView productImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more_product);

        if(getIntent().hasExtra("selected_product")){
            Product product = getIntent().getParcelableExtra("selected_product");

            final String productImageUri = product.getImage_url();

            productImage = findViewById(R.id.more_product_list_image);
            Glide.with(getApplicationContext()).load(productImageUri).into(productImage);

            productTitle = findViewById(R.id.more_reservations_list_title);
            productTitle.setText(product.getTitle());

            productDescription = findViewById(R.id.more_product_description);
            productDescription.setText(product.getDesc());

            productQuantity = findViewById(R.id.more_product_list_quantity);
            productQuantity.setText(Integer.toString(product.getQuantity()));

        }

    }
}
