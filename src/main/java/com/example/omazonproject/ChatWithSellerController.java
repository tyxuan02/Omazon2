package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import javax.mail.MessagingException;

public class ChatWithSellerController {

    /**
     * A field named sellerEmail with String data type
     */
    private String sellerEmail;

    /**
     * A field named productName with String data type
     */
    private String productName;

    /**
     * A label used to display the seller's name
     */
    @FXML
    private Label sellerNameLabel;

    /**
     * A text area for the user to enter their chat message
     */
    @FXML
    private TextArea textArea;

    /**
     * This method is used to send the chat message in the form of an email to the seller when it is pressed,
     * inform the user when the message had sent successfully and close the chat with seller pop-up window.
     *
     * @param event An instance of the ActionEvent class
     * @throws MessagingException when the program fails to send emails
     */
    @FXML
    void sendButtonPressed(ActionEvent event) throws MessagingException {
        // send the chat message to the seller
        Email.sendChatMessage(sellerEmail, productName, textArea.getText());

        // Inform the user that the message sent successfully
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chat Message Sent Successfully");
        alert.setHeaderText(null);
        alert.setContentText("The seller successfully received your message. Please be patient and check your email regularly.");
        alert.showAndWait();

        // close the pop-up window
        Node n = (Node) event.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    /**
     * This method sets the seller's name into the chat with seller pop-up window, and
     * update the sellerEmail and productName field
     *
     * @param sellerName  the seller's name
     * @param sellerEmail the seller's email
     * @param productName the current product's name
     */
    public void setInfo(String sellerName, String sellerEmail, String productName) {
        sellerNameLabel.setText(sellerName);
        this.sellerEmail = sellerEmail;
        this.productName = productName;
    }
}
