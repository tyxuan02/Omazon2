package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;

/**
 * This class acts as a controller for the ordered-item-template
 *
 * @author XiangLun
 */
public class OrderedItemController {

    /**
     * This field is used to store the current OrderedItem object.
     */
    private OrderedItem currentOrderedItem;

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label quantity;

    @FXML
    void deliveredButtonPressed(ActionEvent event) throws IOException {
        // remove the particular record and add the removed record into order history
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        jsonFileUtil.deleteOrderRecord(currentOrderedItem);

        // reload the contents in the vBox
        FXMLLoader loader = new FXMLLoader(getClass().getResource("purchase-page.fxml"));
        loader.load();
        UserPurchasePageController userPurchasePageController = loader.getController();
        userPurchasePageController.refreshOrderedItem();
        // TODO: 1/11/2022 find a way to refresh the vbox after pressing the delivered button
        // TODO: 1/11/2022 pop up a feedback window 
    }

    /**
     * This method fills in the information of the ordered item into the template
     *
     * @param orderedItem an instance of the Ordered Item class
     * @author XiangLun
     */
    public void setData(OrderedItem orderedItem) {
        currentOrderedItem = orderedItem;
        productName.setText(orderedItem.getProductName());
        quantity.setText(Integer.toString(orderedItem.getQuantity()));
        Image image = new Image(new File(orderedItem.getOrderedImagePath()).toURI().toString());
        productImage.setImage(image);
    }
}
