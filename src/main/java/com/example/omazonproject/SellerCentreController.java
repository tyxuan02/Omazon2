package com.example.omazonproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * This controller is responsible to control the events happening in the Seller Centre
 */
public class SellerCentreController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    // The seller name menu button(manage profile and logout) at seller centre
    private MenuButton sellerNameMenuButton;

    @FXML
    // The menu button(customer review, delivery status and orders) at seller centre
    private MenuButton menuButton;

    @FXML
    // First product description entered by seller at seller centre
    private TextArea description1;

    @FXML
    // Second product description entered by seller at seller centre
    private TextArea description2;

    @FXML
    // Third product description entered by seller at seller centre
    private TextArea description3;

    @FXML
    // Fourth product description entered by seller at seller centre
    private TextArea description4;

    @FXML
    // Manage profile selection at sellerNameMenuButton at seller centre
    public void manageProfilePressed(ActionEvent event) throws IOException {
        // create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-profile-page.fxml"));
        root = fxmlLoader.load();

        // create an instance of the UserProfileController class
        SellerProfileController sellerProfileController = fxmlLoader.getController();

        // fill-in the text field before displaying the scene and show or hide the set payment password option
        sellerProfileController.setSellerInitialContents();

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // display the scene
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Logout selection at sellerNameMenuButton at seller centre
    public void logoutPressed(ActionEvent event) throws IOException {
        // Forward user to seller login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Add product button at seller centre
    public void addProductButtonPressed(ActionEvent event) throws IOException {
        // Forward user to seller add product page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-add-product-page.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Please add your product");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    // Remove product button at seller centre
    public void removeProductButtonPressed(ActionEvent event) {
    }

    @FXML
    // Customer review selection at menuButton at seller centre
    public void customerReviewPressed(ActionEvent event) {
    }

    @FXML
    // Delivery status selection at menuButton at seller centre
    public void deliveryStatusPressed(ActionEvent event) {
    }

    @FXML
    // Orders selection at menuButton at seller centre
    public void ordersPressed(ActionEvent event) {
    }


    @FXML
    // First update button at seller centre
    public void update1ButtonPressed(MouseEvent event) {
    }

    @FXML
    // Second update button at seller centre
    public void update2ButtonPressed(MouseEvent event) {
    }

    @FXML
    // Third update button at seller centre
    public void update3ButtonPressed(MouseEvent event) {
    }

    @FXML
    // Fourth update button at seller centre
    public void update4ButtonPressed(MouseEvent event) {
    }

    public void setSellerName() {
        // fill-in seller name in the menu button
        sellerNameMenuButton.setText("  " + Seller.getSellerName());
    }

}

