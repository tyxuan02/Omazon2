package com.example.omazonproject;

import javafx.animation.FadeTransition;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Rating;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class acts as a controller for the product page
 */
public class AutoFillProductPageClass {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Product currentProduct;
    DecimalFormat df = new DecimalFormat("0.00");

    @FXML
    private Rating rating;

    @FXML
    private Label sellerName;

    @FXML
    private ImageView productImage;

    @FXML
    private Label name;

    @FXML
    private Label category;

    @FXML
    private Label country;

    @FXML
    private Label priceLabel;

    @FXML
    private Label productDescription;

    @FXML
    private Label quantity;

    @FXML
    private Label shipFrom;

    @FXML
    private Label stock;

    @FXML
    private Button profileIcon;

    @FXML
    private Button homeIcon;

    @FXML
    private ImageView favorite;

    @FXML
    private VBox vBox;

    @FXML
    private Button chatWithSellerBtn;

    private String imageName;

    @FXML
    public void autoFill(Product product) {

        //Save the object of the current product
        this.currentProduct = product;

        //Autofill the product information into product page
        sellerName.setText(product.getSellerName());
        priceLabel.setText(String.valueOf(df.format(product.getProductPrice())));
        category.setText(product.getCategory());
        name.setText(product.getProductName());
        country.setText("Malaysia");
        stock.setText(String.valueOf(product.getNumOfStock()));
        shipFrom.setText(product.getAddress());
        productDescription.setText(product.getDescription());
        rating.setRating(1.0 * ((product.getNumOfFiveStars() * 5) + (product.getNumOfFourStars() * 4) + (product.getNumOfThreeStars() * 3) + (product.getNumOfTwoStars() * 2) + (product.getNumOfOneStars()))
                / (product.getNumOfOneStars() + product.getNumOfTwoStars() + product.getNumOfThreeStars() + product.getNumOfFourStars() + product.getNumOfFiveStars()));
        Image image = new Image(new File("src/main/resources/images/" + product.getProductImagePath() + ".png").toURI().toString());
        productImage.setImage(image);
        displayProductReview(product.getProductImagePath());
    }

    @FXML
    public void initialize() {
        // prevent mouse clicking on the rating node
        rating.setMouseTransparent(true);
        rating.setFocusTraversable(false);

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

        // Fade in chat with seller button
        FadeTransition fadeInChatWithSellerBtn = new FadeTransition(Duration.seconds(1.3), chatWithSellerBtn);
        fadeInChatWithSellerBtn.setFromValue(0);
        fadeInChatWithSellerBtn.setToValue(1);
        fadeInChatWithSellerBtn.play();
    }

    @FXML
    void homeButtonPressed(MouseEvent event) throws IOException {
        // Forward user to home page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void userProfileButtonPressed(MouseEvent event) throws IOException {

        // prevent autofocus to the text field
        Platform.runLater(() -> root.requestFocus());

        // Forward user to user profile page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-profile-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void addToCartButtonPressed(ActionEvent event) {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // use button to choose quantity
        jsonFileUtil.writeCartFile(currentProduct, Integer.parseInt(quantity.getText()));

        // inform the user that the item is added to cart successfully
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful");
        alert.setHeaderText(null);
        alert.setContentText("Item added to cart successfully.");
        alert.showAndWait();

    }

    @FXML
    void decreaseQuantityButtonPressed(ActionEvent event) {
        int currentQuantity = Integer.parseInt(quantity.getText());
        if (currentQuantity > 1) {
            quantity.setText(Integer.toString(currentQuantity - 1));
        }
    }

    @FXML
    void increaseQuantityButtonPressed(ActionEvent event) {
        int currentQuantity = Integer.parseInt(quantity.getText());
        quantity.setText(Integer.toString(currentQuantity + 1));
    }

    @FXML
    void favoriteButtonPressed(ActionEvent event) {
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        jsonFileUtil.writeFavoriteFile(currentProduct);
        URL icon = this.getClass().getResource("/images/favoriteButtonPressed.png");
        Image image = new Image(String.valueOf(icon));
        favorite.setImage(image);
    }

