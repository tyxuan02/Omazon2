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

    private ProductReview productReview;

    @FXML
    private TextField reply;

    @FXML
    private Label review;

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
     * This method fills in the information of the review product into the template and let the seller reply to user reviews
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