package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UserProfileController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField currentPassword;

    @FXML
    private TextField emailAddress;

    @FXML
    private PasswordField newPassword;

    @FXML
    private PasswordField paymentPassword;

    @FXML
    private TextField pickupAddress;

    @FXML
    private TextField username;

    @FXML
    void accountBalanceButtonPressed(ActionEvent event) {

    }

    @FXML
    void deleteAccountButtonPressed(ActionEvent event) {

    }

    @FXML
    void favouriteListButtonPressed(ActionEvent event) {

    }

    @FXML
    void homepageButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void myPurchaseButtonPressed(ActionEvent event) {

    }

    @FXML
    void saveButtonPressed(ActionEvent event) {

    }

    @FXML
    void startSellingButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
