package com.example.omazonproject;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

public interface ProductListener {

    void onClickListener (Product product) throws IOException;

}
