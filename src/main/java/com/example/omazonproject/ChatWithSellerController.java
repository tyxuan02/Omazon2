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

    private String sellerEmail;
    private String productName;

    @FXML
    private Label sellerNameLabel;

    @FXML
    private TextArea textArea;

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

    public void setInfo(String sellerName, String sellerEmail, String productName) {
        sellerNameLabel.setText(sellerName);
        this.sellerEmail = sellerEmail;
        this.productName = productName;
    }
}
