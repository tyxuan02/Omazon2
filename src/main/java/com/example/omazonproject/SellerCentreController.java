package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SellerCentreController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    // This part is responsible to control the events happening at the seller centre

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
        // Forward user to the seller profile page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-profile-page.fxml")));
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
        scene.getStylesheets().add(getClass().getResource("styling.css").toExternalForm());
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


    //This part is responsible to control the events happening at the seller-profile-page

    @FXML
    private PasswordField sellerCurrentPassword;

    @FXML
    private TextField sellerEmail;

    @FXML
    private PasswordField sellerNewPassword;

    @FXML
    private TextField sellerUsername;

    @FXML
    private TextField shopAddress;

    @FXML
    public void accountBalanceButtonPressed(ActionEvent event) {

    }

    @FXML
    public void deleteAccountButtonPressed(ActionEvent event) {

    }

    @FXML
    public void favouriteListButtonPressed(ActionEvent event) {

    }

    @FXML
    public void homepageButtonPressed(ActionEvent event) throws IOException {
        // Forward user to the seller centre
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void myPurchaseButtonPressed(ActionEvent event) {

    }

    @FXML
    public void saveButtonPressed(ActionEvent event) {

    }

    @FXML
    public void startSellingButtonPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //This part is responsible to control the events happening at the seller-add-product-page

}

