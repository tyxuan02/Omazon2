package com.example.omazonproject;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class acts as a controller for the seller product page
 */
public class AutoFillSellerProductPageClass {

    /**
     * Stage is used to represent a window in a JavaFX desktop application
     */
    public Stage stage;

    /**
     * Scene is the container for all content in a scene graph
     */
    public Scene scene;

    /**
     * Root provides a solution to the issue of defining a reusable component with FXML
     */
    public Parent root;

    /**
     * Object of Product class
     */
    public Product currentProduct;

    /**
     * DecimalFormat is used to format numbers using a formatting pattern you specify yourself
     */
    DecimalFormat df = new DecimalFormat("0.00");

    /**
     * A label to display product category
     */
    @FXML
    public Label category;

    /**
     * A label to display country
     */
    @FXML
    public Label country;

    /**
     * A text area to fill product description in it
     */
    @FXML
    public TextArea productDescription;

    /**
     * A text field to fill product price in it
     */
    @FXML
    public TextField productPrice;

    /**
     * A text field to fill product stock in it
     */
    @FXML
    public TextField stock;

    /**
     * To display home icon
     */
    @FXML
    public Button homeIcon;

    /**
     * To display product name
     */
    @FXML
    public Label name;

    /**
     * An image view to display product image
     */
    @FXML
    public ImageView productImage;

    /**
     * To display profile icon
     */
    @FXML
    public Button profileIcon;

    /**
     * A label to display where product will be shipped
     */
    @FXML
    public Label shipFrom;

    /**
     * A layout component which positions all its child nodes (components) in a vertical column
     */
    @FXML
    public VBox vBox;

    /**
     * This method is used to fill product information automatically into seller's-product-page.fxml
     * @param product
     */
    @FXML
    public void autoFill(Product product) {

        //Save the object of the current product
        this.currentProduct = product;

        //Autofill the product information into product page
        productPrice.setText(String.valueOf(df.format(product.getProductPrice())));
        category.setText(product.getCategory());
        name.setText(product.getProductName());
        country.setText("Malaysia");
        stock.setText(String.valueOf(product.getNumOfStock()));
        shipFrom.setText(product.getAddress());
        productDescription.setText(product.getDescription());

        Image image = new Image(new File("src/main/resources/images/" + product.getProductImagePath() + ".png").toURI().toString());
        productImage.setImage(image);
        displayProductReview(product.getProductImagePath());

    }

    /**
     * Direct seller to seller homepage
     * @param event
     * @throws IOException
     */
    @FXML
    public void sellerHomeButtonPressed(ActionEvent event) throws IOException {
        // forward the user to seller centre
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Direct seller to seller profile page
     * @param event
     * @throws IOException
     */
    @FXML
    public void sellerProfileButtonPressed(ActionEvent event) throws IOException {
        // forward the user to the seller profile page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is used to let user upload a new product image after clicking it
     * @param event
     */
    @FXML
    public void EditImage(ActionEvent event) {
        try {
            //Update product image
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload product image");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            Image image = new Image(selectedFile.toURI().toString(), 1024, 720, false, false);
            productImage.setImage(image);

            //Store product image in images folder
            File fileoutput = new File("src/main/resources/images/" + currentProduct.getProductImagePath() + ".png");
            BufferedImage BI = SwingFXUtils.fromFXImage(productImage.getImage(), null);
            ImageIO.write(BI, "png", fileoutput);

        } catch (NullPointerException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to delete seller product after clicking it
     * Product information for this product will be removed in database
     * @param event
     * @throws IOException
     */
    @FXML
    public void deleteProduct(ActionEvent event) throws IOException {

        Connection connectDB = null;
        Statement statement = null;

        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String deleteProduct = "DELETE FROM product_info WHERE imageName = '" + currentProduct.getProductImagePath() + "'";

            statement = connectDB.createStatement();
            statement.executeUpdate(deleteProduct);


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

        File fileoutput = new File("src/main/resources/images/" + currentProduct.getProductImagePath() + ".png");
        fileoutput.delete();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product removed successfully");
        alert.setHeaderText(null);
        alert.setContentText("Your product has been removed");
        alert.showAndWait();

        // forward the user to the seller product page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * This method will save changes after clicking it
     * Product information for this product will change or update in database
     * @param event
     */
    @FXML
    public void saveButtonPressed(ActionEvent event) {

        Connection connectDB = null;
        Statement statement = null;

        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String price = productPrice.getText();
            String numStock = stock.getText();
            String description = productDescription.getText();
            String email = currentProduct.getSellerEmail();

            String updatePrice = "UPDATE product_info set price = '" + price + "'" + " WHERE sellerEmail = '" + email + "' and imageName = '" + currentProduct.getProductImagePath() + "'";
            String updateStock = "UPDATE product_info set numOfStock = '" + numStock + "'" + " WHERE sellerEmail = '" + email + "' and imageName = '" + currentProduct.getProductImagePath() + "'";
            String updateDescription = "UPDATE product_info set description = '" + description + "'" + " WHERE sellerEmail = '" + email + "' and imageName = '" + currentProduct.getProductImagePath() + "'";
            statement = connectDB.createStatement();
            statement.executeUpdate(updatePrice);
            statement.executeUpdate(updateStock);
            statement.executeUpdate(updateDescription);

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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Product information changed successfully");
        alert.setHeaderText(null);
        alert.setContentText("Product information has been changed");
        alert.showAndWait();

    }

    /**
     * This method is used to display product review
     * @param imageName     product image name
     */
    public void displayProductReview (String imageName) {
        // clear the previous contents in the vbox
        vBox.getChildren().clear();

        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the product review list method
        List<ProductReview> productReviewList = new ArrayList<>(jsonFileUtil.readProductReviewFile(imageName));
        // loop through the productReviewList, fills in the product review, username, seller reply and add it to the vbox
        for (ProductReview productReview : productReviewList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller's-reply-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                SellerReplyController sellerReplyController = fxmlLoader.getController();
                sellerReplyController.setData(productReview);
                vBox.getChildren().add(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
