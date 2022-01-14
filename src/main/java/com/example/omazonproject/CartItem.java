package com.example.omazonproject;

/**
 * This class is a blueprint for all cart item, which will be used in the purchase page
 *
 * @author XiangLun
 */
public class CartItem {

    /**
     * An instance variable named productName with String data type
     */
    public String productName;

    /**
     * An instance variable named cartItemPath with String data type
     */
    public String cartImagePath;

    /**
     * An instance variable named quantity with int data type
     */
    public int quantity;

    /**
     * An instance variable named pricePerUnit with double data type
     */
    public double pricePerUnit;

    /**
     * An instance variable named sellerEmail with String data type
     */
    public String sellerEmail;

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
     * A method to return cartImagePath in String
     */
    public String getCartImagePath() {
        return cartImagePath;
    }

    /**
     * A method to receive a String parameter and set it as cartItemPath
     */
    public void setCartImagePath(String cartImagePath) {
        this.cartImagePath = cartImagePath;
    }

    /**
     * A method to return quantity in int
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * A method to receive a int parameter and set it as quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
