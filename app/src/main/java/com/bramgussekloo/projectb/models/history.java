package com.bramgussekloo.projectb.models;

import java.util.Date;

public class history {
    public String product;
    public Date timeOfLend;
    public Date timeOfReturn;

    public history(){}

    public history(String product, Date timeOfLend, Date timeOfReturn) {
        this.product = product;
        this.timeOfLend = timeOfLend;
        this.timeOfReturn = timeOfReturn;
    }

    public Date getTimeOfLend() {
        return timeOfLend;
    }

    public void setTimeOfLend(Date timeOfLend) {
        this.timeOfLend = timeOfLend;
    }

    public Date getTimeOfReturn() {
        return timeOfReturn;
    }

    public void setTimeOfReturn(Date timeOfReturn) {
        this.timeOfReturn = timeOfReturn;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

}
