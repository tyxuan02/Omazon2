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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.w3c.dom.events.EventException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * This class is responsible to control the events happening in the homepage
 *
 * @author XiangLun
 */
public class HomepageController {

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
    private TextField searchItems;

    ArrayList<String> productName = new ArrayList<>();

    @FXML
    public void initialize() throws Exception {

        productCategory_home.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory_home.setPromptText("select");

        //Autocomplete search
        //If the product category is not selected, view all the product in database
        getProductNameFromDatabase("select");
        AtomicReference<AutoCompletionBinding> acb = new AtomicReference<>(TextFields.bindAutoCompletion(searchItems, productName));

        //Select product category
        productCategory_home.setOnAction(event -> {
            try {
                String get = productCategory_home.getSelectionModel().getSelectedItem();
                if (get != null) {
                    acb.get().dispose();
                    productName.clear();
                    getProductNameFromDatabase(get);
                    acb.set(TextFields.bindAutoCompletion(searchItems, productName));
                }
            } catch (EventException e) {

            }
        });

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

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // display the scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    // This method is used to get the product name from database and store it in an array list named productName
    // Therefore it can be used by the autocomplete search
    void getProductNameFromDatabase (String PRODUCTCATEGORY) {

            Connection connectDB = null;
            Statement statement = null;
            ResultSet Result = null;

            try {
                DatabaseConnection connectNow = new DatabaseConnection();
                connectDB = connectNow.getConnection();
                statement = connectDB.createStatement();

                if (PRODUCTCATEGORY.equals("select")) {
                    Result = statement.executeQuery("SELECT * FROM product_info");
                } else {
                    Result = statement.executeQuery("SELECT * FROM product_info where category = '" + PRODUCTCATEGORY + "'");
                }

                while (Result.next()) {
                    String PRODUCTNAME = Result.getString("name");
                        productName.add(PRODUCTNAME);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (Result != null) {
                    try {
                        Result.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connectDB != null) {
                    try {
                        connectDB.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

}
