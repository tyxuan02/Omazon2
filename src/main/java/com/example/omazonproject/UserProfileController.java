package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is responsible to control the events happening in the user profile page
 */
public class UserProfileController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField currentPassword;

    @FXML
    private Label emailAddress;

    @FXML
    private TextArea pickupAddress;

    @FXML
    private TextField username;


    @FXML
    void accountBalanceButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user-account-balance-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void deleteAccountButtonPressed(ActionEvent event) {

    }

    @FXML
    void favouriteListButtonPressed(ActionEvent event) {

    }

    @FXML
    void homepageButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void myPurchaseButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("purchase-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void saveButtonPressed(ActionEvent event) {

    }

    @FXML
    void startSellingButtonPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-login-page.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void changeLoginPasswordButtonPressed(ActionEvent event) {
        // generate a text input dialog box to request the user to enter the current password
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Change Login Password");
        textInputDialog.setHeaderText("Please enter the current password before changing the password.");
        textInputDialog.setContentText("Current password:");
        Optional<String> currentPassword = textInputDialog.showAndWait();
        if (currentPassword.isPresent() && currentPassword.get().equals(User.password)) {
            // if the password entered matches with the current password

            // generate a dialog box to let the user enter their desired password to change to
            // create a new custom dialog box with two inputs
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Change Login Password");
            dialog.setHeaderText("The password provided matches. Please enter your new password.");

            // Set up the button.
            ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

            // Create the username and password labels and fields.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            PasswordField newPass1 = new PasswordField();
            newPass1.setPromptText("Enter new password");
            PasswordField newPass2 = new PasswordField();
            newPass2.setPromptText("Re-enter new password");
            Text warningText = new Text("The confirmation password is empty or does not match");
            warningText.setFill(Color.RED);

            grid.add(new Label("New password:"), 0, 0);
            grid.add(newPass1, 1, 0);
            grid.add(new Label("Confirm new password:"), 0, 1);
            grid.add(newPass2, 1, 1);
            grid.add(warningText, 0, 2);
            dialog.getDialogPane().setContent(grid);

            // Enable/Disable change button depending on whether the passwords match.
            // Show/Hide warning text depending on whether the passwords match.
            // TODO: 12/10/2021 fix listener issue. ie. when the user enter confirmation password first then change the new password
            Node changeButton = dialog.getDialogPane().lookupButton(changeButtonType);
            changeButton.setDisable(true);
            newPass2.setDisable(true);
            newPass1.textProperty().addListener((observable, oldValue, newValue) -> {
                newPass2.setDisable(false);
                newPass2.textProperty().addListener((observable2, oldValue2, newValue2) -> {
                    warningText.setVisible(!newValue.equals(newValue2));
                    changeButton.setDisable(!newValue.equals(newValue2));
                });
            });
            Optional<Pair<String, String>> result = dialog.showAndWait();

            // TODO: 12/10/2021 connect to the database and change the user's password
/*            result.ifPresent(newPassword -> {
                //if the confirmation password matches
                //connect to the database and change the user's password
                 Connection connection = null;
                            PreparedStatement psChangePass = null;
                            ResultSet resultSet = null;

                            try {
                                DatabaseConnection db = new DatabaseConnection();
                                connection = db.getConnection();
                                psChangePass = connection.prepareStatement("SELECT * FROM user_account WHERE username = ?");
                                psChangePass.setString(1, User.username);

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
            });*/

        } else {
            // if the password entered does not match with the current password
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("The password entered does not match with the current password.");
            alert.setContentText("Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    void setPaymentPasswordButtonPressed(ActionEvent event) {

    }

    /**
     * This method fills the user's information into the respective text field
     */
    public void setInitialContents() {
        emailAddress.setText(User.email);
        username.setText(User.username);
        pickupAddress.setText(User.address);
    }

}
