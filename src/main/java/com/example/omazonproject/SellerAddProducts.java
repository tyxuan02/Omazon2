package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SellerAddProducts {
    @FXML
    private ComboBox comboBox;

    @FXML
    public void initialize() {
        comboBox.getItems().removeAll(comboBox.getItems());
        comboBox.getItems().addAll("Electronic Devices","Fashion","Food","Health & Beauty","Sports","TV & Home Appliances");
        comboBox.getSelectionModel().select("Choose Category");

    }
}
