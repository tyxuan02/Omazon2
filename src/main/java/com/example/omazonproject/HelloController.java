package com.example.omazonproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.*;
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
    void forgotPasswordBtnPressed(ActionEvent event) throws MessagingException {
        // if forget button is pressed,
        // inform the user that they will receive an email
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset Password");
        alert.setHeaderText(null);
        alert.setContentText("An email containing a confirmation code will be sent to the email address entered at the email text field.");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> btn = alert.showAndWait();
        if (emailEntered_Login.getText().isEmpty() || !valEmail(emailEntered_Login.getText())) {
            // inform the user to enter their email at the email text field
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed To Send Conformation Email");
            alert1.setHeaderText(null);
            alert1.setContentText("Please key-in your valid email address at the email text field in order to receive a conformation email");
            alert1.showAndWait();

        } else if (btn.isPresent() && btn.get() == ButtonType.OK) {
            // send conformation email to the user
            VerificationEmail verificationEmail = new VerificationEmail();
            verificationEmail.sendVerificationEmail(emailEntered_Login.getText(), "forgetPassword");

            // request the confirmation code from the user
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Confirmation Email Sent Successfully");
            textInputDialog.setHeaderText("Please enter the confirmation code.");
            textInputDialog.setContentText("Confirmation code:");
            Optional<String> code = textInputDialog.showAndWait();

            if (code.isPresent() && code.get().equals(Integer.toString(verificationEmail.verificationCode))) {
                // if the code entered is correct,
                // let the user change the password

                // create a new custom dialog box with two inputs
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Change Login Password");
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

                    // Connect to the database and change the user's password
                    Connection connection = null;
                    PreparedStatement psUpdatePass = null;

                    try {
                        // Change the user's password in the database
                        DatabaseConnection db = new DatabaseConnection();
                        connection = db.getConnection();
                        psUpdatePass = connection.prepareStatement("UPDATE user_account SET password = ? WHERE email = ?");
                        psUpdatePass.setString(1, newPassword.getKey());
                        psUpdatePass.setString(2, emailEntered_Login.getText());
                        psUpdatePass.executeUpdate();

                        // Display successful pop-up message
                        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                        alert1.setTitle("Successful");
                        alert1.setHeaderText(null);
                        alert1.setContentText("Password changed successfully.");
                        alert1.showAndWait();

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

                });

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
                            // inform the user that they will receive an email
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Creating new account");
                            alert1.setHeaderText(null);
                            alert1.setContentText("An email containing a verification email will be sent to your email address.");
                            alert1.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

                            Optional<ButtonType> btn = alert1.showAndWait();
                            if (btn.isPresent() && btn.get() == ButtonType.OK) {
                                // if the user request an email,
                                // send a confirmation email to the user
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
                    if (queryResult.getString("balance") == null) {
                        User.setBalance(0);
                    } else {
                        User.setBalance(Double.parseDouble(queryResult.getString("balance")));
                    }
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


