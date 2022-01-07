package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class CartItemController {

    @FXML
    private Label pricePerUnit;

    @FXML
    private ImageView productImage;

    @FXML
    private Label quantity;

    @FXML
    private Label productName;

    @FXML
    private Label totalPrice;

    /**
     * This method fills in the information of the cart item into the template
     *
     * @param cartItem an instance of the Cart Item class
     * @author XiangLun
     */
    public void setData(CartItem cartItem) {
        productName.setText(cartItem.getProductName());
        pricePerUnit.setText(String.format("RM %.2f", cartItem.getPricePerUnit()));
        quantity.setText(Integer.toString(cartItem.getQuantity()));
        totalPrice.setText(String.format("RM %.2f", cartItem.getPricePerUnit() * cartItem.getQuantity()));
        Image image = new Image(new File(cartItem.getCartImagePath()).toURI().toString());
        productImage.setImage(image);
    }
}
