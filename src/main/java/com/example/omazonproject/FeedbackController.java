package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is used as a controller for the feedback-page.fxml
 */
public class FeedbackController {

    @FXML
    private Rating rating;

    @FXML
    private TextArea textArea;

    private String imageName;

    @FXML
    void OKBtnPressed(ActionEvent event) {
        // update the rating in the database (ie. numberOfFiveStars++) and the json file
        int numberOfStars = (int) rating.getRating();

        Connection connectDB = null;
        Statement statement = null;

        try {

            DatabaseConnection connectNow = new DatabaseConnection();
            connectDB = connectNow.getConnection();

            String updateNumOfStars;

            if (numberOfStars == 1) {
                updateNumOfStars = "UPDATE product_info set numOfOneStars = IFNULL(numOfOneStars, 0) + 1 WHERE imageName = '" + imageName + "'";
            } else if  (numberOfStars == 2) {
                updateNumOfStars = "UPDATE product_info set numOfTwoStars = IFNULL(numOfTwoStars, 0) + 1 WHERE imageName = '" + imageName + "'";
            } else if  (numberOfStars == 3) {
                updateNumOfStars = "UPDATE product_info set numOfThreeStars = IFNULL(numOfThreeStars, 0) + 1 WHERE imageName = '" + imageName + "'";
            } else if  (numberOfStars == 4) {
                updateNumOfStars = "UPDATE product_info set numOfFourStars = IFNULL(numOfFourStars, 0) + 1 WHERE imageName = '" + imageName + "'";
            } else {
                updateNumOfStars = "UPDATE product_info set numOfFiveStars = IFNULL(numOfFiveStars, 0) + 1 WHERE imageName = '" + imageName + "'";
            }

            statement = connectDB.createStatement();
            statement.executeUpdate(updateNumOfStars);

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

        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        jsonFileUtil.writeProductReview(imageName,numberOfStars,textArea.getText(),User.getUsername(),"");

        // close the pop-up window
        Node n = (Node) event.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

    // This method is used to get product image name that user wants to review
    void getProductName (String imageName) {
        this.imageName = imageName;
    }

}
