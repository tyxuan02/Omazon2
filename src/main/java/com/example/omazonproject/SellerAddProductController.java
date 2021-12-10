package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SellerAddProductController implements Initializable {

    @FXML
    private ComboBox<String> productCategory;

    @FXML
    private TextArea productDescription;

    @FXML
    private TextField productName;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField imageLocation;

    @Override
    // Set the items viewing in product category
    public void initialize(URL location, ResourceBundle resources) {
        productCategory.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory.setPromptText("Please select");
    }

    @FXML
    public void uploadImageButtonPressed(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
    }

    @FXML
    // SAVE button at seller add product page
    public void saveButtonPressed(MouseEvent event) {
        String productNAME = productName.getText();
        String productPRICE = productPrice.getText();
        String productCATEGORY = String.valueOf(productCategory.getValue());
        String productDESCRIPTION = productDescription.getText();
        String productLOCATION;

        // If product name is entered
        if (!productNAME.isBlank()) {
            // If product price is entered
            if (!productPRICE.isBlank()) {
                try {
                    // If product price entered is valid
                    if (Double.parseDouble(productPRICE) <= Double.MAX_VALUE && Double.parseDouble(productPRICE) > 0) {
                        // If product category is selected
                        if (productCATEGORY.equals("Electronic Devices") || productCATEGORY.equals("Fashion") || productCATEGORY.equals("Food") || productCATEGORY.equals("Health & Beauty") || productCATEGORY.equals("Sports") || productCATEGORY.equals("TV & Home Appliances")) {
                            // If product description is entered
                            if (!productDESCRIPTION.isBlank()) {
                                // Add product information into database
                                addProduct();
                                Alert alert = new Alert(Alert.AlertType.NONE);
                                alert.setTitle("Successful");
                                alert.setContentText("Your product has been added!");
                                alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
                                alert.showAndWait();

                            } else {
                                // If product description is not entered
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Product description is not entered");
                                alert.setContentText("Please enter product description");
                                alert.showAndWait();
                            }
                        } else {
                            // If product category is not selected
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Product category is not selected");
                            alert.setContentText("Please select product category");
                            alert.showAndWait();
                        }
                    }
                } catch (NumberFormatException e) {
                    // If product price entered is invalid
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Product price entered is invalid");
                    alert.setContentText("Please enter a valid product price");
                    alert.showAndWait();
                }
            } else {
                // If product price is not entered
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Product price is not entered");
                alert.setContentText("Please enter a product price");
                alert.showAndWait();
            }
        } else {
            // If product name is not entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product name is not entered");
            alert.setContentText("Please enter a valid product name");
            alert.showAndWait();
        }
    }

    // Let seller add product and store product information in database
    public void addProduct() {
        Connection connectDB = null;
        Statement statement = null;
        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            String name = productName.getText();
            String price = productPrice.getText();
            String category = productCategory.getValue();
            String description = productDescription.getText();
            String location;
            String insertFields = "INSERT INTO product_info (name, price, category, description) VALUES ('";
            String insertValues = name + "','" + price + "','" + category + "','" + description + "')";
            String insertToRegister = insertFields + insertValues;
            statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
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



