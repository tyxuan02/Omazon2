package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SellerReplyController {

    /**
     * An object of ProductReview class
     */
    private ProductReview productReview;

    /**
     * A text field to fill seller reply
     */
    @FXML
    private TextField reply;

    /**
     * A label to display user comment
     */
    @FXML
    private Label review;

    /**
     * A label to display username
     */
    @FXML
    private Label username;

    @FXML
    void replyButtonPressed(ActionEvent event) {
        // store seller reply in (product image name).json file
        productReview.setSellerReply(reply.getText());
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        jsonFileUtil.writeSellerReply(productReview);
    }

    /**
     * This method will fill in the information of the review product into the template and let the seller reply to user reviews
     *
     * @param productReview an instance of the Product Review class
     * @author YuXuan
     */
    public void setData(ProductReview productReview) {
        this.productReview = productReview;
        username.setText(productReview.getUsername());
        review.setText(productReview.getUserReview());
        reply.setText(productReview.getSellerReply());
    }
}