package com.bramgussekloo.projectb.models;

public class SingleProduct {
    private Integer Quantity;
    private String image;
    private String category;
    private String title;
    private String description;
    private String thumb_url;

    public SingleProduct(){
    }

    public SingleProduct(String thumb_url, Integer quantity, String image_url, String category, String title, String desc){
    this.category = category;
    this.thumb_url = thumb_url;
    this.description = desc;
    this.image = image_url;
    this.Quantity = quantity;
    this.title = title;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public String getQuantity() {
        return Quantity.toString();
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
