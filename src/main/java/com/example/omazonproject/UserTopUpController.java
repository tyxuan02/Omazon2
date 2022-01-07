package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserTopUpController {

    @FXML
    public Label balanceLabel;

    @FXML
    private TextField priceEntered;

    @FXML
    public void initialize() {
        balanceLabel.setText(String.valueOf(User.getBalance()));
    }

    @FXML
    void confirmButtonPressed(ActionEvent event) {

        try {
            if ((!priceEntered.getText().isBlank()) && (Double.parseDouble(priceEntered.getText()) <= Double.MAX_VALUE && Double.parseDouble(priceEntered.getText()) > 0)) {
                // if top up value is entered
                // display top up successful pop-up message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Top Up Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your account balanced has increased");
                alert.showAndWait();
                double currentBalance = User.getBalance() + Double.parseDouble(priceEntered.getText());
                User.setBalance(currentBalance);
                balanceLabel.setText(String.valueOf(User.getBalance()));
                resetAccountBalance(User.getBalance());

                // top up page will close automatically when product information has been added
                Node n = (Node) event.getSource();
                Stage stage = (Stage) n.getScene().getWindow();
                stage.close();

            } else {
                // if top up value is not entered
                // display top up unsuccessful pop-up message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Top Up Unsuccessful");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid amount");
                alert.showAndWait();
            }
        } catch (NumberFormatException e) {
            // If top up value entered is invalid
            // display top up unsuccessful pop-up message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Top Up Unsuccessful");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid amount");
            alert.showAndWait();
        }

    }

    // update user account in database
    void resetAccountBalance(Double balance) {
        Connection connectDB = null;
        Statement statement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String updateBalance = "UPDATE user_account set balance = '" + User.getBalance() + "'" + " WHERE email = '" + User.getEmail() + "'";
            statement = connectDB.createStatement();
            statement.executeUpdate(updateBalance);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connectDB != null) {
                try {
                    connectDB.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
