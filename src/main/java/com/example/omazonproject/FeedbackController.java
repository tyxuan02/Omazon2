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

    /**
     * A rating to display product rating
     */
    @FXML
    public Rating rating;

    /**
     * A text area to fill user review
     */
    @FXML
    public TextArea textArea;

    /**
     * An instance variable named imageName with String data type
     */
    public String imageName;

    /**
     * This method is used to update rating in database
     * This method is used to write image name, number of stars, user review and username in (product image name)json.file
     * Each product will have its own json file
     * @param event
     */
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

    /**
     * A method to return imageName in String
     * @param imageName     product image name
     */
    public void getProductName (String imageName) {
        this.imageName = imageName;
    }

}
