package com.example.omazonproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.w3c.dom.events.EventException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView top1;

    @FXML
    private ImageView top2;

    @FXML
    private ImageView top3;

    public List<Product> products = new ArrayList<>();

    private ProductListener productListener;

    int[] getMaxIndex = new int[3];

    ArrayList<String> productName = new ArrayList<>();
    List<Product> objects = new ArrayList<>();

    @FXML
    public void initialize() throws Exception {

        productCategory_home.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory_home.setPromptText("select");

        //Autocomplete search
        //If the product category is not selected, view all the product in database
        getProductNameFromDatabase("select");
        getSellerNameFromDatabase();
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
                e.printStackTrace();
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


        //Create a list to store all product objects
        Connection connectDB = null;
        Statement statement = null;
        ResultSet bestSellingProductResult = null;

        int count = 0;

        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();
            bestSellingProductResult = statement.executeQuery("SELECT * FROM product_info ");

            while (bestSellingProductResult.next()) {
                objects.add(count, new Product());
                objects.get(count).setSellerName(bestSellingProductResult.getString("sellerName"));
                objects.get(count).setSellerEmail(bestSellingProductResult.getString("sellerEmail"));
                objects.get(count).setProductName(bestSellingProductResult.getString("name"));
                objects.get(count).setProductPrice(bestSellingProductResult.getDouble("price"));
                objects.get(count).setCategory(bestSellingProductResult.getString("category"));
                objects.get(count).setDescription(bestSellingProductResult.getString("description"));
                objects.get(count).setNumOfOneStars(bestSellingProductResult.getInt("numOfOneStars"));
                objects.get(count).setNumOfTwoStars(bestSellingProductResult.getInt("numOfTwoStars"));
                objects.get(count).setNumOfThreeStars(bestSellingProductResult.getInt("numOfThreeStars"));
                objects.get(count).setNumOfFourStars(bestSellingProductResult.getInt("numOfFourStars"));
                objects.get(count).setNumOfFiveStars(bestSellingProductResult.getInt("numOfFiveStars"));
                objects.get(count).setNumberOfSales((bestSellingProductResult.getInt("numberOfSales")));
                objects.get(count).setProductImagePath(bestSellingProductResult.getString("imageName"));
                objects.get(count).setProductStock(bestSellingProductResult.getInt("numOfStock"));
                objects.get(count).setAddress(bestSellingProductResult.getString("sellerAddress"));
                count = count + 1;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (bestSellingProductResult != null) {
                try {
                    bestSellingProductResult.close();
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

        //Get top 3 best-selling products by checking the number of sales for each product
        ArrayList<Integer> index = new ArrayList<>();

        int max;
        int maxIndex = 0;

        for (int i = 0; i < 3; i++) {
            max = 0;
            for (int j = 0; j < count; j++) {
                if (!index.contains(j)) {
                    if (objects.get(j).getNumberOfSales() >= max) {
                        maxIndex = j;
                        max = objects.get(j).getNumberOfSales();
                    }
                }
            }
            index.add(maxIndex);
            getMaxIndex[i] = maxIndex;
        }

        //Get the top 3 best-selling product images from images folder
        String Top1 = objects.get(getMaxIndex[0]).getProductImagePath();
        String Top2 = objects.get(getMaxIndex[1]).getProductImagePath();
        String Top3 = objects.get(getMaxIndex[2]).getProductImagePath();

        //Display the top 3 best-selling product images at home page
        Image image1 = new Image(new File("src/main/resources/images/" + Top1 + ".png").toURI().toString());
        Image image2 = new Image(new File("src/main/resources/images/" + Top2 + ".png").toURI().toString());
        Image image3 = new Image(new File("src/main/resources/images/" + Top3 + ".png").toURI().toString());
        top1.setImage(image1);
        top2.setImage(image2);
        top3.setImage(image3);


        // display all the products at user homepage
        // add all products in products list
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

            for (int i = 0; i < products.size(); i++) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("product-template.fxml"));
                AnchorPane anchorPane = loader.load();

                ProductController productController = loader.getController();
                // set product name, product price and product image
                // display it at seller product page
                productController.setData(products.get(i), productListener);


                if (column == 4) {
                    column = 0;
                    row++;
                }

                gridPane.add(anchorPane, column++, row);

                //set grid width
                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                //set grid height
                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(28));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getProduct() {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet Result = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();

            Result = statement.executeQuery("SELECT * FROM product_info");

            int count = 0;
            while (Result.next()) {
                // add all product information into products list
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

    @FXML
    void top1Pressed(ActionEvent event) throws IOException {
        //Forward user to product page based on product information
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product-page.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        AutoFillProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(objects.get(getMaxIndex[0]));
    }

    @FXML
    void top2Pressed(ActionEvent event) throws IOException {
        //Forward user to product page based on product information
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product-page.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        AutoFillProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(objects.get(getMaxIndex[1]));
    }

    @FXML
    void top3Pressed(ActionEvent event) throws IOException {
        //Forward user to product page based on product information
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product-page.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        AutoFillProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(objects.get(getMaxIndex[2]));
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

    @FXML
    void searchButtonPressed(ActionEvent event) throws IOException {

        String productToSearchName = searchItems.getText();

        if (!searchItems.getText().isBlank()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("product-search-page.fxml"));
            root = loader.load();

            ProductSearchController productSearchController = loader.getController();
            productSearchController.initialize(productToSearchName);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Enter something to search");
            alert.setHeaderText(null);
            alert.setContentText("Please enter something to search");
            alert.showAndWait();
        }


    }

    // This method is used to get product name from database and store it in an array list named productName
    // Therefore it can be used by the autocomplete search
    void getProductNameFromDatabase(String PRODUCTCATEGORY) {

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

    // This method is used to get seller name from database and store it in an array list named productName
    // Therefore it can be used by the autocomplete search
    void getSellerNameFromDatabase() {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet sellerResult = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();

            sellerResult = statement.executeQuery("SELECT * FROM seller_account");

            while (sellerResult.next()) {
                String SELLERNAME = sellerResult.getString("sellerName");
                productName.add(SELLERNAME);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sellerResult != null) {
                try {
                    sellerResult.close();
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

    private void changeScene(Product product) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product-page.fxml"));
        ProductController productController = new ProductController();

        stage = (Stage) ((Node) productController.event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        AutoFillProductPageClass controller = loader.getController();
        stage.show();
        controller.autoFill(product);
    }

}