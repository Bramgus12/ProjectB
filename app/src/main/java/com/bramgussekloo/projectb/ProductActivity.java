package com.bramgussekloo.projectb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.bramgussekloo.projectb.Adapter.RecyclerViewAdapter;
import com.bramgussekloo.projectb.models.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    // UI

    private RecyclerView mRecyclerView;

    // Variabels

    private ArrayList<Product> mProducts = new ArrayList<>();
    private RecyclerViewAdapter mProductRecyclerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productactivity);

        mRecyclerView = findViewById(R.id.recycler_view);

        InitRecyclerView();
        insertFakeProducts();





    }

    private void insertFakeProducts(){
        Product RaspBerryPi = new Product("Raspberry Pi", "https://cdn.sparkfun.com//assets/parts/1/2/8/2/8/14643-Raspberry_Pi_3_B_-02.jpg", "Dit is een RaspBerry Pi", "05-02-2019");
        Product MicroPython = new Product("MicroPython", "https://i.imgur.com/4uk86YY.jpg", "Dit is een MicroPython", "05-02-2019");

        mProducts.add(RaspBerryPi);
        mProducts.add(MicroPython);

        mProductRecyclerAdapter.notifyDataSetChanged();
    }

    private void InitRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mProductRecyclerAdapter = new RecyclerViewAdapter(this, mProducts);
        mRecyclerView.setAdapter(mProductRecyclerAdapter);
    }


}

