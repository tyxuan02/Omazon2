package com.example.omazonproject;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * This controller is responsible to control the events happening in the Seller Profile page
 *
 * @author XiangLun, YuXuan
 */
public class SellerProfileController {

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
     * A label to display seller email
     */
    @FXML
    public Label sellerEmail;

    /**
     * A text field to fill seller username
     */
    @FXML
    public TextField sellerUsername;

    /**
     * A text area to fill seller shop address
     */
    @FXML
    public TextArea shopAddress;

    /**
     * A circle to display seller profile image
     */
    @FXML
    public Circle sellerProfileImage;

    /**
     * This method will fill seller information (email address, username and shop address) after switching scene
     * This method will load seller's profile image after switching scene
     */
    @FXML
    public void initialize() {
        // fill seller's information
        sellerEmail.setText(Seller.getEmail());
        sellerUsername.setText(Seller.getSellerName());
        shopAddress.setText(Seller.getAddress());

        // load the seller's profile image
        Image image;
        String sellerImageName = Seller.getSellerName() + "-" + Seller.getEmail();
        File file = new File("assets/" + sellerImageName + ".png");

        //Check if the seller profile image exists or not
        if (file.exists()) {
            //If exists, display the seller profile image
            image = new Image(new File("assets/" + sellerImageName + ".png").toURI().toString());
            sellerProfileImage.setFill(new ImagePattern(image));
        }
    }

