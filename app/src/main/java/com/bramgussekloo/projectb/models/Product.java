package com.bramgussekloo.projectb.models;

import android.content.Context;

public class Product {

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


}
