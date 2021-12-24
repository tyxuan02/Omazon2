package com.example.omazonproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import javafx.scene.control.ComboBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;


/**
 * This class is responsible to control the events happening in the homepage
 *
 * @author XiangLun
 */
public class HomepageController {//implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button profileIcon;

    @FXML
    private Button homeIcon;

    @FXML
    private Button logOutIcon;

    @FXML
    private ComboBox<String> productCategory_home;

    @FXML
    public void initialize() {
        productCategory_home.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory_home.setPromptText("select");

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

        final Tooltip tooltipLogOut = new Tooltip();
        tooltipLogOut.setText("Log Out");
        logOutIcon.setTooltip(tooltipLogOut);
        logOutIcon.getTooltip().setOnShowing(l -> {
            Bounds bLog = logOutIcon.localToScreen(logOutIcon.getBoundsInLocal());
            logOutIcon.getTooltip().setX(bLog.getMaxX() - 40);
            logOutIcon.getTooltip().setY(bLog.getMinY() + 35);
        });

    }

    @FXML
    void logOutButtonPressed(ActionEvent event) throws IOException {
        // forward user to the login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void profileButtonPressed(ActionEvent event) throws IOException {
        // create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-profile-page.fxml"));
        root = fxmlLoader.load();

        // create an instance of the UserProfileController class
        UserProfileController userProfileController = fxmlLoader.getController();

        // fill-in the text field before displaying the scene and show or hide the set payment password option
        userProfileController.setInitialContents();

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // display the scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
