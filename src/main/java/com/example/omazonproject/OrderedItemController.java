package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class OrderedItemController {

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label quantity;

    @FXML
    void deliveredButtonPressed(ActionEvent event) {

    }

    /**
     * This method fills in the information of the ordered item into the template
     *
     * @param orderedItem an instance of the Ordered Item class
     * @author XiangLun
     */
    public void setData(OrderedItem orderedItem) {
        productName.setText(orderedItem.getProductName());
        quantity.setText(Integer.toString(orderedItem.getQuantity()));
        Image image = new Image(new File(orderedItem.getOrderedImagePath()).toURI().toString());
        productImage.setImage(image);
    }
}
