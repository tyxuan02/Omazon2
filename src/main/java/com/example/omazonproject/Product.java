package com.example.omazonproject;

/**
 * This class is a blueprint for all product, which will be used in the search page and the seller's product page
 *
 * @author XiangLun
 */
public class Product {
    private String productName;
    private String productImagePath;
    private String sellerEmail;
    private String description;
    private String category;
    private double productPrice;
    private int numOfOneStars;
    private int numOfTwoStars;
    private int numOfThreeStars;
    private int numOfFourStars;
    private int numOfFiveStars;
    private int numberOfSales;
    private int numOfStock;
    private String address;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumOfOneStars() {
        return numOfOneStars;
    }

    public void setNumOfOneStars(int numOfOneStars) {
        this.numOfOneStars = numOfOneStars;
    }

    public int getNumOfTwoStars() {
        return numOfTwoStars;
    }

    public void setNumOfTwoStars(int numOfTwoStars) {
        this.numOfTwoStars = numOfTwoStars;
    }

    public int getNumOfThreeStars() {
        return numOfThreeStars;
    }

    public void setNumOfThreeStars(int numOfThreeStars) {
        this.numOfThreeStars = numOfThreeStars;
    }

    public int getNumOfFourStars() {
        return numOfFourStars;
    }

    public void setNumOfFourStars(int numOfFourStars) {
        this.numOfFourStars = numOfFourStars;
    }

    public int getNumOfFiveStars() {
        return numOfFiveStars;
    }

    public void setNumOfFiveStars(int numOfFiveStars) {
        this.numOfFiveStars = numOfFiveStars;
    }

    public int getNumberOfSales() {
        return numberOfSales;
    }

    public void setNumberOfSales(int numberOfSales) {
        this.numberOfSales = numberOfSales;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImagePath() {
        return productImagePath;
    }

    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    public void setProductStock(int numOfStock) {
        this.numOfStock = numOfStock;
    }

    public int getNumOfStock() {
        return this.numOfStock;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setNumOfStock(int numOfStock) {
        this.numOfStock = numOfStock;
    }
}
