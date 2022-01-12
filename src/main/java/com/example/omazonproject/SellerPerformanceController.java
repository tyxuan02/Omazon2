package com.example.omazonproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as a controller for the seller's performance page
 *
 * @author XiangLun
 */
public class SellerPerformanceController {
    @FXML
    private Label bestSellingProduct;

    @FXML
    private Label numberOfSales;

    @FXML
    private Label profitLabel;

    public void initialize() {
        // a list of the product of the seller
        List<Product> productList = new ArrayList<>();

        // connect to database to fetch the seller's performance
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;

        try {
            // fetching result
            DatabaseConnection db = new DatabaseConnection();
            connection = db.getConnection();
            psUpdate = connection.prepareStatement("SELECT * FROM product_info WHERE sellerEmail = ?");
            psUpdate.setString(1, Seller.getEmail());
            resultSet = psUpdate.executeQuery();

            // add all the seller's product into a Product list
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductName(resultSet.getString("name"));
                product.setProductPrice(Double.parseDouble(resultSet.getString("price")));
                if (resultSet.getString("numberOfSales") != null) {
                    product.setNumberOfSales(Integer.parseInt(resultSet.getString("numberOfSales")));
                } else {
                    product.setNumberOfSales(0);
                }
                productList.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        double profit = 0;
        int totalNumberOfSales = 0;
        int mostSale = Integer.MIN_VALUE;
        String mostSaleProduct = null;
        // Iterate through the list of product
        for (Product product : productList) {
            int sales = product.getNumberOfSales();
            double price = product.getProductPrice();

            // keep track of the most sale product
            if (sales > mostSale) {
                mostSale = sales;
                mostSaleProduct = product.getProductName();
            }

            // calculate the total profit and total sales
            profit += sales * price;
            totalNumberOfSales += sales;
        }

        // fill in the required field to the template
        bestSellingProduct.setText(mostSaleProduct);
        numberOfSales.setText(Integer.toString(totalNumberOfSales));
        profitLabel.setText(String.format("RM %.2f", profit));
    }
}
