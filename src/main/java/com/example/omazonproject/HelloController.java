package com.example.omazonproject;

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
 * This class is responsible to control the events happening in the login and sign-up page
 */
public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    // Email entered by user at the login page
    private TextField emailEntered_Login;

    @FXML
    // Password entered by user at the login page
    private PasswordField passwordEntered_Login;

    @FXML
    // Confirm password entered by user at the sign-up page
    private PasswordField confirmPassword_SignUp;

    @FXML
    // Email entered by user at the sign-up page
    private TextField emailEntered_SignUp;

    @FXML
    // Password entered by user at the sign-up page
    private PasswordField passwordEntered_SignUp;

    @FXML
    // Username entered by user at the sign-up page
    private TextField username_SignUp;

    @FXML
    // The "Please enter email and password" label
    private Label loginMessageLabel;

    @FXML
    // The "Password does not match or is empty" label
    private Label notMatchLabel;

    @FXML
    // Sign-up button pressed at the sign-up page
    // This method will check all the information entered by the user while the user is signing up
    public void signUpButtonPressed(MouseEvent event) throws MessagingException {

        // hide the "Password does not match or is empty" label
        notMatchLabel.setVisible(false);

        // Determine whether the username is empty,
        if (!username_SignUp.getText().isBlank()) {
            // If username entered is not empty,
            // Validate the email address entered by the user
            if (valEmail(emailEntered_SignUp.getText())) {
                // If valid email address is entered,
                // Determine whether the confirmation password is equal to password
                if (passwordEntered_SignUp.getText().equals(confirmPassword_SignUp.getText()) && ((!passwordEntered_SignUp.getText().isBlank())) && ((!confirmPassword_SignUp.getText().isBlank()))) {
                    // If password and confirmation password matches,
                    // Check whether the email entered is in use
                    Connection connectDB = null;
                    Statement statement = null;
                    ResultSet emailResult = null;

                    try {
                        DatabaseConnection connectNow = new DatabaseConnection();
                        connectDB = connectNow.getConnection();
                        statement = connectDB.createStatement();
                        String emailEntered = emailEntered_SignUp.getText();
                        emailResult = statement.executeQuery("SELECT email FROM user_account WHERE email= '" + emailEntered + "'");
                        if (emailResult.next()) {
                            // If the email entered is in use,
                            // Display a warning pop-up message
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Invalid Email Address");
                            alert.setHeaderText("The email address entered is in use.");
                            alert.setContentText("Please re-enter a valid email address.");
                            alert.showAndWait();

                        } else {
                            // If the email entered is not in use,
                            // Send verification email
                            VerificationEmail verificationEmail = new VerificationEmail();
                            verificationEmail.sendVerificationEmail(emailEntered_SignUp.getText(), "user");

                            // Create a dialog box and check the code entered
                            TextInputDialog textInputDialog = new TextInputDialog();
                            textInputDialog.setTitle("Verification Email Sent Successfully");
                            textInputDialog.setHeaderText("Please enter the verification code to verify your email address");
                            textInputDialog.setContentText("Verification code:");

                            Optional<String> code = textInputDialog.showAndWait();
                            if (code.isPresent() && code.get().equals(Integer.toString(verificationEmail.verificationCode))) {
                                // If the code entered matches,
                                // Register the user
                                registerUser();

                                // Display sign-up successful pop-up message
                                Alert alert = new Alert(Alert.AlertType.NONE);
                                alert.setGraphic(new ImageView(Objects.requireNonNull(this.getClass().getResource("GreenTick.gif")).toString()));
                                alert.setTitle("Success");
                                alert.setHeaderText("Your account has been created.");
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
                        if (emailResult != null) {
                            try {
                                emailResult.close();
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
                    notMatchLabel.setVisible(true);
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
            // If username entered is empty,
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid username");
            alert.setHeaderText("The username entered is empty.");
            alert.setContentText("Please re-enter a valid username.");
            alert.showAndWait();
        }
    }

    @FXML
    // Prompt sign-up button pressed in the login page
    public void signUpPromptButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the sign-up page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-register-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Quit button in the sign-up page pressed
    public void registrationPageQuitButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-login-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    // Login button pressed in the login page
    public void loginButtonPressed(MouseEvent event) throws IOException {
        // hide the "Please enter email and password" label
        loginMessageLabel.setVisible(false);

        if (!emailEntered_Login.getText().isBlank() && !passwordEntered_Login.getText().isBlank()) {
            //  if email and password entered is not empty,
            // Check the credentials entered by the user
            if (validateLogin()) {
                // if valid,
                // Forward user to the homepage the credentials matches
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } else {
            loginMessageLabel.setVisible(true);
        }
    }

    /**
     * This method was made to validate the email entered by user in the sign-up page
     *
     * @param input the email entered by the user
     * @return a boolean value indicating the validity of the email
     */
    public static boolean valEmail(String input) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }

    /**
     * Register users and store their credentials into our database.
     */
    public void registerUser() {

        Connection connectDB = null;
        Statement statement = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            String username = username_SignUp.getText();
            String email = emailEntered_SignUp.getText();
            String password = passwordEntered_SignUp.getText();
            String insertFields = "INSERT INTO user_account (username, email, password) VALUES ('";
            String insertValues = username + "','" + email + "','" + password + "')";
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

    /**
     * Validate the credentials entered by the user with our database and log them into the server if it matches, and
     * store the current user's info who has logged into the server in the User class
     *
     * @return a boolean value indicating the validity of the credentials entered
     */
    public boolean validateLogin() {

        Connection connectDB = null;
        Statement statement = null;
        ResultSet queryResult = null;

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();
            statement = connectDB.createStatement();
            String email = emailEntered_Login.getText();
            String password = passwordEntered_Login.getText();
            queryResult = statement.executeQuery("SELECT * FROM user_account WHERE email = '" + email + "' AND password ='" + password + "'");
            // if the query result is not empty
            if (queryResult.next()) {
                String retrievedEmail = queryResult.getString("email");
                String retrievedPassword = queryResult.getString("password");
                if (retrievedEmail.equals(email) && retrievedPassword.equals(password)) {
                    // if the credentials matches
                    // display login successful pop-up message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Welcome to Omazon!");
                    alert.showAndWait();

                    //store current user's info in the User class
                    User.setUsername(queryResult.getString("username"));
                    User.setEmail(retrievedEmail);
                    User.setPassword(retrievedPassword);
                    User.setAddress(queryResult.getString("address"));
                    User.setPaymentPassword(queryResult.getString("paymentPassword"));
                    User.setBalance(Double.parseDouble(queryResult.getString("balance")));
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
}


