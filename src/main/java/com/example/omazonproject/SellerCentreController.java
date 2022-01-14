package com.example.omazonproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This controller is responsible to control the events happening in the Seller Homepage
 */
public class SellerCentreController {

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
     * A menu button(manage profile and logout)
     */
    @FXML
    private MenuButton sellerNameMenuButton;

    /**
     * GridPane is a layout component which lays out its child components in a grid
     */
    @FXML
    private GridPane grid;

    /**
     * An array list to store product objects
     * This array list is used to store all products of a seller
     */
    private List<Product> products = new ArrayList<>();

    /**
     * A child class of ProductListener class
     */
    private ProductListener productListener;

    /**
     * This method is used to get all products of a seller and store all the product objects in products array list
     */
    private void getProduct() {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet Result = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();

            Result = statement.executeQuery("SELECT * FROM product_info WHERE sellerEmail = '" + Seller.getEmail() + "'");

            int count = 0;
            while (Result.next()) {
                // add all product information that belongs to the seller into a products list
                products.add(count, new Product());
                products.get(count).setSellerName(Result.getString("sellerName"));
                products.get(count).setSellerEmail(Result.getString("sellerEmail"));
                products.get(count).setProductName(Result.getString("name"));
                products.get(count).setProductPrice(Result.getDouble("price"));
                products.get(count).setCategory(Result.getString("category"));
                products.get(count).setDescription(Result.getString("description"));
                products.get(count).setNumOfOneStars(Result.getInt("numOfOneStars"));
                products.get(count).setNumOfTwoStars(Result.getInt("numOfTwoStars"));
                products.get(count).setNumOfThreeStars(Result.getInt("numOfThreeStars"));
                products.get(count).setNumOfFourStars(Result.getInt("numOfFourStars"));
                products.get(count).setNumOfFiveStars(Result.getInt("numOfFiveStars"));
                products.get(count).setNumberOfSales((Result.getInt("numberOfSales")));
                products.get(count).setProductImagePath(Result.getString("imageName"));
                products.get(count).setProductStock(Result.getInt("numOfStock"));
                products.get(count).setAddress(Result.getString("sellerAddress"));
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
    }

    /**
     * This method will automatically display seller name at seller name menu button after switching scene
     * This method will call getProduct method to get all products of a seller and display it at seller home page after switching scene
     * This method will direct seller to seller edit product page if seller click the product
     */
    @FXML
    void initialize() {

        // fill-in seller name in the menu button
        sellerNameMenuButton.setText("  " + Seller.getSellerName());

        // call the method to add all product information that belongs to the seller into a products list
        getProduct();

        productListener = new ProductListener() {
            @Override
            public void onClickListener(Product product) throws IOException {
                changeScene(product);
            }
        };

        int column = 0;
        int row = 1;

        try {

            for (Product product : products) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("product-template.fxml"));
                AnchorPane anchorPane = loader.load();

                ProductController productController = loader.getController();
                // set product name, product price and product image
                // display it at seller product page
                productController.setData(product, productListener);


                if (column == 4) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);

                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(28));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manage profile button at seller homepage
     * This method will direct seller to seller profile page after clicking it
     *
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void manageProfilePressed() throws IOException {
        // create an instance of the FXMLLoader class
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-profile-page.fxml"));
        root = fxmlLoader.load();

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // display the scene
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Logout button at seller homepage
     * This method will direct seller to seller login page after clicking it
     *
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void logoutPressed() throws IOException {
        // Forward user to seller login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Add product button at seller homepage
     * A window will pop up after seller clicking it
     * This window is used to let user add product
     *
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void addProductButtonPressed() throws IOException {
        // Forward user to seller add product page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-add-product-page.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Please add your product");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        resetScene();
    }

    /**
     * This method will forward the user to the customer review page
     *
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void customerReviewPressed() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-review-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method will show a pop-up window which presents the seller's overall performance
     *
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void yourPerformancePressed() throws IOException {
        // show the seller's performance
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seller-performance-template.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Your Performance");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * This method will direct seller to seller edit page
     * Seller can edit product information at seller edit page
     *
     * @param product products of a seller
     * @throws IOException when the resource file is not found
     */
    private void changeScene(Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("seller-edit-page.fxml"));
        ProductController productController = new ProductController();

        stage = (Stage) ((Node) productController.event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        AutoFillSellerProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(product);
    }

    /**
     * This method will refresh seller homepage after seller add a new product
     * A new product will be displayed at seller homepage
     */
    private void resetScene() {
        // clear the contents in the grid
        grid.getChildren().clear();
        products.clear();

        getProduct();
        productListener = new ProductListener() {
            @Override
            public void onClickListener(Product product) throws IOException {
                changeScene(product);
            }
        };

        int column = 0;
        int row = 1;

        try {

            for (Product product : products) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("product-template.fxml"));
                AnchorPane anchorPane = loader.load();

                ProductController productController = loader.getController();
                // set product name, product price and product image
                // display it at seller product page
                productController.setData(product, productListener);


                if (column == 4) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);

                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(28));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

