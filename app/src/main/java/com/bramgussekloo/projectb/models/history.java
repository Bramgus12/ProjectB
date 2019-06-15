package com.bramgussekloo.projectb.models;

import java.sql.Timestamp;

public class history {
    public String product;
    //public Timestamp timeOfLend;
    //public Timestamp timeOfReturn;

    public history(){}
    public history(String product, Timestamp timeOfLend, Timestamp timeOfReturn) {
        this.product = product;
        //this.timeOfLend = timeOfLend;
        //this.timeOfReturn = timeOfReturn;
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


}
