package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

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
