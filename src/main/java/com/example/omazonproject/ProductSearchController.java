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

/**
 * This controller is responsible to control the events happening in the Product Search Page
 */
public class ProductSearchController{

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
     * GridPane is a layout component which lays out its child components in a grid
     */
    @FXML
    public GridPane grid;

    /**
     * A label to display product name that user searches
     */
    @FXML
    public Label productToSearch;

    /**
     * An array list to store all product names that user searches and display it at product search page
     */
    public java.util.List<Product> products = new ArrayList<>();

    /**
     * An array list to store all product information that got from database
     */
    public java.util.List<Product> objectsForProduct = new ArrayList<>();

    /**
     * An array list to store all product names of a seller that user searches and display it at product search page
     */
    public java.util.List<Product> sellerProducts = new ArrayList<>();

    /**
     * An array list to store all product information of a seller that got from database
     */
    public java.util.List<Product> objectsForSellerProduct = new ArrayList<>();

    /**
     * An object of ProductListener
     */
    public ProductListener productListener;

    /**
     * This method is used to display product name and products that user searches after switching scene
     * This method will direct user to product page if seller click the product
     * @param productToSearchName   product name that user searches
     */
    @FXML
    public void initialize(String productToSearchName) {

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

    /**
     * A home button at product search page
     * This method will direct user to user homepage after clicking it
     * @param event
     * @throws IOException
     */
    @FXML
    public void HomeButtonPressed(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A user profile button
     * This method will direct user to user profile page after clicking it
     * @param event
     * @throws IOException
     */
    @FXML
    public void UserProfileButtonPressed(MouseEvent event) throws IOException {
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

    /**
     * This method is used to change product search page to product page
     * This method will be called if user click the product at product search page
     * @param product   product at product search page
     * @throws IOException
     */
    public void changeScene(Product product) throws IOException {
        // Forward user to product page based on product information
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product-page.fxml"));

        ProductController productController = new ProductController();
        stage = (Stage) ((Node) productController.event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(loader.load()));
        AutoFillProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(product);
    }

    /**
     * This method is used to retrieve product information from database based on product name entered by user
     * @return an objects of Product
     */
    public List<Product> getProduct () {

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

    /**
     * This method is used to retrieve product information from database based on seller name entered by user
     * @return an object of Product
     */
    public List <Product> getSellerProduct () {

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
