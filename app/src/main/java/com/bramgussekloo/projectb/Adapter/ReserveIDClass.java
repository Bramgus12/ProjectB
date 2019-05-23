package com.bramgussekloo.projectb.Adapter;

import com.google.firebase.database.Exclude;

import io.reactivex.annotations.NonNull;

public class ReserveIDClass {
    @Exclude
    public String NameId;
    public <T extends ReserveIDClass> T withId(@NonNull final String id){
        this.NameId = id;
        return (T) this;
    }
}
