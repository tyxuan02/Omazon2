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


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    // email entered by user at the login page
    private TextField emailEntered_Login;

    @FXML
    // password entered by user at the login page
    private PasswordField passwordEntered_Login;

    @FXML
    // confirm password entered by user at the sign-up page
    private PasswordField confirmPassword_SignUp;

    @FXML
    // email entered by user at the sign-up page
    private TextField emailEntered_SignUp;

    @FXML
    // password entered by user at the sign-up page
    private PasswordField passwordEntered_SignUp;

    @FXML
    // username entered by user at the sign-up page
    private TextField username_SignUp;

    @FXML
    // login message
    private Label loginMessageLabel;

    @FXML
    // check password and confirm password
    private Label confirmPasswordLabel;

    @FXML
        // Sign-up button pressed at the sign-up page
    void signUpButtonPressed(MouseEvent event) {

        if (passwordEntered_SignUp.getText().equals(confirmPassword_SignUp.getText()) && ((passwordEntered_SignUp.getText() != "") && (confirmPassword_SignUp.getText() != ""))) {
            registerUser();
        } else {
            confirmPasswordLabel.setText("Password does not match.");
        }
    }
/*
        // Validating the email address entered by the user
        if (valEmail(emailEntered_SignUp.getText())) {
            // if valid email address is entered
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setGraphic(new ImageView(Objects.requireNonNull(this.getClass().getResource("GreenTick.gif")).toString()));
            alert.setTitle("Success");
            alert.setHeaderText("Your account has been created.");
            alert.setContentText("Thank you for signing up at Omazon :D");
            alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
            alert.showAndWait();
        } else {
            // if invalid email address is entered
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid email address");
            alert.setHeaderText("The email address entered is invalid.");
            alert.setContentText("Please re-enter a valid email address.");
            alert.showAndWait();
        }
    }
    */



    void registerUser(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String username = username_SignUp.getText();
        String email = emailEntered_SignUp.getText();
        String password = passwordEntered_SignUp.getText();
        String insertFields = "INSERT INTO user_account (username, email, password) VALUES ('";
        String insertValues = username + "','" + email + "','" + password +"')";
        String insertToRegister = insertFields + insertValues;

        try {
            Statement statement = connectDB.createStatement();

            if (!username_SignUp.getText().equals("") && !emailEntered_SignUp.getText().equals("") ) {

                confirmPasswordLabel.setText("");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sign up successful.");
                alert.setHeaderText(null);
                alert.setContentText("Your account has been created.");
                alert.showAndWait();
                statement.executeUpdate(insertToRegister);

            }
            else {
                confirmPasswordLabel.setText("");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("Please enter data in correct format.");
                alert.showAndWait();
            }

        } catch (SQLException e){
            e.printStackTrace();
            e.getCause();
        }

    }


    @FXML
        // Login button pressed
    void loginButtonPressed(MouseEvent event) {
        if (!emailEntered_Login.getText().isBlank() && !passwordEntered_Login.getText().isBlank()){
            loginMessageLabel.setText("");
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter email and password");
        }
    }

    void validateLogin() {

        try {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            Statement statement = connectDB.createStatement();
            String email = emailEntered_Login.getText();
            String password = passwordEntered_Login.getText();
            ResultSet queryResult = statement.executeQuery("SELECT * FROM user_account WHERE email = '" + email + "' AND password ='" + password + "'");
                if (queryResult.next()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Login Successful!");
                    alert.setHeaderText(null);
                    alert.setContentText("Welcome to Omazon");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid credentials. Please re-enter a valid email address.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }





    @FXML
        // Sign-up button pressed at the login page
    void signUpPromptButtonPressed(MouseEvent event) throws IOException {
        // Forward user to the sign-up page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RegisterPage.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void registrationPageQuitButtonPressed(MouseEvent event) throws IOException {
        // Forward user back to the login page
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Email-validating method
    public static boolean valEmail(String input) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(input);
        return matcher.find();
    }
}


