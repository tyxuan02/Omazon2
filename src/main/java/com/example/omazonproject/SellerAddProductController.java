package com.example.omazonproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
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
    private ImageView productImage;

    @Override
    // Set the items viewing in product category
    public void initialize(URL location, ResourceBundle resources) {
        productCategory.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory.setPromptText("Please select");
    }

    @FXML
    // UPLOAD button at seller add product page
    public void uploadImageButtonPressed(MouseEvent event) {

        // Seller need to enter product name before uploading product image

        if (productName.getText().isBlank()) {
            // If product name is not entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product name is not entered");
            alert.setContentText("Please enter product name");
            alert.showAndWait();
        } else {
            // View product image
            viewImage();
        }
    }


    // Method that used to view image at productImage image view
    public void viewImage() {
        try {
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload product image");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            Image image = new Image(selectedFile.toURI().toString(), 200, 200, false, false);
            productImage.setImage(image);
        } catch (NullPointerException e) {
        }
    }


    @FXML
    // SAVE button at seller add product page
    public void saveButtonPressed(MouseEvent event) {
        String productNAME = productName.getText();
        String productPRICE = productPrice.getText();
        String productCATEGORY = String.valueOf(productCategory.getValue());
        String productDESCRIPTION = productDescription.getText();

        // If product name is entered
        if (!productNAME.isBlank())
            // If product price is entered
            if (!productPRICE.isBlank()) {
                try {
                    // If product price entered is valid
                    if (Double.parseDouble(productPRICE) <= Double.MAX_VALUE && Double.parseDouble(productPRICE) > 0) {
                        // If product category is selected
                        if (productCATEGORY.equals("Electronic Devices") || productCATEGORY.equals("Fashion") || productCATEGORY.equals("Food") || productCATEGORY.equals("Health & Beauty") || productCATEGORY.equals("Sports") || productCATEGORY.equals("TV & Home Appliances")) {
                            // If product description is entered
                            if (!productDESCRIPTION.isBlank()) {
                                // If product image is uploaded
                                if (productImage.getImage() != null) {
                                    // Add product information into database
                                    addProduct();
                                    Alert alert = new Alert(Alert.AlertType.NONE);
                                    alert.setTitle("Successful");
                                    alert.setContentText("Your product has been added!");
                                    alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
                                    alert.showAndWait();
                                } else {
                                    // If product image is not uploaded
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("Product image is not uploaded");
                                    alert.setContentText("Please upload product image");
                                    alert.showAndWait();
                                }
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
        else {
            // If product name is not entered
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product name is not entered");
            alert.setContentText("Please enter product name");
            alert.showAndWait();
        }

    }

    // Let seller add product and store product information into database
    // Save product image into "image" folder
    public void addProduct() {
        Random r = new Random();
        Connection connectDB = null;
        Statement statement = null;
        ResultSet productImageNameResult = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();
            String productIMAGENAME = productName.getText();
            productImageNameResult = statement.executeQuery("SELECT imageName FROM product_info");

            // If the image name has already been used, add a character behind the image name
            while (productImageNameResult.next()) {
                String getImageName = productImageNameResult.getString("imageName");
                if (getImageName.equals(productIMAGENAME)) {
                    productIMAGENAME += (char) ('a' + r.nextInt(26));
                }
            }

            // Store image into folder
            File fileoutput = new File("src/main/resources/images/" + productIMAGENAME + ".png");
            BufferedImage BI = SwingFXUtils.fromFXImage(productImage.getImage(), null);
            ImageIO.write(BI, "png", fileoutput);

            // Store product information into database
            String price = productPrice.getText();
            String category = productCategory.getValue();
            String description = productDescription.getText();
            String imageName = productIMAGENAME;
            String insertFields = "INSERT INTO product_info (name, price, category, description, imageName) VALUES ('";
            String insertValues = productName.getText() + "','" + price + "','" + category + "','" + description + "','" + imageName + "')";
            String insertToRegister = insertFields + insertValues;
            statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (productImageNameResult != null) {
                try {
                    productImageNameResult.close();
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



