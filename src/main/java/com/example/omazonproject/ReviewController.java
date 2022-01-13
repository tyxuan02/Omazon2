package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReviewController {

    @FXML
    private Label reply;

    @FXML
    private Label review;

    @FXML
    private Label username;


    /**
     * This method fills in the information of the review product into the template
     *
     * @param productReview an instance of the Product Review class
     * @author YuXuan
     */
    public void setData(ProductReview productReview) {
        username.setText(productReview.getUsername());
        review.setText(productReview.getUserReview());
        reply.setText(productReview.getSellerReply());
    }
}