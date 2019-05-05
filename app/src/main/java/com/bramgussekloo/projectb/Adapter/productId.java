package com.bramgussekloo.projectb.Adapter;

import com.google.firebase.database.Exclude;

import io.reactivex.annotations.NonNull;

public class productId {
    @Exclude
    public String productId;
    public <T extends productId> T withId(@NonNull final String id){
        this.productId = id;
        return (T) this;
    }
}
