package com.bramgussekloo.projectb.Adapter;

import com.google.firebase.database.Exclude;

import io.reactivex.annotations.NonNull;

public class ProductIdClass {
    @Exclude
    public String ProductId;
    public <T extends ProductIdClass> T withId(@NonNull final String id){
        this.ProductId = id;
        return (T) this;
    }
}
