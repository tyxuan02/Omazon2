package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This part is responsible to control the events happening at the seller-login-page and seller-register-page
 */

public class SellerController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    // Seller username entered by user at the seller sing-up page
    private TextField sellerName_Signup;

    @FXML
    // Seller email address entered by user at the seller sign-up page
    private TextField sellerEmail_SignUp;

    @FXML
    // Shop address entered by user at the seller sign-up page
    private TextField shopAddress_SignUp;

    @FXML
    // Seller password entered by user at the seller sign-up page
    private PasswordField sellerPassword_SignUp;

    @FXML
    // Seller confirm password entered by user at the seller sing-up page
    private PasswordField sellerConfirmPassword_SignUp;

    @FXML
    // The "Password does not match or is empty" label
    private Label sellerSignUpPageNotMatchLabel;

    @FXML
    // Seller email address entered by user at the seller login page
    private TextField sellerEmail_Login;

    @FXML
    // Seller password entered by user at the seller login page
    private TextField sellerPassword_Login;

    @FXML
    // The "Please enter email address and password." label
    private Label sellerLoginMessageLabel;

    @FXML
    // Sign-up button pressed at the seller sign-up page
    // This method will check all the information entered by the user while the user is signing up at the seller sign-up page
    public void sellerSignUpButtonPressed(MouseEvent event) throws SQLException, MessagingException {

        // hide the "Password does not match or is empty" label
        sellerSignUpPageNotMatchLabel.setVisible(false);

        // Determine whether the username is empty,
        if (!sellerName_Signup.getText().isBlank()) {
            // If seller name entered is not empty,

            // Validate the email address entered by the user
            if (valEmail(sellerEmail_SignUp.getText())) {
                // If valid email address is entered,
                // Determine whether the confirmation password is equal to password
                if (!shopAddress_SignUp.getText().isBlank()) {
                    // If shop address entered is not empty,
                    if (sellerPassword_SignUp.getText().equals(sellerConfirmPassword_SignUp.getText()) && ((!sellerPassword_SignUp.getText().isBlank())) && ((!sellerConfirmPassword_SignUp.getText().isBlank()))) {
                        // If password and confirmation password matches,
                        // Check whether the email entered is in use
                        Connection connectDB = null;
                        Statement statement = null;
                        ResultSet sellerEmailResult = null;

                        try {
                            DatabaseConnection connectNow = new DatabaseConnection();
                            connectDB = connectNow.getConnection();
                            statement = connectDB.createStatement();
                            String sellerEmailEntered = sellerEmail_SignUp.getText();
                            sellerEmailResult = statement.executeQuery("SELECT email FROM seller_account WHERE email= '" + sellerEmailEntered + "'");
                            if (sellerEmailResult.next()) {
                                // If the email entered is in use,
                                // Display a warning pop-up message
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Invalid email address");
                                alert.setHeaderText("The email address entered is in use.");
                                alert.setContentText("Please re-enter a valid email address.");
                                alert.showAndWait();

                            } else {
                                // If the email entered is not in use,
                                // Send verification email
                                VerificationEmail verificationEmail = new VerificationEmail();
                                verificationEmail.sendVerificationEmail(sellerEmail_SignUp.getText(), "user");

                                // Create a dialog box and check the code entered
                                TextInputDialog textInputDialog = new TextInputDialog();
                                textInputDialog.setTitle("Verification email sent successfully");
                                textInputDialog.setHeaderText("Please enter the verification code to verify your email address");
                                textInputDialog.setContentText("Verification code:");

                                Optional<String> code = textInputDialog.showAndWait();
                                if (code.isPresent() && code.get().equals(Integer.toString(verificationEmail.verificationCode))) {
                                    // If the code entered matches,
                                    // Register the seller
                                    registerSeller();

                                    // Display sign-up successful pop-up message
                                    Alert alert = new Alert(Alert.AlertType.NONE);
                                    alert.setGraphic(new ImageView(Objects.requireNonNull(this.getClass().getResource("GreenTick.gif")).toString()));
                                    alert.setTitle("Success");
                                    alert.setHeaderText("Your seller account has been created.");
                                    alert.setContentText("Thank you for signing up at Omazon :D");
                                    alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
                                    alert.showAndWait();

                                } else {
                                    // If the code entered does not match,
                                    // Display error pop-up message
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("Error");
                                    alert.setHeaderText("The verification code entered does not match.");
                                    alert.setContentText("Please try again.");
                                    alert.showAndWait();
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();

                        } finally {
                            if (sellerEmailResult != null) {
                                try {
                                    sellerEmailResult.close();
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

                    } else {
                        // If password and confirmation password does not match or is empty,
                        // Show the "password does not match or is empty" label
                        sellerSignUpPageNotMatchLabel.setVisible(true);
                    }

                } else {
                    // If shop address entered is empty,
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid shop address");
                    alert.setHeaderText("The shop address is empty.");
                    alert.setContentText("Please enter a shop address.");
                    alert.showAndWait();
                }

            } else {
                // If invalid email address is entered,
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid email address");
                alert.setHeaderText("The email address entered either is invalid or empty.");
                alert.setContentText("Please re-enter a valid email address.");
                alert.showAndWait();
            }

        } else {
            // If seller name entered is empty,
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid seller name");
            alert.setHeaderText("The seller name entered is empty.");
            alert.setContentText("Please enter a seller name.");
            alert.showAndWait();
        }
    }


    /**
     * This method was made to validate the email entered by user(seller) in the seller sign-up page
     *
     * @param input the email entered by the user(seller)
     * @return a boolean value indicating the validity of the email
     */
    public static boolean valEmail(String input) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }


    // Register sellers and store their credentials into our database.
    public void registerSeller() {
        Connection connectDB = null;
        Statement statement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            String name = sellerName_Signup.getText();
            String email = sellerEmail_SignUp.getText();
            String password = sellerPassword_SignUp.getText();
            String address = shopAddress_SignUp.getText();
            String insertFields = "INSERT INTO seller_account (sellerName, email, address, password) VALUES ('";
            String insertValues = name + "','" + email + "','" + address + "','" + password + "')";
            String insertToRegister = insertFields + insertValues;
            statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);

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
    }

    @FXML
    // Prompt sign-up button pressed at the seller login page
    public void sellerSignUpPromptButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the seller sign-up page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-register-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    // Quit button at the seller sign-up page pressed
    public void sellerRegistrationPageQuitButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the seller login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
        // Quit button pressed at seller login page
    void sellerLoginPageQuitButtonPressed(MouseEvent event) throws IOException {
        // Forward user to product page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Login button pressed at seller login page
    public void sellerLoginButtonPressed(MouseEvent event) throws IOException {
        // Hide the "Please enter email and password" label
        sellerLoginMessageLabel.setVisible(false);

        if (!sellerEmail_Login.getText().isBlank() && !sellerPassword_Login.getText().isBlank()) {
            //  if email and password entered is not empty,
            // Check the credentials entered by the user
            if (sellerValidateLogin()) {
                // if valid,
                // Forward user to the seller centre if the credentials matches
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } else {
            sellerLoginMessageLabel.setVisible(true);
        }
    }

    //Validate the credentials entered by the user(seller) with our database and log them into seller page if it matches
    //@return a boolean value indicating the validity of the credentials entered
    public boolean sellerValidateLogin() {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet queryResult = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();
            String sellerEmail = sellerEmail_Login.getText();
            String sellerPassword = sellerPassword_Login.getText();
            queryResult = statement.executeQuery("SELECT * FROM seller_account WHERE email = '" + sellerEmail + "' AND password ='" + sellerPassword + "'");
            // if the query result is not empty
            if (queryResult.next()) {
                String retrievedSellerEmail = queryResult.getString("email");
                String retrievedSellerPassword = queryResult.getString("password");
                if (retrievedSellerEmail.equals(sellerEmail) && retrievedSellerPassword.equals(sellerPassword)) {
                    // if the credentials matches
                    // display login successful pop-up message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Welcome to Omazon!");
                    alert.showAndWait();
                    return true;
                } else {
                    // if the credentials does not match
                    // display error pop-up message
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid credentials. Please re-enter a valid credentials.");
                    alert.showAndWait();
                    return false;
                }
            } else {
                // if the query result is empty
                // display error pop-up message
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid credentials. Please re-enter a valid credentials.");
                alert.showAndWait();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (queryResult != null) {
                try {
                    queryResult.close();

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
        return false;
    }

    /**
     * This part is responsible to control the events happening at the seller centre
     */

    @FXML
    // The seller name menu button(manage profile and logout) at seller centre
    private MenuButton sellerNameMenuButton;

    @FXML
    // The menu button(customer review, delivery status and orders) at seller centre
    private MenuButton menuButton;

    @FXML
    // First product description entered by seller at seller centre
    private TextArea description1;

    @FXML
    // Second product description entered by seller at seller centre
    private TextArea description2;

    @FXML
    // Third product description entered by seller at seller centre
    private TextArea description3;

    @FXML
    // Fourth product description entered by seller at seller centre
    private TextArea description4;

    @FXML
    // Manage profile selection at sellerNameMenuButton at seller centre
    public void manageProfilePressed(ActionEvent event) throws IOException {
        // Forward user to the seller profile page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-profile-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Logout selection at sellerNameMenuButton at seller centre
    public void logoutPressed(ActionEvent event) throws IOException {
        // Forward user to seller login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) sellerNameMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Add product button at seller centre
    public void addProductButtonPressed(ActionEvent event) throws IOException {
        // Forward user to seller add product page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-add-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Remove product button at seller centre
    public void removeProductButtonPressed(ActionEvent event) {
    }

    @FXML
    // Customer review selection at menuButton at seller centre
    public void customerReviewPressed(ActionEvent event) {
    }

    @FXML
    // Delivery status selection at menuButton at seller centre
    public void deliveryStatusPressed(ActionEvent event) {
    }

    @FXML
    // Orders selection at menuButton at seller centre
    public void ordersPressed(ActionEvent event) {
    }

    @FXML
    // First update button at seller centre
    public void update1ButtonPressed(MouseEvent event) {
    }

    @FXML
    // Second update button at seller centre
    public void update2ButtonPressed(MouseEvent event) {
    }

    @FXML
    // Third update button at seller centre
    public void update3ButtonPressed(MouseEvent event) {
    }

    @FXML
    // Fourth update button at seller centre
    public void update4ButtonPressed(MouseEvent event) {
    }

    /**
     * This class is responsible to control the events happening at the seller-add-product-page
     */

    @FXML
    public void addProductPageQuitButtonPressed(MouseEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}