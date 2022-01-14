package com.example.omazonproject;


/**
 * This class is a blueprint for all product that reviewed by customer, which will be used in the Product Page and Seller Edit Product Page
 */
public class ProductReview {

    /**
     * A field named userReview with String data type
     */
    private String userReview;

    /**
     * A field named username with String data type
     */
    private String username;

    /**
     * A field named sellerReply with String data type
     */
    private String sellerReply;

    /**
     * A field named imageName with String data type
     */
    private String imageName;

    /**
     * A field named numOfStars with int data type
     */
    private int numOfStars;

    /**
     * A method to return userReview in String
     */
    public String getUserReview() {
        return userReview;
    }

    /**
     * A method to receive a String parameter and set it as userReview
     */
    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }

    /**
     * A method to return username in String
     */
    public String getUsername() {
        return username;
    }

    /**
     * A method to receive a String parameter and set it as username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * A method to return sellerReply in String
     */
    public String getSellerReply() {
        return sellerReply;
    }

    /**
     * A method to receive a String parameter and set it as sellerReply
     */
    public void setSellerReply(String sellerReply) {
        this.sellerReply = sellerReply;
    }

    /**
     * A method to return imageName in String
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * A method to receive a String parameter and set it as imageName
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * A method to return numOfStars in int
     */
    public int getNumOfStars() {
        return numOfStars;
    }

    /**
     * A method to receive an int parameter and set it as numOfStars
     */
    public void setNumOfStars(int numOfStars) {
        this.numOfStars = numOfStars;
    }
}
