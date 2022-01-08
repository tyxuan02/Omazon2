package com.example.omazonproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.animation.TranslateTransition;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;

/**
 * This class is responsible to control the events happening in the user purchase page
 *
 * @author XiangLun
 */
public class UserPurchasePageController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button orders;

    @FXML
    private Button orderHistory;

    @FXML
    private Button toPay;

    @FXML
    private Label deliveredLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Line underLine;

    @FXML
    private ComboBox<String> productCategory_home;

    @FXML
    private Label unitPriceLabel;

    @FXML
    private Button profileIcon;

    @FXML
    private Button homeIcon;

    @FXML
    private VBox vBox;

    @FXML
    public void initialize() {
        // Set-up category in the combo box
        productCategory_home.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory_home.setPromptText("Select");

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

        displayCartItem();
    }

    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void profileButtonPressed(ActionEvent event) throws IOException {
        // create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-profile-page.fxml"));
        root = fxmlLoader.load();

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // display the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void ordersButtonPressed(ActionEvent event) {
        unitPriceLabel.setVisible(false);
        totalPriceLabel.setVisible(false);
        deliveredLabel.setVisible(true);

        // get the x-position of the cancel payment button and store the value in the currentPos variable
        Bounds boundsInScene = orders.localToScene(orders.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());

        // clear the contents in the vbox
        vBox.getChildren().clear();

        // display orders
        displayOrderedItem();
    }

    @FXML
    void orderHistoryButtonPressed(ActionEvent event) {
        unitPriceLabel.setVisible(true);
        totalPriceLabel.setVisible(true);
        deliveredLabel.setVisible(false);

        // get the x-position of the order history button and store the value in the currentPos variable
        Bounds boundsInScene = orderHistory.localToScene(orderHistory.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());

        // clear the contents in the vbox
        vBox.getChildren().clear();

        // display order history
    }

    @FXML
    void toPayButtonPressed(ActionEvent event) {
        unitPriceLabel.setVisible(true);
        totalPriceLabel.setVisible(true);
        deliveredLabel.setVisible(false);

        // get the x-position of the to pay button and store the value in the currentPos variable
        Bounds boundsInScene = toPay.localToScene(toPay.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());

        // clear the contents in the vbox
        vBox.getChildren().clear();

        // display the cart contents
        displayCartItem();
    }

    /**
     * This method is used to get the current x-position of the line
     *
     * @return current x-position of the line
     * @author XiangLun
     */
    public double getLinePos() {
        Bounds bounds = underLine.localToScene(underLine.getBoundsInLocal());
        return bounds.getCenterX();
    }

    /**
     * This method plays the animation of the underline
     *
     * @param currentPos  x-position where the line wants to go to
     * @param previousPos x-position where the line is currently at
     * @author XiangLun
     */
    public void playAnimation(double currentPos, double previousPos) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(underLine);
        translate.setByX(currentPos - previousPos);
        translate.play();
    }

    /**
     * This method is used to display the user's cart item
     *
     * @author XiangLun
     */
    private void displayCartItem() {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the get cart item list method
        List<CartItem> cartItemList = new ArrayList<>(jsonFileUtil.readCartFile());

        // loop through the cartItemList, fills in the cart item information and add it to the vbox
        for (CartItem cartItem : cartItemList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cart-item-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CartItemController cartItemController = fxmlLoader.getController();
                //scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("cart-item-template.css")).toExternalForm());
                cartItemController.setData(cartItem);
                vBox.getChildren().add(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to display the user's ordered item
     *
     * @author XiangLun
     */
    private void displayOrderedItem() {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the get ordered item list method
        List<OrderedItem> orderedItemList = new ArrayList<>(jsonFileUtil.readOrdersFile());

        // loop through the orderedItemList, fills in the ordered item information and add it to the vbox
        for (OrderedItem orderedItem : orderedItemList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ordered-item-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                OrderedItemController orderedItemController = fxmlLoader.getController();
                orderedItemController.setData(orderedItem);
                vBox.getChildren().add(anchorPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
