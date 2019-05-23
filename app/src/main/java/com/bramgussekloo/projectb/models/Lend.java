package com.bramgussekloo.projectb.models;


import com.bramgussekloo.projectb.Adapter.ReserveIDClass;

import java.sql.Date;
import java.sql.Timestamp;

public class Lend extends ReserveIDClass {
    private int day;
    private int month;
    private int year;
    private int quantity;
    private String product;
    private java.util.Date TimeOfLend;

    public Lend() {
    }

    public Lend(int day, int month, int year, int quantity, String product, java.util.Date TimeOfLend) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.quantity = quantity;
        this.product = product;
        this.TimeOfLend = TimeOfLend;
    }

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
}
