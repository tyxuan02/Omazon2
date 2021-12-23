package com.example.omazonproject;

/**
 * This class is used to store the current seller's info who had logged-in to the server
 *
 * @author XiangLun
 */
public class Seller {

    // A list of information of the seller
    private static String sellerName;
    private static String email;
    private static String address;
    private static String password;

    // A list of getter
    public static String getSellerName() {
        return sellerName;
    }
    public static String getEmail() {
        return email;
    }
    public static String getAddress() {
        return address;
    }
    public static String getPassword() {
        return password;
    }

    // A list of setter
    public static void setSellerName(String sellerName) {
        Seller.sellerName = sellerName;
    }
    public static void setAddress(String address) {
        Seller.address = address;
    }
    public static void setPassword(String password) {
        Seller.password = password;
    }
    public static void setEmail(String email) {
        Seller.email = email;
    }
}