    /**
     * This method is used to let seller change profile image
     * The image will be saved in a folder named assets by using seller name and seller email address to name the image
     *
     * @param event
     */
    @FXML
    public void editSellerImagePressed(ActionEvent event) {
        Image image;
        try {
            //Upload seller profile image
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload profile image");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("JPEG", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            image = new Image(selectedFile.toURI().toString(), 200, 200, false, false);
            sellerProfileImage.setFill(new ImagePattern(image));

            //Store seller profile image in assets folder
            //seller profile image name = "(seller_name)-(seller_email_address)"
            File fileoutput = new File("assets/" + Seller.getSellerName() + "-" + Seller.getEmail() + ".png");
            BufferedImage BI = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(BI, "png", fileoutput);

        } catch (NullPointerException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void changePasswordButtonPressed(ActionEvent event) throws MessagingException {
        // create a new custom dialog box with password field
        Dialog<ButtonType> preDialog = new Dialog<>();
        preDialog.setTitle("Change Seller Centre Login Password");
        preDialog.setHeaderText("Please enter your current seller account password before changing the password.");

        // set up the buttons
        ButtonType forgetPasswordButtonType = new ButtonType("Forget password?");
        preDialog.getDialogPane().getButtonTypes().addAll(forgetPasswordButtonType, ButtonType.OK, ButtonType.CANCEL);

        // create a grid pane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // create a password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter current password");

        // set contents of the grid pane
        gridPane.add(new Label("Current Password:"), 0, 0);
        gridPane.add(passwordField, 1, 0);
        preDialog.getDialogPane().setContent(gridPane);

        // focus on the password field by default
        Platform.runLater(passwordField::requestFocus);

        // check button pressed
        Optional<ButtonType> btnPressed = preDialog.showAndWait();

        if (btnPressed.isPresent() && btnPressed.get() == ButtonType.OK) {
            // if OK button is pressed,
            // check the password entered with the current password
            String currentPassword = passwordField.getText();
            if (!currentPassword.isEmpty() && currentPassword.equals(Seller.getPassword())) {
                // if password entered is correct
                changeSellerPassword();

            } else {
                // if the password entered does not match with the current password
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incorrect Password");
                alert.setHeaderText("The password entered does not match with the current login password.");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }

        } else if (btnPressed.isPresent() && btnPressed.get() == forgetPasswordButtonType) {
            // if forget button is pressed,
            // inform the seller that they will receive an email
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reset Password");
            alert.setHeaderText(null);
            alert.setContentText("An email containing a confirmation code will be sent to your email address.");
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> btn = alert.showAndWait();
            if (btn.isPresent() && btn.get() == ButtonType.OK) {
                // if the seller request an email,
                // send a confirmation email to the seller
                VerificationEmail verificationEmail = new VerificationEmail();
                verificationEmail.sendVerificationEmail(Seller.getEmail(), "forgetPassword");

                // request the confirmation code from the seller
                TextInputDialog textInputDialog = new TextInputDialog();
                textInputDialog.setTitle("Confirmation Email Sent Successfully");
                textInputDialog.setHeaderText("Please enter the confirmation code.");
                textInputDialog.setContentText("Confirmation code:");
                Optional<String> code = textInputDialog.showAndWait();

                if (code.isPresent() && code.get().equals(Integer.toString(verificationEmail.verificationCode))) {
                    // if the code entered is correct,
                    // let the seller change the password
                    changeSellerPassword();
                } else {
                    // If the code entered does not match,
                    // Display error pop-up message
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("The confirmation code entered is empty or does not match.");
                    alert1.setContentText("Please try again.");
                    alert1.showAndWait();
                }
            }
        }

    }

    @FXML
    public void deleteAccountButtonPressed(ActionEvent event) {
        // create a pop-up message to alert the seller
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Account");
        alert.setHeaderText(null);

        // create a text field
        TextField textField = new TextField();

        // create a grid pane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 40, 10, 10));

        // set up buttons
        ButtonType delete = new ButtonType("DELETE", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(delete, ButtonType.CANCEL);

        // add content into grid pane
        gridPane.add(new Label("Are you sure you want to delete your seller account?\nYour seller account will be permanently erased. Enter CONFIRM to proceed."), 0, 0);
        gridPane.add(textField, 0, 1);
        alert.getDialogPane().setContent(gridPane);

        // enable/disable the deleteButton depending on seller's input
        Node deleteButton = alert.getDialogPane().lookupButton(delete);
        deleteButton.setDisable(true);
        textField.textProperty().addListener((observable, oldValue, newValue) -> deleteButton.setDisable(!newValue.equals("CONFIRM")));

        // auto-focus on the text field
        Platform.runLater(textField::requestFocus);

        // show the alert box and wait for seller
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == delete) {
            // if delete button is pressed,
            // delete seller's account in our database
            Connection connection = null;
            PreparedStatement psDelete = null;

            try {
                // connect to database
                DatabaseConnection db = new DatabaseConnection();
                connection = db.getConnection();

                // set up mySQL statement and execute update
                psDelete = connection.prepareStatement("DELETE FROM seller_account WHERE email = ?");
                psDelete.setString(1, Seller.getEmail());
                psDelete.executeUpdate();

                // remove seller's info in the User class
                Seller.setAddress(null);
                Seller.setSellerName(null);
                Seller.setEmail(null);
                Seller.setPassword(null);

                // say goodbye
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Seller Account Deleted");
                alert1.setHeaderText(null);
                alert1.setContentText("Your seller account was deleted successfully. All your data was removed from our services. Thank you for using Omazon.");
                alert1.showAndWait();

                // forward seller to the login page
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
                stage.setScene(scene);
                stage.show();

            } catch (SQLException | IOException e) {
                e.printStackTrace();

            } finally {
                if (psDelete != null) {
                    try {
                        psDelete.close();
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

        }
    }

    /**
     * This method will direct seller to seller home page
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void homepageButtonPressed(ActionEvent event) throws IOException {
        // forward the user to the seller centre
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void saveButtonPressed(ActionEvent event) {
        // when the save button is pressed
        if (!sellerUsername.getText().isEmpty() && !sellerUsername.getText().equals(Seller.getSellerName())) {
            //if the username in the text field is not empty and is not the same as the username stored in the Seller class,
            Connection connection = null;
            PreparedStatement psUpdateUsername = null;

            try {
                //change the seller's username in the database
                DatabaseConnection databaseConnection = new DatabaseConnection();
                connection = databaseConnection.getConnection();
                psUpdateUsername = connection.prepareStatement("UPDATE seller_account SET sellerName = ? WHERE email = ?");
                psUpdateUsername.setString(1, sellerUsername.getText());
                psUpdateUsername.setString(2, Seller.getEmail());
                psUpdateUsername.executeUpdate();

                // change the seller's username in the Seller class
                Seller.setSellerName(sellerUsername.getText());

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (psUpdateUsername != null) {
                    try {
                        psUpdateUsername.close();

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
        }

        // check whether address is changed
        if (!shopAddress.getText().isEmpty() && !Seller.getAddress().equals(shopAddress.getText())) {
            Connection connection = null;
            PreparedStatement psUpdateAddress = null;

            try {
                // change the seller's address in the database
                DatabaseConnection databaseConnection = new DatabaseConnection();
                connection = databaseConnection.getConnection();
                psUpdateAddress = connection.prepareStatement("UPDATE seller_account SET address = ? WHERE email = ?");
                psUpdateAddress.setString(1, shopAddress.getText());
                psUpdateAddress.setString(2, Seller.getEmail());
                psUpdateAddress.executeUpdate();

                // change the seller's address in the Seller class
                Seller.setAddress(shopAddress.getText());

            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                if (psUpdateAddress != null) {
                    try {
                        psUpdateAddress.close();

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
        }

    }

    /**
     * This method create a dialog box that request the seller to enter a new password and the confirmation password.
     * If the passwords match, change the seller's account password in our database and display a successful pop-up
     * message if the password change successfully.
     *
     * @author XiangLun
     */
    public void changeSellerPassword() {
        // if the password entered matches with the current password
        // generate a dialog box to let the seller enter their desired password to change to

        // create a new custom dialog box with two inputs
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Change Seller Centre Login Password");
        dialog.setHeaderText("Please enter your new password.");

        // Set up the button.
        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        // Create a grid pane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create the required labels and password fields.
        PasswordField newPass1 = new PasswordField();
        newPass1.setPromptText("Enter new password");
        PasswordField newPass2 = new PasswordField();
        newPass2.setPromptText("Re-enter new password");
        Text warningText = new Text("The password does not match");
        warningText.setFill(Color.RED);

        // Place the labels and password fields into the grid pane
        grid.add(new Label("New password:"), 0, 0);
        grid.add(newPass1, 1, 0);
        grid.add(new Label("Confirm new password:"), 0, 1);
        grid.add(newPass2, 1, 1);
        grid.add(warningText, 0, 2);
        dialog.getDialogPane().setContent(grid);

        // Enable/Disable change button depending on whether the passwords match.
        // Show/Hide warning text depending on whether the passwords match.
        Node changeButton = dialog.getDialogPane().lookupButton(changeButtonType);
        changeButton.setDisable(true);
        warningText.setVisible(false);
        newPass2.textProperty().addListener((observable, oldValue, newValue) -> {
            warningText.setVisible(!newValue.isEmpty());
            changeButton.setDisable(true);
            if (newValue.equals(newPass1.getText()) && !newPass1.getText().isEmpty()) {
                warningText.setVisible(false);
                changeButton.setDisable(false);
            }
        });
        newPass1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(newPass2.getText()) && !newPass2.getText().isEmpty()) {
                warningText.setVisible(true);
                changeButton.setDisable(true);
            } else if (newValue.equals(newPass2.getText()) && !newPass2.getText().isEmpty()) {
                warningText.setVisible(false);
                changeButton.setDisable(false);
            }
        });

        // Request focus on the first password field by default
        Platform.runLater(newPass1::requestFocus);

        // Convert the result to a password-confirmation password pari when the button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == changeButtonType) {
                return new Pair<>(newPass1.getText(), newPass2.getText());
            }
            return null;
        });

        // Show the dialog
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // If the confirmation password matches
        result.ifPresent(newPassword -> {

            // Check whether the password entered is same as the current password
            if (!newPassword.getKey().equals(Seller.getPassword())) {
                // If the password entered is not the same as the current password,
                // Connect to the database and change the seller's password
                Connection connection = null;
                PreparedStatement psUpdatePass = null;

                try {
                    // Change the seller's password in the database
                    DatabaseConnection db = new DatabaseConnection();
                    connection = db.getConnection();
                    psUpdatePass = connection.prepareStatement("UPDATE seller_account SET password = ? WHERE email = ?");
                    psUpdatePass.setString(1, newPassword.getKey());
                    psUpdatePass.setString(2, Seller.getEmail());
                    psUpdatePass.executeUpdate();

                    // Change the seller's password in the Seller class
                    Seller.setPassword(newPassword.getKey());

                    // Display successful pop-up message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Password change successfully.");
                    alert.showAndWait();

                } catch (SQLException e) {
                    e.printStackTrace();

                } finally {
                    if (psUpdatePass != null) {
                        try {
                            psUpdatePass.close();

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
            } else {
                // If the password entered is the same as the current password
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("The password that you wish to change to is the same as the current password.");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }
        });

    }

}




