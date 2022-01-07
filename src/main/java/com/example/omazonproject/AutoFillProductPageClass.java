package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;

/**
 * This class acts as a controller for the product page
 */
public class AutoFillProductPageClass {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Product currentProduct;
    DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    private ImageView productImage;

    @FXML
    private Label name;

    @FXML
    private Label category;

    @FXML
    private Label country;

    @FXML
    private Label priceLabel;

    @FXML
    private Label productDescription;

    @FXML
    private Label quantity;

    @FXML
    private Label shipFrom;

    @FXML
    private Label stock;
    
    @FXML
    private Button profileIcon;
    
    @FXML
    private Button homeIcon;

    @FXML
    public void autoFill(Product product) {

        //Save the object of the current product
        this.currentProduct = product;

        //Autofill the product information into product page
        priceLabel.setText(String.valueOf(df.format(product.getProductPrice())));
        category.setText(product.getCategory());
        name.setText(product.getProductName());
        country.setText("Malaysia");
        stock.setText(String.valueOf(product.getNumOfStock()));
        shipFrom.setText(product.getAddress());
        productDescription.setText(product.getDescription());

        URL u = this.getClass().getResource("/images/" + product.getProductImagePath() + ".png");
        Image image = new Image(String.valueOf(u));
        productImage.setImage(image);
    }
    
    @FXML
    public void initialize() {
        // Show tooltip message when user point at the icon
        final Tooltip tooltipProfile = new Tooltip();
        tooltipProfile.setText("My Profile");
        profileIcon.setTooltip(tooltipProfile);
        profileIcon.getTooltip().setOnShowing(p -> {
            Bounds bProfile = profileIcon.localToScreen(profileIcon.getBoundsInLocal());
            profileIcon.getTooltip().setX(bProfile.getMaxX() - 70);
            profileIcon.getTooltip().setY(bProfile.getMinY() + 35);
        });

        final Tooltip tooltipHome = new Tooltip();
        tooltipHome.setText("Homepage");
        homeIcon.setTooltip(tooltipHome);
        homeIcon.getTooltip().setOnShowing(h -> {
            Bounds bHome = homeIcon.localToScreen(homeIcon.getBoundsInLocal());
            homeIcon.getTooltip().setX(bHome.getMaxX() - 60);
            homeIcon.getTooltip().setY(bHome.getMinY() + 35);
        });
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

    @FXML
    void addToCartButtonPressed(ActionEvent event) {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        try {
            // use button to choose quantity
            jsonFileUtil.writeCartFile(currentProduct, Integer.parseInt(quantity.getText()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
