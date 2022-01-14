package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This class acts as a controller for review-template.fxml
 */

public class ReviewController {

    /**
     * A label used to display the seller's reply
     */
    @FXML
    private Label reply;

    /**
     * A label used to display the customer's review
     */
    @FXML
    private Label review;

    /**
     * A label used to display the customer's username
     */
    @FXML
    private Label username;


    /**
     * This method will fill in the review of a product into review template
     *
     * @param productReview an object of the Product Review class
     */
    public void setData(ProductReview productReview) {
        username.setText(productReview.getUsername());
        review.setText(productReview.getUserReview());
        reply.setText(productReview.getSellerReply());
    }
}