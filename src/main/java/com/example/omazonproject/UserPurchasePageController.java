package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class is responsible to control the events happening in the user purchase page
 */
public class UserPurchasePageController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Line cancelLine;

    @FXML
    private Line orderHistoryLine;

    @FXML
    private Line toPayLine;

    @FXML
    private Line toReceivedLine;

    @FXML
    private Line toShipLine;

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        toPayLine.setVisible(false);
        cancelLine.setVisible(true);
        toShipLine.setVisible(false);
        toReceivedLine.setVisible(false);
        orderHistoryLine.setVisible(false);
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
    void orderHistoryButtonPressed(ActionEvent event) {
        toPayLine.setVisible(false);
        cancelLine.setVisible(false);
        toShipLine.setVisible(false);
        toReceivedLine.setVisible(false);
        orderHistoryLine.setVisible(true);
    }

    @FXML
    void profileButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void toPayButtonPressed(ActionEvent event) {
        toPayLine.setVisible(true);
        cancelLine.setVisible(false);
        toShipLine.setVisible(false);
        toReceivedLine.setVisible(false);
        orderHistoryLine.setVisible(false);
    }

    @FXML
    void toReceivedButtonPressed(ActionEvent event) {
        toPayLine.setVisible(false);
        cancelLine.setVisible(false);
        toShipLine.setVisible(false);
        toReceivedLine.setVisible(true);
        orderHistoryLine.setVisible(false);
    }

    @FXML
    void toShipButtonPressed(ActionEvent event) {
        toPayLine.setVisible(false);
        cancelLine.setVisible(false);
        toShipLine.setVisible(true);
        toReceivedLine.setVisible(false);
        orderHistoryLine.setVisible(false);
    }

}
