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

    @FXML
    void OKBtnPressed(ActionEvent event) {
        // update the rating in the database (ie. numberOfFiveStars++) and the json file
        int numberOfStars = (int) rating.getRating();

        // TODO store the comment in the json file (YuXuan)
        // TODO store the username of the user in the json file (YuXuan)

        // close the pop-up window
        Node n = (Node) event.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.close();
    }

}
