package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class ProductController {

    @FXML
    private ImageView productImagePath;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    private ProductListener productListener;

    private Product product;

    static MouseEvent event;

    @FXML
    void click(MouseEvent event) throws IOException {
        ProductController.event = event;
        productListener.onClickListener(product);
    }

    /**
     * This method fills in the information of the product into the template
     *
     * @param product an instance of the Product class
     * @author XiangLun
     */

    public void setData(Product product, ProductListener productListener) throws MalformedURLException {
        this.product = product;
        this.productListener = productListener;
        productName.setText(product.getProductName());
        productPrice.setText("RM " + String.format("%.2f", product.getProductPrice()));
        Image image = new Image(new File("src/main/resources/images/" + product.getProductImagePath() + ".png").toURI().toString());
        productImagePath.setImage(image);
    }

}


