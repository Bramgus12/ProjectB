package com.bramgussekloo.projectb.models;

public class Product {

    private String name;
    private String image;
    private String content;
    private String timestamp;

    public Product(String name, String image, String content, String timestamp) {
        this.name = name;
        this.image = image;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
