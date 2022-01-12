package com.example.omazonproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

    private String imageName;

    @FXML
    void deliveredButtonPressed(ActionEvent event) throws IOException {
        // remove the particular record and add the removed record into order history
        JsonFileUtil jsonFileUtil = new JsonFileUtil();
        jsonFileUtil.deleteOrderRecord(currentOrderedItem);

        // pop up the feedback page
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("feedback-page.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Leave a Review!");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);

        // pass product image name as a parameter to FeedbackController
        FeedbackController controller = fxmlLoader.getController();
        controller.getProductName(imageName);

        stage.showAndWait();
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
        this.imageName = orderedItem.getOrderedImageName();
        productImage.setImage(image);
    }
}
