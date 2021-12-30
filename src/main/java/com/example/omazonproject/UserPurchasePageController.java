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
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import javafx.animation.TranslateTransition;

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
    private Button cancelPayment;

    @FXML
    private Button orderHistory;

    @FXML
    private Button toPay;

    @FXML
    private Button toReceived;

    @FXML
    private Button toShip;

    @FXML
    private Line underLine;

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
    void cancelPaymentButtonPressed(ActionEvent event) {
        // get the x-position of the cancel payment button and store the value in the currentPos variable
        Bounds boundsInScene = cancelPayment.localToScene(cancelPayment.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());
    }

    @FXML
    void orderHistoryButtonPressed(ActionEvent event) {
        // get the x-position of the order history button and store the value in the currentPos variable
        Bounds boundsInScene = orderHistory.localToScene(orderHistory.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());
    }

    @FXML
    void toPayButtonPressed(ActionEvent event) {
        // get the x-position of the to pay button and store the value in the currentPos variable
        Bounds boundsInScene = toPay.localToScene(toPay.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());
    }

    @FXML
    void toReceivedButtonPressed(ActionEvent event) {
        // get the x-position of the received button and store the value in the currentPos variable
        Bounds boundsInScene = toReceived.localToScene(toReceived.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());
    }

    @FXML
    void toShipButtonPressed(ActionEvent event) {
        // get the x-position of the to ship button and store the value in the currentPos variable
        Bounds boundsInScene = toShip.localToScene(toShip.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());
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
}