    @FXML
    void BuyNowButtonPressed(ActionEvent event) {
        // calculate the total price in 2 decimal places
        double totalPrice = (int) (Integer.parseInt(quantity.getText()) * Double.parseDouble(priceLabel.getText()) * 100 + 0.5) / 100.0;

        // check whether the user have set their payment password before
        if (User.getPaymentPassword() == null) {
            // if the user haven't set the payment password before, ask them to set it first
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("A Payment Password is Required");
            alert.setHeaderText(null);
            alert.setContentText("Please set your payment password at the profile page.");
            alert.showAndWait();

        } else if (Integer.parseInt(quantity.getText()) > currentProduct.getNumOfStock()) {
            // if the stock is not enough
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient stock");
            alert.setHeaderText(null);
            alert.setContentText("The number of stocks for this product is insufficient. Please wait until the seller increases their stock.");
            alert.showAndWait();

        } else if (User.getBalance() < totalPrice) {
            // if the user does not have enough balance, ask them to top up
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Payment Unsuccessful");
            alert.setHeaderText(null);
            alert.setContentText("You do not have sufficient balance to proceed with this payment.");
            alert.showAndWait();

        } else {
            // if the user do have enough balance
            // request payment password
            // request for payment password to proceed using a custom dialog box
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Processing Payment...");
            dialog.setHeaderText(String.format("The total amount of payment is RM%.2f\nPlease key-in your " +
                    "payment password to proceed with the payment.", Integer.parseInt(quantity.getText()) * Double.parseDouble(priceLabel.getText())));
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
                // if payment password entered is correct
                // subtract the amount from the user's balance in the User class
                double balance = User.getBalance() - totalPrice;
                User.setBalance(balance);

                // connect to the database to update the new balance, increment the product's number of sales, and decrease the number of stock
                Connection connection = null;
                PreparedStatement psUpdate = null;
                PreparedStatement psIncrementSales = null;
                PreparedStatement psDecrementStocks = null;

                try {
                    DatabaseConnection db = new DatabaseConnection();
                    connection = db.getConnection();

                    psUpdate = connection.prepareStatement("UPDATE user_account SET balance = ? WHERE email = ?");
                    psUpdate.setString(1, String.format("%.2f", balance));
                    psUpdate.setString(2, User.getEmail());
                    psUpdate.executeUpdate();

                    psIncrementSales = connection.prepareStatement("UPDATE product_info SET numberOfSales = ? WHERE sellerEmail = ? AND name = ?");
                    psIncrementSales.setString(1, Integer.toString(currentProduct.getNumberOfSales() + (Integer.parseInt(quantity.getText()))));
                    psIncrementSales.setString(2, currentProduct.getSellerEmail());
                    psIncrementSales.setString(3, currentProduct.getProductName());
                    psIncrementSales.executeUpdate();

                    psDecrementStocks = connection.prepareStatement("UPDATE product_info SET numOfStock = ? WHERE sellerEmail = ? AND name = ?");
                    psDecrementStocks.setString(1, Integer.toString(currentProduct.getNumOfStock() - (Integer.parseInt(quantity.getText()))));
                    psDecrementStocks.setString(2, currentProduct.getSellerEmail());
                    psDecrementStocks.setString(3, currentProduct.getProductName());
                    psDecrementStocks.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally {
                    if (psDecrementStocks != null) {
                        try {
                            psDecrementStocks.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (psUpdate != null) {
                        try {
                            psUpdate.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (psIncrementSales != null) {
                        try {
                            psIncrementSales.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // add ordered item into orders.json file
                JsonFileUtil jsonFileUtil = new JsonFileUtil();
                jsonFileUtil.writeOrdersFile(currentProduct, Integer.parseInt(quantity.getText()));

                try {
                    // send notification email to the seller
                    Email.sendNotification(currentProduct.getSellerEmail(), currentProduct.getProductName(), Integer.parseInt(quantity.getText()), currentProduct.getProductPrice());

                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                // inform the user that the payment is successful
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment successful");
                alert.setHeaderText(null);
                alert.setContentText("The payment is successful. You can view your orders at Profile > My Purchase > Orders.");
                alert.showAndWait();
            } else {
                // if the payment password is incorrect
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Payment Unsuccessful");
                alert.setHeaderText(null);
                alert.setContentText("The payment password entered is incorrect.");
                alert.showAndWait();

            }
        }
    }


    @FXML
    void chatWithSellerBtnPressed(ActionEvent event) throws IOException {
        // pop up the chat with seller page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-with-seller-page.fxml"));
        Parent root = fxmlLoader.load();
        ChatWithSellerController chatWithSellerController = fxmlLoader.getController();
        chatWithSellerController.setInfo(currentProduct.getSellerName(), currentProduct.getSellerEmail(), currentProduct.getProductName());
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Chat With Seller");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * This method is used to display the product review
     *
     * @author YuXuan
     */
    public void displayProductReview(String imageName) {
        // clear the previous contents in the vbox
        vBox.getChildren().clear();

        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        // create an array list and call the product review list method
        List<ProductReview> productReviewList = new ArrayList<>(jsonFileUtil.readProductReviewFile(imageName));
        // loop through the productReviewList, fills in the product review, username, seller reply and add it to the vbox
        for (ProductReview productReview : productReviewList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("review-template.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ReviewController reviewController = fxmlLoader.getController();
                reviewController.setData(productReview);
                vBox.getChildren().add(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
