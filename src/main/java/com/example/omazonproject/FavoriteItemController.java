package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class FavoriteItemController {

    /**
     * A label used to display the item's price
     */
    @FXML
    private Label priceLabel;

    /**
     * An image view used to display the product's image
     */
    @FXML
    private ImageView productImage;

    /**
     * A label used to display the product's name
     */
    @FXML
    private Label productName;

    /**
     * This method fills in the information of the favorite item into the template
     *
     * @param favoriteItem an instance of the Favorite Item class
     * @author XiangLun
     */
    public void setData(FavoriteItem favoriteItem) {
        productName.setText(favoriteItem.getProductName());
        priceLabel.setText(String.format("RM %.2f", favoriteItem.getPricePerUnit()));
        Image image = new Image(new File(favoriteItem.getFavoriteImagePath()).toURI().toString());
        productImage.setImage(image);
    }
}

