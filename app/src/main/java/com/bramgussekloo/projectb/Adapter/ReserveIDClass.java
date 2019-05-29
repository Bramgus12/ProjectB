package com.bramgussekloo.projectb.Adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import io.reactivex.annotations.NonNull;

public class ReserveIDClass implements Parcelable {
    @Exclude
    public String NameId;

    protected ReserveIDClass(Parcel in) {
        NameId = in.readString();
    }

    protected ReserveIDClass(){

    }

    public static final Creator<ReserveIDClass> CREATOR = new Creator<ReserveIDClass>() {
        @Override
        public ReserveIDClass createFromParcel(Parcel in) {
            return new ReserveIDClass(in);
        }

        @Override
        public ReserveIDClass[] newArray(int size) {
            return new ReserveIDClass[size];
        }
    };

    public <T extends ReserveIDClass> T withId(@NonNull final String id){
        this.NameId = id;
        return (T) this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(NameId);
    }
}
