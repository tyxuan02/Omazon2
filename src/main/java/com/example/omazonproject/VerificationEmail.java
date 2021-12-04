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
     * @throws MessagingException if errors occur while sending email
     */
    public void sendVerificationEmail(String recipient) throws MessagingException {

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
        Message message = prepareMessage(session, myAccountEmail, recipient);
        assert message != null;

        // Send message
        Transport.send(message);
    }

    /**
     * Generate random 6-digits code and store it in the verificationCode field
     */
    private void codeGenerator() {
        // Generate random code with 6 digits
        final int MAX = 999999;
        final int MIN = 100000;
        Random r = new Random();
        this.verificationCode = r.nextInt(MAX - MIN + 1) + MIN;
    }

    /**
     * Prepare the message to be sent
     *
     * @param session        session created in the sendVerificationEmail method
     * @param myAccountEmail email address of our Omazon account
     * @param recipient      email address of the recipient
     * @return a multipart message containing an HTML header and an image
     */
    private Message prepareMessage(Session session, String myAccountEmail, String recipient) {
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

}
