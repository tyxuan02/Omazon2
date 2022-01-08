package com.example.omazonproject;

/**
 * This class is a blueprint for all favorite item
 *
 * @author XiangLun
 */
public class FavoriteItem {
    private String productName;
    private String favoriteImagePath;
    private double pricePerUnit;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFavoriteImagePath() {
        return favoriteImagePath;
    }

    public void setFavoriteImagePath(String favoriteImagePath) {
        this.favoriteImagePath = favoriteImagePath;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
