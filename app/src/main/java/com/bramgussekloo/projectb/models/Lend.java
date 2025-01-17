package com.bramgussekloo.projectb.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.bramgussekloo.projectb.Adapter.ReserveIDClass;

import java.util.Date;

public class Lend extends ReserveIDClass implements Parcelable
{
    private int day;
    private int month;
    private int year;
    private int quantity;
    private String product;
    private java.util.Date TimeOfLend;
    private java.util.Date TimeOfReturn;

    public Lend() {
    }

    public Lend(int day, int month, int year, int quantity, String product, java.util.Date TimeOfLend, java.util.Date TimeOfReturn) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.quantity = quantity;
        this.product = product;
        this.TimeOfLend = TimeOfLend;
        this.TimeOfReturn = TimeOfReturn;
    }

    protected Lend(Parcel in) {
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        quantity = in.readInt();
        product = in.readString();
        TimeOfLend = (java.util.Date) in.readSerializable();
        TimeOfReturn = (java.util.Date) in.readSerializable();
        NameId = in.readString();
    }

    public static final Creator<Lend> CREATOR = new Creator<Lend>() {
        @Override
        public Lend createFromParcel(Parcel in) {
            return new Lend(in);

        }

        @Override
        public Lend[] newArray(int size) {
            return new Lend[size];
        }
    };

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public java.util.Date getTimeOfLend() {
        return TimeOfLend;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setTimeOfLend(java.util.Date timeOfLend) {
        TimeOfLend = timeOfLend;
    }


    @Override
    public String toString() {
        return "Lend{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", quantity=" + quantity +
                ", TimeOfLend=" + TimeOfLend +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Date getTimeOfReturn() {
        return TimeOfReturn;
    }

    public void setTimeOfReturn(Date timeOfReturn) {
        TimeOfReturn = timeOfReturn;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(quantity);
        dest.writeString(product);
        dest.writeSerializable(TimeOfLend);
        dest.writeSerializable(TimeOfReturn);
        dest.writeString(NameId);
    }
}
