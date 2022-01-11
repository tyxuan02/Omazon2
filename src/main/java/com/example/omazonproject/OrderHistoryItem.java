package com.example.omazonproject;

/**
 * This class is a blueprint for all order history item, which will be used in the purchase page
 *
 * @author XiangLun
 */
public class OrderHistoryItem {
    private String productName;
    private String orderHistoryItemImagePath;
    private int quantity;
    private double pricePerUnit;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderHistoryItemImagePath() {
        return orderHistoryItemImagePath;
    }

    public void setOrderHistoryItemImagePath(String orderHistoryItemImagePath) {
        this.orderHistoryItemImagePath = orderHistoryItemImagePath;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
