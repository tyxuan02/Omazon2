package com.example.omazonproject;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains method which is responsible to send verification email to the user while they are signing up
 *
 * @author XiangLun
 */
public class VerificationEmail {

    /**
     * This field stores the verification code generated
     */
    public int verificationCode;

    /**
     * This method will send a verification email to the recipient
     *
     * @param recipient email address of the recipient
     * @param type      type of email to be sent, "user" for user verification email, "seller" for seller verification email,
     *                  and "forgetPassword" for forget password email;
     * @throws MessagingException if errors occur while sending email
     * @author XiangLun
     */
    public void sendVerificationEmail(String recipient, String type) throws MessagingException {

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
        Message message = switch (type) {
            case "user" -> prepareMessageForUser(session, myAccountEmail, recipient);
            case "seller" -> prepareMessageForSeller(session, myAccountEmail, recipient);
            case "forgetPassword" -> prepareMessageForForgetPassword(session, myAccountEmail, recipient);
            default -> null;
        };

        // Send message
        assert message != null;
        Transport.send(message);
    }

    /**
     * Generate random 6-digits code and store it in the verificationCode field
     *
     * @author XiangLun
     */
    public void codeGenerator() {
        // Generate random code with 6 digits
        final int MAX = 999999;
        final int MIN = 100000;
        Random r = new Random();
        this.verificationCode = r.nextInt(MAX - MIN + 1) + MIN;
    }

    /**
     * Prepare the message to be sent to the user
     *
     * @param session        session created in the sendVerificationEmail method
     * @param myAccountEmail email address of our Omazon account
     * @param recipient      email address of the recipient
     * @return a multipart message containing an HTML header and an image
     * @author XiangLun
     */
    public Message prepareMessageForUser(Session session, String myAccountEmail, String recipient) {
        try {
            // Write down the code on the template and save the copy of it in Verification Email.png
            final BufferedImage image = ImageIO.read(new File("assets\\Verification Email Template.png"));
            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(25f));
            g.setColor(new Color(0));
            codeGenerator();
            g.drawString(Integer.toString(verificationCode), 260, 333);
            g.dispose();
            ImageIO.write(image, "png", new File("assets\\Verification Email.png"));

            // Prepare a new message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("[Omazon] Verify Your Omazon Account");

            // Set the message to be a multi-part message
            MimeMultipart multipart = new MimeMultipart("related");

            // html body part
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<H1>Welcome to Omazon!</H1><img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // image body part
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("assets\\Verification Email.png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);

            // Place the contents into the message
            message.setContent(multipart);

            return message;

        } catch (Exception ex) {
            Logger.getLogger(VerificationEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Prepare the message to be sent to the seller
     *
     * @param session        session created in the sendVerificationEmail method
     * @param myAccountEmail email address of our Omazon account
     * @param recipient      email address of the recipient (seller)
     * @return a multipart message containing an HTML header and an image
     * @author XiangLun
     */
    public Message prepareMessageForSeller(Session session, String myAccountEmail, String recipient) {
        try {
            // Write down the code on the template and save the copy of it in Verification Email.png
            final BufferedImage image = ImageIO.read(new File("assets\\Seller Verification Email Template.png"));
            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(25f));
            g.setColor(new Color(0));
            codeGenerator();
            g.drawString(Integer.toString(verificationCode), 260, 333);
            g.dispose();
            ImageIO.write(image, "png", new File("assets\\Seller Verification Email.png"));

            // Prepare a new message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("[Omazon] Verify Your Omazon Seller Account");

            // Set the message to be a multi-part message
            MimeMultipart multipart = new MimeMultipart("related");

            // html body part
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<H1>Welcome to the Seller Centre!</H1><img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // image body part
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("assets\\Seller Verification Email.png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);

            // Place the contents into the message
            message.setContent(multipart);

            return message;

        } catch (Exception ex) {
            Logger.getLogger(VerificationEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Prepare the message to be sent to the user who forgets his/her password
     *
     * @param session        session created in the sendVerificationEmail method
     * @param myAccountEmail email address of our Omazon account
     * @param recipient      email address of the recipient
     * @return a multipart message containing an HTML header and an image
     * @author XiangLun
     */
    public Message prepareMessageForForgetPassword(Session session, String myAccountEmail, String recipient) {
        try {
            // Write down the code on the template and save the copy of it in Verification Email.png
            final BufferedImage image = ImageIO.read(new File("assets\\Forget Password Template.png"));
            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(25f));
            g.setColor(new Color(0));
            codeGenerator();
            g.drawString(Integer.toString(verificationCode), 260, 333);
            g.dispose();
            ImageIO.write(image, "png", new File("assets\\Forget Password.png"));

            // Prepare a new message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("[Omazon] Reset your account password");

            // Set the message to be a multi-part message
            MimeMultipart multipart = new MimeMultipart("related");

            // html body part
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "<H1>Hi there!</H1><img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);

            // image body part
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource("assets\\Forget Password.png");
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);

            // Place the contents into the message
            message.setContent(multipart);

            return message;

        } catch (Exception ex) {
            Logger.getLogger(VerificationEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
