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

    /**
     * A label to display balance
     */
    @FXML
    public Label balanceLabel;

    /**
     * A text field to fill price entered by user
     */
    @FXML
    public TextField priceEntered;

    /**
     * This method is used to set and display user account balance after switching the scene
     */
    @FXML
    public void initialize() {
        balanceLabel.setText(String.format("%.2f", User.getBalance()));
    }

    /**
     * This method is used to let user top up and user account balance will increase after clicking it
     * @param event
     */
    @FXML
    public void confirmButtonPressed(ActionEvent event) {

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
                balanceLabel.setText(String.format("%.2f", User.getBalance()));
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

    /**
     * This method will update user account balance in database after user top up
     * @param balance
     */
    public void resetAccountBalance(Double balance) {
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
