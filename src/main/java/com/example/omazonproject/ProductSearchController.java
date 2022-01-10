package com.example.omazonproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSearchController{

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private GridPane grid;

    @FXML
    private Label productToSearch;

    private java.util.List<Product> products = new ArrayList<>();

    private java.util.List<Product> objectsForProduct = new ArrayList<>();

    private java.util.List<Product> sellerProducts = new ArrayList<>();

    private java.util.List<Product> objectsForSellerProduct = new ArrayList<>();

    private ProductListener productListener;

    @FXML
    void initialize(String productToSearchName) {

        productToSearch.setText(productToSearchName);

        // Add all objects in objectsForProduct list to a products list
        products.addAll(getProduct());

        // Add all objects in objectsForSellerProduct list to sellerProducts list
        sellerProducts.addAll(getSellerProduct());

        productListener = new ProductListener() {
            @Override
            public void onClickListener(Product product) throws IOException {
                changeScene(product);
            }
        };

        int column = 0;
        int row = 1;

        try {

            if (sellerProducts.size() == 0) {
                // If seller product list size is 0 (product name is entered to search)

                for (Product product : products) {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("product-template.fxml"));
                    AnchorPane anchorPane = loader.load();

                    ProductController productController = loader.getController();
                    // Set product name, product price and product image
                    // Display it at product search page
                    productController.setData(product, productListener);


                    if (column == 4) {
                        column = 0;
                        row++;
                    }

                    grid.add(anchorPane, column++, row);

                    // Set grid width
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);

                    // Set grid height
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(anchorPane, new Insets(28));
                }


            } else if (products.size() == 0) {
                // If product list size is 0 (seller name is entered to search)

                for (Product sellerProduct : sellerProducts) {

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("product-template.fxml"));
                    AnchorPane anchorPane = loader.load();

                    ProductController productController = loader.getController();
                    // Set product name, product price and product image
                    // Display it at product search page
                    productController.setData(sellerProduct, productListener);


                    if (column == 4) {
                        column = 0;
                        row++;
                    }

                    grid.add(anchorPane, column++, row);

                    // Set grid width
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);

                    // Set grid height
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(anchorPane, new Insets(28));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void HomeButtonPressed(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void UserProfileButtonPressed(MouseEvent event) throws IOException {
        // Create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user-profile-page.fxml"));
        root = fxmlLoader.load();

        // Prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // Display the scene
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void changeScene(Product product) throws IOException {
        // Forward user to product page based on product information
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product-page.fxml"));

        ProductController productController = new ProductController();
        stage = (Stage) ((Node) productController.event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(loader.load()));
        AutoFillProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(product);
    }

    // This method is used to retrieve product information from database
    private List<Product> getProduct () {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet Result = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();

            Result = statement.executeQuery("SELECT * FROM product_info WHERE name = '" + productToSearch.getText() + "'");

            int count = 0;
            while (Result.next()) {
                // Add all product information into a list named objectsForProduct
                objectsForProduct.add(count, new Product());
                objectsForProduct.get(count).setSellerEmail(Result.getString("sellerEmail"));
                objectsForProduct.get(count).setSellerName(Result.getString("sellerName"));
                objectsForProduct.get(count).setProductName(Result.getString("name"));
                objectsForProduct.get(count).setProductPrice(Result.getDouble("price"));
                objectsForProduct.get(count).setCategory(Result.getString("category"));
                objectsForProduct.get(count).setDescription(Result.getString("description"));
                objectsForProduct.get(count).setNumOfOneStars(Result.getInt("numOfOneStars"));
                objectsForProduct.get(count).setNumOfTwoStars(Result.getInt("numOfTwoStars"));
                objectsForProduct.get(count).setNumOfThreeStars(Result.getInt("numOfThreeStars"));
                objectsForProduct.get(count).setNumOfFourStars(Result.getInt("numOfFourStars"));
                objectsForProduct.get(count).setNumOfFiveStars(Result.getInt("numOfFiveStars"));
                objectsForProduct.get(count).setNumberOfSales((Result.getInt("numberOfSales")));
                objectsForProduct.get(count).setProductImagePath(Result.getString("imageName"));
                objectsForProduct.get(count).setProductStock(Result.getInt("numOfStock"));
                objectsForProduct.get(count).setAddress(Result.getString("sellerAddress"));
                count++;
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

        return objectsForProduct;
    }

    // This method is used to search product from database based on seller name
    private List <Product> getSellerProduct () {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet Result = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();

            Result = statement.executeQuery("SELECT * FROM product_info WHERE sellerName = '" + productToSearch.getText() + "'");

            int count = 0;
            while (Result.next()) {
                // Add all product information into a list named objectsForSellerProduct
                objectsForSellerProduct.add(count, new Product());
                objectsForSellerProduct.get(count).setSellerEmail(Result.getString("sellerEmail"));
                objectsForSellerProduct.get(count).setSellerName(Result.getString("sellerName"));
                objectsForSellerProduct.get(count).setProductName(Result.getString("name"));
                objectsForSellerProduct.get(count).setProductPrice(Result.getDouble("price"));
                objectsForSellerProduct.get(count).setCategory(Result.getString("category"));
                objectsForSellerProduct.get(count).setDescription(Result.getString("description"));
                objectsForSellerProduct.get(count).setNumOfOneStars(Result.getInt("numOfOneStars"));
                objectsForSellerProduct.get(count).setNumOfTwoStars(Result.getInt("numOfTwoStars"));
                objectsForSellerProduct.get(count).setNumOfThreeStars(Result.getInt("numOfThreeStars"));
                objectsForSellerProduct.get(count).setNumOfFourStars(Result.getInt("numOfFourStars"));
                objectsForSellerProduct.get(count).setNumOfFiveStars(Result.getInt("numOfFiveStars"));
                objectsForSellerProduct.get(count).setNumberOfSales((Result.getInt("numberOfSales")));
                objectsForSellerProduct.get(count).setProductImagePath(Result.getString("imageName"));
                objectsForSellerProduct.get(count).setProductStock(Result.getInt("numOfStock"));
                objectsForSellerProduct.get(count).setAddress(Result.getString("sellerAddress"));
                count++;
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

        return objectsForSellerProduct;
    }

}
