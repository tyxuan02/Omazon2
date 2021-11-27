package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
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
        // Sign-up button pressed at the sign-up page
    void signUpButtonPressed(MouseEvent event) {
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

    @FXML
        // Login button pressed
    void loginButtonPressed(MouseEvent event) {

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


