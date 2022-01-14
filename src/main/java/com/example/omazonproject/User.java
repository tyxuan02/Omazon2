package com.example.omazonproject;

/**
 * This class is used to store the current user's info who had logged-in to the server
 *
 * @author XiangLun
 */
public class User {

    /**
     * A static variable named username with String data type
     */
    private static String username;

    /**
     * A static variable named email with String data type
     */
    private static String email;

    /**
     * A static variable named password with String data type
     */
    private static String password;

    /**
     * A static variable named address with String data type
     */
    private static String address;

    /**
     * A static variable named paymentPassword with String data type
     */
    private static String paymentPassword;

    /**
     * A static variable named balance with double data type
     */
    private static double balance;

    /**
     * A static method to return username in String
     */
    public static String getUsername() {
        return username;
    }

    /**
     * A static method to return email in String
     */
    public static String getEmail() {
        return email;
    }

    /**
     * A static method to return password in String
     */
    public static String getPassword() {
        return password;
    }

    /**
     * A static method to return address in String
     */
    public static String getAddress() {
        return address;
    }

    /**
     * A static method to return paymentPassword in String
     */
    public static String getPaymentPassword() {
        return paymentPassword;
    }

    /**
     * A static method to return balance in double
     */
    public static double getBalance() {
        return balance;
    }

    /**
     * A static void method to receive a String parameter and set it as username
     */
    public static void setUsername(String username) {
        User.username = username;
    }

    /**
     * A static void method to receive a String parameter and set it as email
     */
    public static void setEmail(String email) {
        User.email = email;
    }

    /**
     * A static void method to receive a String parameter and set it as password
     */
    public static void setPassword(String password) {
        User.password = password;
    }

    /**
     * A static void method to receive a String parameter and set it as address
     */
    public static void setAddress(String address) {
        User.address = address;
    }

    /**
     * A static void method to receive a String parameter and set it as paymentPassword
     */
    public static void setPaymentPassword(String paymentPassword) {
        User.paymentPassword = paymentPassword;
    }

    /**
     * A static void method to receive a double parameter and set it as balance
     */
    public static void setBalance(double balance) {User.balance = balance;}

}
