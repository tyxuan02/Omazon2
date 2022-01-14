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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible to control the events happening in the user favorite list page
 */

public class UserFavoriteListPageController {

    /**
     * Stage is used to represent a window in a JavaFX desktop application
     */
    private Stage stage;

    /**
     * Scene is the container for all content in a scene graph
     */
    private Scene scene;

    /**
     * Root provides a solution to the issue of defining a reusable component with FXML
     */
    private Parent root;

    /**
     * A button to display profile icon
     */
    @FXML
    private Button profileIcon;

    /**
     * A button to display home icon
     */
    @FXML
    private Button homeIcon;

    /**
     * VBox component is a layout component which positions all its child nodes (components) in a vertical column
     */
    @FXML
    private VBox vBox;

    /**
     * This initialize method set up tooltip message for the icons and display the favorite items
     */
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

        displayFavoriteItem();
    }

    /**
     * This method will direct user to user home page after clicking it
     *
     * @param event An instance of the ActionEvent class
     * @throws IOException when the resource file is not found
     */
    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will direct user to user profile page after clicking it
     *
     * @param event An instance of the ActionEvent class
     * @throws IOException when the resource file is not found
     */
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

    /**
     * This method is used to display the user's favorite item
     *
     * @author XiangLun
     */
    void displayFavoriteItem() {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the get cart item list method
        List<FavoriteItem> favoriteItemList = new ArrayList<>(jsonFileUtil.readFavoriteFile());

        // loop through the cartItemList, fills in the cart item information and add it to the vbox
        for (FavoriteItem favoriteItem : favoriteItemList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("favorite-list-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                FavoriteItemController favoriteItemController = fxmlLoader.getController();
                favoriteItemController.setData(favoriteItem);
                vBox.getChildren().add(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
