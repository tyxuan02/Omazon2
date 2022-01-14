package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    /**
     * This method is used to store seller's reply in a .json file
     */
    @FXML
    void replyButtonPressed() {
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