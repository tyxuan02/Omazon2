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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.ComboBox;
import javafx.fxml.Initializable;

/**
 * This class is responsible to control the events happening in the homepage
 */
public class HomepageController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private ComboBox<String> productCategory_home;
    
    @Override
    // Set the items viewing in product category
    public void initialize(URL location, ResourceBundle resources) {
        productCategory_home.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory_home.setPromptText("Select");
    }
    
    @FXML
    void profileButtonPressed(ActionEvent event) throws IOException {
        // create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-profile-page.fxml"));
        root = fxmlLoader.load();

        // create an instance of the UserProfileController class
        UserProfileController userProfileController = fxmlLoader.getController();

        // fill-in the text field before displaying the scene
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
