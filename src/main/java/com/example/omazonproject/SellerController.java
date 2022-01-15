package com.example.omazonproject;

import javafx.application.Platform;
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
 * This part is responsible to control the events happening at the seller-login-page and seller-register-page
 */


public class SellerController {

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
     * A text field to fill seller name at seller sign-up page
     */
    @FXML
    private TextField sellerName_Signup;

    /**
     * A text field to fill seller email address at seller sign-up page
     */
    @FXML
    private TextField sellerEmail_SignUp;

    /**
     * A text field to fill seller shop address at seller sign-up page
     */
    @FXML
    private TextField shopAddress_SignUp;

    /**
     * A password field to fill seller account password at seller sign-up page
     */
    @FXML
    private PasswordField sellerPassword_SignUp;

    /**
     * A password field to fill seller account confirm password at seller sign-up page
     */
    @FXML
    private PasswordField sellerConfirmPassword_SignUp;

    /**
     * A label to display seller account password and seller account confirm password do not match at seller sign-up page
     */
    @FXML
    private Label sellerSignUpPageNotMatchLabel;

    /**
     * A text field to fill seller email address at seller login page
     */
    @FXML
    private TextField sellerEmail_Login;

    /**
     * A text field to fill seller account password at seller login page
     */
    @FXML
    private TextField sellerPassword_Login;

    /**
     * A label to display login message label("Please enter email address and password") at seller login page
     */
    @FXML
    private Label sellerLoginMessageLabel;

    /**
     * Send a conformation email to the user and ask for the conformation code,
     * allow the user to change their password when the conformation code matches
     *
     * @throws MessagingException when the program fails to send email
     */
    @FXML
    void forgotPasswordBtnPressed() throws MessagingException {
        // if forget button is pressed,
        // inform the seller that they will receive an email
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reset Password");
        alert.setHeaderText(null);
        alert.setContentText("An email containing a confirmation code will be sent to the email address entered at the email text field.");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> btn = alert.showAndWait();
        if (sellerEmail_Login.getText().isEmpty() || !valEmail(sellerEmail_Login.getText())) {
            // inform the seller to enter their email at the email text field
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed To Send Conformation Email");
            alert1.setHeaderText(null);
            alert1.setContentText("Please key-in your valid email address at the email text field in order to receive a conformation email");
            alert1.showAndWait();

        } else if (btn.isPresent() && btn.get() == ButtonType.OK) {
            // send conformation email to the seller
            VerificationEmail verificationEmail = new VerificationEmail();
            verificationEmail.sendVerificationEmail(sellerEmail_Login.getText(), "forgetPassword");

            // request the confirmation code from the seller
            TextInputDialog textInputDialog = new TextInputDialog();
            textInputDialog.setTitle("Confirmation Email Sent Successfully");
            textInputDialog.setHeaderText("Please enter the confirmation code.");
            textInputDialog.setContentText("Confirmation code:");
            Optional<String> code = textInputDialog.showAndWait();

            if (code.isPresent() && code.get().equals(Integer.toString(verificationEmail.verificationCode))) {
                // if the code entered is correct,
                // let the seller change the password

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

                    // Connect to the database and change the seller's password
                    Connection connection = null;
                    PreparedStatement psUpdatePass = null;

                    try {
                        // Change the seller's password in the database
                        DatabaseConnection db = new DatabaseConnection();
                        connection = db.getConnection();
                        psUpdatePass = connection.prepareStatement("UPDATE seller_account SET password = ? WHERE email = ?");
                        psUpdatePass.setString(1, newPassword.getKey());
                        psUpdatePass.setString(2, sellerEmail_Login.getText());
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

    /**
     * Sign up button pressed at seller sign-up page
     * This method will check all the information entered by the user while the user is signing up at seller sign-up page
     */
    @FXML
    public void sellerSignUpButtonPressed() throws MessagingException {

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
                                // inform the seller that they will receive an email
                                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                alert1.setTitle("Creating new seller account");
                                alert1.setHeaderText(null);
                                alert1.setContentText("An email containing a verification email will be sent to your email address.");
                                alert1.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

                                Optional<ButtonType> btn = alert1.showAndWait();
                                if (btn.isPresent() && btn.get() == ButtonType.OK) {
                                    // if the seller request an email,
                                    // send a confirmation email to the user
                                    VerificationEmail verificationEmail = new VerificationEmail();
                                    verificationEmail.sendVerificationEmail(sellerEmail_SignUp.getText(), "seller");

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
     * @author XiangLun
     */
    public static boolean valEmail(String input) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }


    /**
     * This method is used to register seller and store seller credentials in database
     */
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

    /**
     * A button at seller login up
     * This method will direct user to seller sign-up page after clicking it
     *
     * @param event An instance of the MouseEvent class
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void sellerSignUpPromptButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the seller sign-up page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-register-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A quit button at seller sign-up page
     * This method will direct user to seller login page
     *
     * @param event An instance of the MouseEvent class
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void sellerRegistrationPageQuitButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the seller login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A quit button at seller login up
     * This method will direct user to user homepage
     *
     * @param event An instance of the MouseEvent class
     * @throws IOException when the resource file is not found
     */
    @FXML
    void sellerLoginPageQuitButtonPressed(MouseEvent event) throws IOException {
        // Forward user to user homepage
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A login button at seller login page
     * This method will direct seller to seller homepage if the credentials entered are true
     *
     * @param event An instance of the MouseEvent class
     * @throws IOException when the resource file is not found
     */
    @FXML
    public void sellerLoginButtonPressed(MouseEvent event) throws IOException {
        // Hide the "Please enter email and password" label
        sellerLoginMessageLabel.setVisible(false);

        if (!sellerEmail_Login.getText().isBlank() && !sellerPassword_Login.getText().isBlank()) {
            //  if email and password entered is not empty,
            // Check the credentials entered by the user
            if (sellerValidateLogin()) {
                // if valid,
                // forward the user to the seller centre
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

    /**
     * Validate the credentials entered by the seller with our database and log them into the seller page if it matches, and
     * store the current seller's info who has logged into the server in the Seller class
     *
     * @return A boolean value indicating the validity of the credentials entered
     */
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
                    //store current seller's info in the Seller class
                    Seller.setSellerName(queryResult.getString("sellerName"));
                    Seller.setEmail(retrievedSellerEmail);
                    Seller.setPassword(retrievedSellerPassword);
                    Seller.setAddress(queryResult.getString("address"));
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


