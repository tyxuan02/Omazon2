package com.example.omazonproject;

/**
 * This class is a blueprint for all favorite item
 *
 * @author XiangLun
 */
public class FavoriteItem {

    /**
     * A field named productName with String data type
     */
    private String productName;

    /**
     * A field named favoriteImagePath with String data type
     */
    private String favoriteImagePath;

    /**
     * A field named pricePerUnit with double data type
     */
    private double pricePerUnit;

    /**
     * A method to return productName in String
     */
    public String getProductName() {
        return productName;
    }

    /**
     * A method to receive a String parameter and set it as productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * A method to return favoriteImagePath in String
     */
    public String getFavoriteImagePath() {
        return favoriteImagePath;
    }

    /**
     * A method to receive a String parameter and set it as favoriteImagePath
     */
    public void setFavoriteImagePath(String favoriteImagePath) {
        this.favoriteImagePath = favoriteImagePath;
    }

    /**
     * A method to return pricePerUnit in double
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * A method to receive a double parameter and set it as pricePerUnit
     */
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
