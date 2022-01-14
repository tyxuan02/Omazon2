package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


/**
 * This class is a blueprint for all products, which will be used in the User Homepage, Product Search Page and Seller Homepage
 */
public class ProductController {

    /**
     * An image view used to display the product's image
     */
    @FXML
    private ImageView productImagePath;

    /**
     * A label used to display the product's name
     */
    @FXML
    private Label productName;

    /**
     * A label used to display the product's price
     */
    @FXML
    private Label productPrice;

    /**
     * An object of the ProductListener
     */
    private ProductListener productListener;

    /**
     * An object of the Product class
     */
    private Product product;

    /**
     * This is an input event that occurs when a mouse is clicked
     */
    static MouseEvent event;

    /**
     * A click button at product template
     * This method will direct user to product page and direct seller to seller edit product page
     *
     * @param event An instance of the MouseEvent class
     * @throws IOException input-output exception
     */
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


