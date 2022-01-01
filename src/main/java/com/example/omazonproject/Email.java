package com.example.omazonproject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains method which is responsible to notify the seller when they receive new orders
 *
 * @author Xianglun
 */
public class Email {

    /**
     * This method is used to send notification to the seller to inform them about any new orders
     *
     * @param sellerEmail  email address of the seller
     * @param productName  the name of the product bought by the customer
     * @param quantity     the quantity of the product bought by the customer
     * @param pricePerUnit the price per unit of the product bought by the customer
     * @author XiangLun
     */
    public static void sendNotification(String sellerEmail, String productName, int quantity, double pricePerUnit) throws MessagingException {

        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Read the credentials from .json file and store in the variables
        String myAccountEmail = new JsonFileReader().getEmailAddress();
        String password = new JsonFileReader().getPassword();

        // Create a session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        // Create a new message
        Message message = prepareNotificationForSeller(session, myAccountEmail, sellerEmail, productName, quantity, pricePerUnit);

        // Send message
        assert message != null;
        Transport.send(message);
    }

    /**
     * This method is used to prepare seller notification message to inform them about any new orders
     *
     * @param session        session created in the sendNotification method
     * @param myAccountEmail email address of our Omazon account
     * @param sellerEmail    email address of the recipient
     * @param productName    the name of the product bought by the customer
     * @param quantity       the quantity of the product bought by the customer
     * @param pricePerUnit   the price per unit of the product bought by the customer
     * @return a message containing the customer details and order details
     * @author XiangLun
     */
    private static Message prepareNotificationForSeller(Session session, String myAccountEmail, String sellerEmail, String productName, int quantity, double pricePerUnit) {

        try {
            // retrieve the information of the product
            String customerName = User.getUsername();
            String customerAddress = User.getAddress();

            // Prepare a new message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sellerEmail));
            message.setSubject("[Omazon] You received a new order");
            message.setText("*** This is an automatically generated email. Please do not reply***\n\n" +
                    "You have received a new order on Omazon for " + quantity + String.format(" item(s) totalling RM%.2f\n\n", pricePerUnit * quantity) +
                    "ORDER DETAILS:\n" +
                    "Customer: " + customerName + "\n" +
                    "Customer's address: " + customerAddress + "\n" +
                    "Product ordered: " + productName + "\n" +
                    "Quantity: " + quantity + "\n" +
                    "Grand Total: " + String.format("RM%.2f", pricePerUnit * quantity));
            return message;

        } catch (Exception ex) {
            Logger.getLogger(VerificationEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
