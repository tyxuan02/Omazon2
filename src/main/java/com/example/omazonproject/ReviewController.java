package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * This controller is responsible to control event happening at review-template.fxml
 */

public class ReviewController {

    /**
     * A label to display seller reply
     */
    @FXML
    public Label reply;

    /**
     * A label to display customer review
     */
    @FXML
    public Label review;

    /**
     * A label to display customer username
     */
    @FXML
    public Label username;


    /**
     * This method will fill in the review of a product into review template
     *
     * @param productReview     an object of the Product Review class
     */
    public void setData(ProductReview productReview) {
        username.setText(productReview.getUsername());
        review.setText(productReview.getUserReview());
        reply.setText(productReview.getSellerReply());
    }
}