package com.forever.model;

import java.io.Serializable;

public class Product implements Serializable {

    private Long productId;
    private String productDesc;
    private String productName;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productDesc='" + productDesc + '\'' +
                ", productName='" + productName + '\'' +
                '}';
    }
}
