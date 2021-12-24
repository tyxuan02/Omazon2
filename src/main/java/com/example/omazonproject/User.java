package com.example.omazonproject;

/**
 * This class is used to store the current user's info who had logged-in to the server
 *
 * @author XiangLun
 */
public class User {

    // A list of information of the user
    private static String username;
    private static String email;
    private static String password;
    private static String address;
    private static String paymentPassword;
    private static String balance;

    // A list of getters
    public static String getUsername() {
        return username;
    }
    public static String getEmail() {
        return email;
    }
    public static String getPassword() {
        return password;
    }
    public static String getAddress() {
        return address;
    }
    public static String getPaymentPassword() {
        return paymentPassword;
    }
    public static String getBalance() {
        return balance;
    }

    // A list of setters
    public static void setUsername(String username) {
        User.username = username;
    }
    public static void setEmail(String email) {
        User.email = email;
    }
    public static void setPassword(String password) {
        User.password = password;
    }
    public static void setAddress(String address) {
        User.address = address;
    }
    public static void setPaymentPassword(String paymentPassword) {
        User.paymentPassword = paymentPassword;
    }
    public static void setBalance(String balance) {
        User.balance = balance;
    }

}
