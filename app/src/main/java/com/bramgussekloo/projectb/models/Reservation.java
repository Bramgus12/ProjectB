package com.bramgussekloo.projectb.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bramgussekloo.projectb.Adapter.ReserveIDClass;

import java.util.Date;


public class Reservation extends ReserveIDClass implements Parcelable {
    public String product;
    public Date timestamp;

    protected Reservation(Parcel in) {
        product = in.readString();
        NameId = in.readString();
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Reservation(Date timestamp) {
        this.timestamp = timestamp;
    }


    public Reservation(){}

    public Reservation(String product) {
        this.product = product;
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product);
        dest.writeString(NameId);
    }
}
