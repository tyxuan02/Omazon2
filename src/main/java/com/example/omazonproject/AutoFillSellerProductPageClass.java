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

public class AutoFillSellerProductPageClass {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Product currentProduct;

    DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    private Label category;

    @FXML
    private Label country;

    @FXML
    private TextArea productDescription;

    @FXML
    private TextField productPrice;

    @FXML
    private TextField stock;

    @FXML
    private Button homeIcon;

    @FXML
    private Label name;

    @FXML
    private ImageView productImage;

    @FXML
    private Button profileIcon;

    @FXML
    private Label shipFrom;

    @FXML
    private VBox vBox;

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

    @FXML
    public void sellerHomeButtonPressed(ActionEvent event) throws IOException {
        // forward the user to seller centre
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void sellerProfileButtonPressed(ActionEvent event) throws IOException {
        // forward the user to the seller profile page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void EditImage(ActionEvent event) {
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

    @FXML
    void deleteProduct(ActionEvent event) throws IOException {

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

    @FXML
    void saveButtonPressed(ActionEvent event) {

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
     * This method is used to display the product review
     *
     * @author YuXuan
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
