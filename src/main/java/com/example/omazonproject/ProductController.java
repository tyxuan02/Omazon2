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
     * An image view to display product image
     */
    @FXML
    public ImageView productImagePath;

    /**
     * A label to display prodcut name
     */
    @FXML
    public Label productName;

    /**
     * A label to display product price
     */
    @FXML
    public Label productPrice;

    /**
     * An object of ProductListener
     */
    public ProductListener productListener;

    /**
     * An object of Product
     */
    public Product product;

    /**
     * This is an input event that occurs when a mouse is clicked
     */
    public static MouseEvent event;

    /**
     * A click button at product template
     * This method will direct user to product page and direct seller to seller edit product page
     * @param event
     * @throws IOException
     */
    @FXML
    public void click(MouseEvent event) throws IOException {
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


