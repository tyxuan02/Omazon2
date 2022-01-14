package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

/**
 * This class is used as a controller for the cart-item-template.fxml, where the fxml file is used
 * as a template for both the cart item and the order history item
 */
public class CartItemController {

    /**
     * A label used to display the item's price per unit
     */
    @FXML
    private Label pricePerUnit;

    /**
     * An image view used to display the item's image
     */
    @FXML
    private ImageView productImage;

    /**
     * A label used to display the item's quantity
     */
    @FXML
    private Label quantity;

    /**
     * A label used to display the item's name
     */
    @FXML
    private Label productName;

    /**
     * A label used to display the total price of the cart item
     */
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

    /**
     * This method fills in the information of the order history item into the template
     *
     * @param orderHistoryItem an instance of the Cart Item class
     * @author XiangLun
     */
    public void setData(OrderHistoryItem orderHistoryItem) {
        productName.setText(orderHistoryItem.getProductName());
        pricePerUnit.setText(String.format("RM %.2f", orderHistoryItem.getPricePerUnit()));
        quantity.setText(Integer.toString(orderHistoryItem.getQuantity()));
        totalPrice.setText(String.format("RM %.2f", orderHistoryItem.getPricePerUnit() * orderHistoryItem.getQuantity()));
        Image image = new Image(new File(orderHistoryItem.getOrderHistoryItemImagePath()).toURI().toString());
        productImage.setImage(image);
    }
}
