package com.example.omazonproject;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * An interface class named ProductListener
 */
public interface ProductListener {

    /**
     * An abstract method named onClickListener
     * @param product   object of Product
     * @throws IOException
     */
    public void onClickListener (Product product) throws IOException;

}
