package com.example.omazonproject;

/**
 * This class is used to store the current seller's info who had logged-in to the server
 *
 * @author XiangLun
 */
public class Seller {

    /**
     * A static variable named seller with String data type
     */
    private static String sellerName;

    /**
     * A static variable named email with String data type
     */
    private static String email;

    /**
     * A static variable named address with String data type
     */
    private static String address;

    /**
     * A static variable named password with String data type
     */
    private static String password;

    /**
     * A static method to return sellerName in String
     */
    public static String getSellerName() {
        return sellerName;
    }

    /**
     * A static method to return email in String
     */
    public static String getEmail() {
        return email;
    }

    /**
     * A static method to return address in String
     */
    public static String getAddress() {
        return address;
    }

    /**
     * A static method to return password in String
     */
    public static String getPassword() {
        return password;
    }

    /**
     * A static void method to receive a String parameter and set it as sellerName
     */
    public static void setSellerName(String sellerName) {
        Seller.sellerName = sellerName;
    }

    /**
     * A static void method to receive a String parameter and set it as address
     */
    public static void setAddress(String address) {
        Seller.address = address;
    }

    /**
     * A static void method to receive a String parameter and set it as password
     */
    public static void setPassword(String password) {
        Seller.password = password;
    }

    /**
     * A static void method to receive a String parameter and set it as email
     */
    public static void setEmail(String email) {
        Seller.email = email;
    }
}
