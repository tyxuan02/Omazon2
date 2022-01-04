package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;

public class AutoFillProductPageClass {

    private Stage stage;
    private Scene scene;
    private Parent root;

    DecimalFormat df = new DecimalFormat("0.00");


    @FXML
    private ImageView productImage;

    @FXML
    private Label brand;

    @FXML
    private Label category;

    @FXML
    private Label country;

    @FXML
    private Label priceLabel;

    @FXML
    private Label productDescription;

    @FXML
    private TextField quantity;

    @FXML
    private Label shipFrom;

    @FXML
    private Label stock;

    @FXML
    public void autoFill(Product product) {

        //Autofill the product information into product page
        priceLabel.setText(String.valueOf(df.format(product.getProductPrice())));
        category.setText(product.getCategory());
        brand.setText(product.getProductName());
        country.setText("Malaysia");
        stock.setText(String.valueOf(product.getNumOfStock()));
        shipFrom.setText(product.getAddress());
        productDescription.setText(product.getDescription());

        URL u = this.getClass().getResource("/images/" + product.getProductImagePath() + ".png");
        Image image = new Image(String.valueOf(u));
        productImage.setImage(image);
    }

    @FXML
    void homeButtonPressed(MouseEvent event) throws IOException {

        // Forward user to home page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void userProfileButtonPressed(MouseEvent event) throws IOException {

        // Forward user to user profile page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

}
