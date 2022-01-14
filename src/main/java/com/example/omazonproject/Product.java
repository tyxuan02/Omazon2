package com.example.omazonproject;

/**
 * This class is a blueprint for all products, which will be used in the search page and the seller's product page
 *
 * @author XiangLun
 */
public class Product {

    /**
     * A field named productName with String data type
     */
    private String productName;

    /**
     * A field named sellerName with String data type
     */
    private String sellerName;

    /**
     * A field named productImagePath with String data type
     */
    private String productImagePath;

    /**
     * A field named sellerEmail with String data type
     */
    private String sellerEmail;

    /**
     * A field named description with String data type
     */
    private String description;

    /**
     * A field named category with String data type
     */
    private String category;

    /**
     * A field named productPrice with double data type
     */
    private double productPrice;

    /**
     * A field named numOfOneStars with int data type
     */
    private int numOfOneStars;

    /**
     * A field named numOfTwoStars with int data type
     */
    private int numOfTwoStars;

    /**
     * A field named numOfThreeStars with int data type
     */
    private int numOfThreeStars;

    /**
     * A field named numOfFourStars with int data type
     */
    private int numOfFourStars;

    /**
     * A field named numOfFiveStars with int data type
     */
    private int numOfFiveStars;

    /**
     * A field named numberOfSales with int data type
     */
    private int numberOfSales;

    /**
     * A field named numOfStock with int data type
     */
    private int numOfStock;

    /**
     * A field named address with String data type
     */
    private String address;

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
     * A method to return sellerName in String
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * A method to receive a String parameter and set it as sellerName
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * A method to return sellerEmail in String
     */
    public String getSellerEmail() {
        return sellerEmail;
    }

    /**
     * A method to receive a String parameter and set it as sellerEmail
     */
    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    /**
     * A method to return description in String
     */
    public String getDescription() {
        return description;
    }

    /**
     * A method to receive a String parameter and set it as description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * A method to return category in String
     */
    public String getCategory() {
        return category;
    }

    /**
     * A method to receive a String parameter and set it as category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * A method to return numOfOneStars in int
     */
    public int getNumOfOneStars() {
        return numOfOneStars;
    }

    /**
     * A method to receive an int parameter and set it as numOfOneStars
     */
    public void setNumOfOneStars(int numOfOneStars) {
        this.numOfOneStars = numOfOneStars;
    }

    /**
     * A method to return numOfTwoStars in int
     */
    public int getNumOfTwoStars() {
        return numOfTwoStars;
    }

    /**
     * A method to receive an int parameter and set it as numOfTwoStars
     */
    public void setNumOfTwoStars(int numOfTwoStars) {
        this.numOfTwoStars = numOfTwoStars;
    }

    /**
     * A method to return numOfThreeStars in int
     */
    public int getNumOfThreeStars() {
        return numOfThreeStars;
    }

    /**
     * A method to receive an int parameter and set it as numOfThreeStars
     */
    public void setNumOfThreeStars(int numOfThreeStars) {
        this.numOfThreeStars = numOfThreeStars;
    }

    /**
     * A method to return numOfFourStars in int
     */
    public int getNumOfFourStars() {
        return numOfFourStars;
    }

    /**
     * A method to receive an int parameter and set it as numOfFourStars
     */
    public void setNumOfFourStars(int numOfFourStars) {
        this.numOfFourStars = numOfFourStars;
    }

    /**
     * A method to return numOfFiveStars in int
     */
    public int getNumOfFiveStars() {
        return numOfFiveStars;
    }

    /**
     * A method to receive an int parameter and set it as numOfFiveStars
     */
    public void setNumOfFiveStars(int numOfFiveStars) {
        this.numOfFiveStars = numOfFiveStars;
    }

    /**
     * A method to return numberOfSales in int
     */
    public int getNumberOfSales() {
        return numberOfSales;
    }

    /**
     * A method to receive a int parameter and set it as numberOfSales
     */
    public void setNumberOfSales(int numberOfSales) {
        this.numberOfSales = numberOfSales;
    }

    /**
     * A method to return productPrice in double
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * A method to receive a double parameter and set it as productPrice
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * A method to return productImagePath in String
     */
    public String getProductImagePath() {
        return productImagePath;
    }

    /**
     * A method to receive a String parameter and set it as productImagePath
     */
    public void setProductImagePath(String productImagePath) {
        this.productImagePath = productImagePath;
    }

    /**
     * A method to receive an int parameter and set it as numOfStock
     */
    public void setProductStock(int numOfStock) {
        this.numOfStock = numOfStock;
    }

    /**
     * A method to return numOfStock in int
     */
    public int getNumOfStock() {
        return this.numOfStock;
    }

    /**
     * A method to receive a String parameter and set it as address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * A method to return address in String
     */
    public String getAddress() {
        return address;
    }
}
