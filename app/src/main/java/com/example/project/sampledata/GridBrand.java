package com.example.project.sampledata;

import java.util.Map;

public class GridBrand {
    private String nameProduct;
    private double prix;
    private int rate;
    private String image;
    private String description;


    public GridBrand() {
    }

    public GridBrand(String nameProduct, double prix, String image, String description, int rate) {
        this.nameProduct = nameProduct;
        this.prix = prix;
        this.image = image;
        this.description = description;
        this.rate = rate;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public double getPrix() {
        return prix;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public int getRate() {
        return rate;
    }


}
