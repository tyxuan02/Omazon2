package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class ProductController {
    @FXML
    private ImageView productImagePath;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    /**
     * This method fills in the information of the product into the template
     *
     * @param product an instance of the Product class
     * @author XiangLun
     */
    public void setData(Product product) {
        productName.setText(product.getProductName());
        productPrice.setText(Double.toString(product.getProductPrice()));
        Image image = new Image(new File(product.getProductImagePath()).toURI().toString());
        productImagePath.setImage(image);
    }
}
