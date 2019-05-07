package com.bramgussekloo.projectb.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Adapter;

import com.bramgussekloo.projectb.Adapter.productId;

public class Product extends productId implements Parcelable {

    public String title;
    public String desc;
    public String image_url;
    public String thumb_url;

    public String category;
    public int quantity;

    public Product(){}

    public Product(String category, String title, String desc, String image_url, String thumb_url, int quantity) {
        this.title = title;
        this.desc = desc;
        this.image_url = image_url;
        this.thumb_url = thumb_url;
        this.quantity = quantity;
        this.category = category;
    }


    protected Product(Parcel in) {
        title = in.readString();
        desc = in.readString();
        image_url = in.readString();
        thumb_url = in.readString();
        category = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(desc);
        parcel.writeString(image_url);
        parcel.writeString(thumb_url);
        parcel.writeString(category);
        parcel.writeInt(quantity);
    }
}
