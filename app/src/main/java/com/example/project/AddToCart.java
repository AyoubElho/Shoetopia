package com.example.project;

public class AddToCart {
    private int Quantité;
    private String currentDate;
    private String currentTime;
    private String id;
    private String imgProduct;
    private String productName;
    private String productPrice;
    private int size;

    // Constructor

    public AddToCart() {

    }

    public AddToCart(int Quantité, String currentDate, String currentTime, String id, String imgProduct,
                     String productName, String productPrice, int size) {
        this.Quantité = Quantité;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.id = id;
        this.imgProduct = imgProduct;
        this.productName = productName;
        this.productPrice = productPrice;
        this.size = size;
    }

    // Getter methods (you can generate these automatically in most IDEs)
    public int getQuantité() {
        return Quantité;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getId() {
        return id;
    }

    public String getImgProduct() {
        return imgProduct;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public int getSize() {
        return size;
    }
}
