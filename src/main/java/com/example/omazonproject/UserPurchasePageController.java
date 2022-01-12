package com.example.omazonproject;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is responsible to control the events happening in the user purchase page
 *
 * @author XiangLun
 */
public class UserPurchasePageController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean showingCheckOutButton;

    @FXML
    private Button orders;

    @FXML
    private Button orderHistory;

    @FXML
    private Button toPay;

    @FXML
    private Label deliveredLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Line underLine;

    @FXML
    private ComboBox<String> productCategory_home;

    @FXML
    private Label unitPriceLabel;

    @FXML
    private Button profileIcon;

    @FXML
    private Button homeIcon;

    @FXML
    private VBox vBox;

    @FXML
    private Button checkOutBtn;


    @FXML
    public void initialize() {

        // Set-up category in the combo box
        productCategory_home.getItems().addAll("Electronic Devices", "Fashion", "Food", "Health & Beauty", "Sports", "TV & Home Appliances");
        productCategory_home.setPromptText("Select");

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

        // display the cart item
        displayCartItem();

        // Fade in check out button
        TranslateTransition translate = new TranslateTransition(Duration.seconds(.8), checkOutBtn);
        translate.setFromY(checkOutBtn.getLayoutY() + 10);
        translate.setToY(checkOutBtn.getLayoutY());
        translate.play();
        FadeTransition fadeInCheckOutBtn = new FadeTransition(Duration.seconds(.8), checkOutBtn);
        fadeInCheckOutBtn.setFromValue(0);
        fadeInCheckOutBtn.setToValue(1);
        fadeInCheckOutBtn.play();

        // update the boolean variable
        showingCheckOutButton = true;
    }

    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void ordersButtonPressed(ActionEvent event) {
        unitPriceLabel.setVisible(false);
        totalPriceLabel.setVisible(false);
        deliveredLabel.setVisible(true);

        // get the x-position of the cancel payment button and store the value in the currentPos variable
        Bounds boundsInScene = orders.localToScene(orders.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());

        // display orders
        displayOrderedItem();

        if (showingCheckOutButton) {
            // play fade out animation for the checkout button
            TranslateTransition translate = new TranslateTransition(Duration.seconds(.8), checkOutBtn);
            translate.setFromY(checkOutBtn.getLayoutY());
            translate.setToY(checkOutBtn.getLayoutY() + 10);
            translate.play();
            FadeTransition fadeOutCheckOutBtn = new FadeTransition(Duration.seconds(.8), checkOutBtn);
            fadeOutCheckOutBtn.setFromValue(1);
            fadeOutCheckOutBtn.setToValue(0);
            fadeOutCheckOutBtn.play();

            // disable the button
            checkOutBtn.setDisable(true);

            // update the boolean variable
            showingCheckOutButton = false;
        }
    }

    @FXML
    void orderHistoryButtonPressed(ActionEvent event) {
        unitPriceLabel.setVisible(true);
        totalPriceLabel.setVisible(true);
        deliveredLabel.setVisible(false);

        // get the x-position of the order history button and store the value in the currentPos variable
        Bounds boundsInScene = orderHistory.localToScene(orderHistory.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());

        // display order history
        displayOrderHistory();

        if (showingCheckOutButton) {
            // play fade out animation for the checkout button
            TranslateTransition translate = new TranslateTransition(Duration.seconds(.8), checkOutBtn);
            translate.setFromY(checkOutBtn.getLayoutY());
            translate.setToY(checkOutBtn.getLayoutY() + 10);
            translate.play();
            FadeTransition fadeOutCheckOutBtn = new FadeTransition(Duration.seconds(.8), checkOutBtn);
            fadeOutCheckOutBtn.setFromValue(1);
            fadeOutCheckOutBtn.setToValue(0);
            fadeOutCheckOutBtn.play();

            // disable the button
            checkOutBtn.setDisable(true);

            // update the boolean variable
            showingCheckOutButton = false;
        }
    }

    @FXML
    void toPayButtonPressed(ActionEvent event) {
        unitPriceLabel.setVisible(true);
        totalPriceLabel.setVisible(true);
        deliveredLabel.setVisible(false);

        // get the x-position of the to pay button and store the value in the currentPos variable
        Bounds boundsInScene = toPay.localToScene(toPay.getBoundsInLocal());
        double currentPos = boundsInScene.getCenterX();

        // play the animation
        playAnimation(currentPos, getLinePos());

        // display the cart contents
        displayCartItem();

        if (!showingCheckOutButton) {
            // Fade in check out button
            TranslateTransition translate = new TranslateTransition(Duration.seconds(.8), checkOutBtn);
            translate.setFromY(checkOutBtn.getLayoutY() + 10);
            translate.setToY(checkOutBtn.getLayoutY());
            translate.play();
            FadeTransition fadeInCheckOutBtn = new FadeTransition(Duration.seconds(.8), checkOutBtn);
            fadeInCheckOutBtn.setFromValue(0);
            fadeInCheckOutBtn.setToValue(1);
            fadeInCheckOutBtn.play();

            // enable back the button
            checkOutBtn.setDisable(false);

            // update the boolean variable
            showingCheckOutButton = true;
        }
    }

    @FXML
    void checkOutButtonPressed() {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();

        if (!vBox.getChildren().isEmpty()) {
            // if the vBox is not empty,
            // request for payment password to proceed using a custom dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Checking Out Cart Items...");
            dialog.setHeaderText(String.format("The total amount of payment is RM%.2f\nPlease key-in your " +
                    "payment password to proceed with the payment.", jsonFileUtil.getTotalAmountFromCart()));
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            PasswordField passwordField = new PasswordField();

            GridPane grid = new GridPane();
            grid.setVgap(10);
            grid.setHgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));
            grid.add(new Label("Enter payment password: "), 0, 0);
            grid.add(passwordField, 1, 0);
            dialog.getDialogPane().setContent(grid);

            Platform.runLater(passwordField::requestFocus);

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent() && passwordField.getText().equals(User.getPaymentPassword())) {
                // if the password entered is correct,
                // read the cart file and write its contents to orders file
                List<CartItem> cartItemList = new ArrayList<>();
                if (new File("JsonFiles\\cart.json").exists()) {
                    JSONParser jsonParser = new JSONParser();
                    try {
                        Object obj = jsonParser.parse(new FileReader("JsonFiles\\cart.json"));
                        JSONArray jsonArray = (JSONArray) obj;

                        // Iterate over jsonArray to load all the cart item in it
                        for (Object object : jsonArray) {
                            if (object instanceof JSONObject) {
                                CartItem cartItem = new CartItem();
                                cartItem.setSellerEmail((String) ((JSONObject) object).get("sellerEmail"));
                                cartItem.setProductName((String) ((JSONObject) object).get("productName"));
                                cartItem.setQuantity(Math.toIntExact((Long) ((JSONObject) object).get("quantity")));
                                cartItem.setPricePerUnit((Double) ((JSONObject) object).get("pricePerUnit"));
                                cartItem.setCartImagePath((String) ((JSONObject) object).get("cartImagePath"));
                                cartItemList.add(cartItem);
                            }
                        }

                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                JsonFileUtil jsonUtil = new JsonFileUtil();
                jsonUtil.writeOrdersFile(cartItemList);

                for (CartItem cartItem : cartItemList) {
                    // for each cart item, connect to database to increase the product's sales and decrease the stock
                    Connection connection = null;
                    PreparedStatement psUpdate = null;
                    PreparedStatement psGetData = null;
                    ResultSet resultSet = null;
                    int numberOfStocks = 0, numberOfSales = 0;

                    try {
                        DatabaseConnection db = new DatabaseConnection();
                        connection = db.getConnection();

                        psGetData = connection.prepareStatement("SELECT * FROM product_info WHERE imageName = ?");
                        psGetData.setString(1, cartItem.getCartImagePath());
                        resultSet = psGetData.executeQuery();

                        if (resultSet.next()) {
                            numberOfStocks = Integer.parseInt(resultSet.getString("numOfStock"));
                            numberOfSales = Integer.parseInt(resultSet.getString("numberOfSales"));
                        }

                        psUpdate = connection.prepareStatement("UPDATE product_info SET numberOfSales = ?, numOfStock = ? WHERE sellerEmail = ? AND name = ?");
                        psUpdate.setString(1, String.valueOf(numberOfSales + cartItem.getQuantity()));
                        psUpdate.setString(2, String.valueOf(numberOfStocks - cartItem.getQuantity()));
                        psUpdate.setString(3, cartItem.getSellerEmail());
                        psUpdate.setString(4, cartItem.getProductName());
                        psUpdate.executeUpdate();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (psGetData != null) {
                            try {
                                psGetData.close();
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (psUpdate != null) {
                            try {
                                psUpdate.close();
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            try {
                                connection.close();
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


                // send notification to the seller for all the cart items
                for (CartItem cartItem : cartItemList) {
                    try {
                        Email.sendNotification(cartItem.getSellerEmail(), cartItem.getProductName(), cartItem.getQuantity(), cartItem.getPricePerUnit());

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

                // remove all the record in the json file
                JSONArray jsonArray = new JSONArray();
                try {
                    FileWriter file = new FileWriter("JsonFiles\\cart.json");
                    file.write(jsonArray.toJSONString());
                    file.flush();
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // inform the user that the payment is successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment successful");
                alert.setHeaderText(null);
                alert.setContentText("The payment is successful. You can view your orders at Profile > My Purchase > Orders.");
                alert.showAndWait();

                // clear the vBox
                vBox.getChildren().clear();

            } else {
                // if the password is incorrect, warn the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect Password");
                alert.setHeaderText(null);
                alert.setContentText("The password entered is incorrect. Please try again.");
                alert.showAndWait();

            }

        }

    }

    /**
     * This method is used to get the current x-position of the line
     *
     * @return current x-position of the line
     * @author XiangLun
     */
    public double getLinePos() {
        Bounds bounds = underLine.localToScene(underLine.getBoundsInLocal());
        return bounds.getCenterX();
    }

    /**
     * This method plays the animation of the underline
     *
     * @param currentPos  x-position where the line wants to go to
     * @param previousPos x-position where the line is currently at
     * @author XiangLun
     */
    public void playAnimation(double currentPos, double previousPos) {
        TranslateTransition translate = new TranslateTransition();
        translate.setNode(underLine);
        translate.setByX(currentPos - previousPos);
        translate.play();
    }

    /**
     * This method is used to display the user's cart item
     *
     * @author XiangLun
     */
    private void displayCartItem() {
        // clear the previous contents in the vbox
        vBox.getChildren().clear();

        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the get cart item list method
        List<CartItem> cartItemList = new ArrayList<>(jsonFileUtil.readCartFile());

        // loop through the cartItemList, fills in the cart item information and add it to the vbox
        for (CartItem cartItem : cartItemList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cart-item-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CartItemController cartItemController = fxmlLoader.getController();
                cartItemController.setData(cartItem);
                vBox.getChildren().add(anchorPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to display the user's ordered item
     *
     * @author XiangLun
     */
    public void displayOrderedItem() {
        // clear the previous contents in the vbox
        vBox.getChildren().clear();

        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the get ordered item list method
        List<OrderedItem> orderedItemList = new ArrayList<>(jsonFileUtil.readOrdersFile());

        // loop through the orderedItemList, fills in the ordered item information and add it to the vbox
        for (OrderedItem orderedItem : orderedItemList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ordered-item-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                OrderedItemController orderedItemController = fxmlLoader.getController();
                orderedItemController.setData(orderedItem);
                vBox.getChildren().add(anchorPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to display the user's order history
     *
     * @author XiangLun
     */
    private void displayOrderHistory() {
        // clear the previous contents in the vbox
        vBox.getChildren().clear();

        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the get order history list method
        List<OrderHistoryItem> orderHistoryList = new ArrayList<>(jsonFileUtil.readOrderHistoryFile());

        // loop through the orderHistoryList, fills in the ordered item information and add it to the vbox
        for (OrderHistoryItem orderHistory : orderHistoryList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cart-item-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CartItemController cartItemController = fxmlLoader.getController();
                cartItemController.setData(orderHistory);
                vBox.getChildren().add(anchorPane);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
