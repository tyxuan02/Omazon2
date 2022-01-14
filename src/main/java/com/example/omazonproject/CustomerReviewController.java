package com.example.omazonproject;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This class acts as a controller for the customer review page
 *
 * @author XiangLun
 */
public class CustomerReviewController {

    /**
     * A label used to display the percentage of the particular product when the pie chart is clicked
     */
    @FXML
    private Label percentageLabel;

    /**
     * A pie chart to display the numbers of sales for all products
     */
    @FXML
    private PieChart pieChart;

    /**
     * A bar chart to display the product's rating for each product
     */
    @FXML
    private BarChart<String, Number> barChart;

    /**
     * This method will direct the seller to the seller home page after it is clicked
     *
     * @param event An instance of the ActionEvent class
     * @throws IOException when the resource file is not found
     */
    @FXML
    void homeButtonPressed(ActionEvent event) throws IOException {
        // forward the user back to the seller home page
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller's-product-page.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A profile button at customer review page
     * This method will direct seller to seller homepage
     * @param event An instance of the ActionEvent class
     * @throws IOException
     */
    @FXML
    void profileButtonPressed (ActionEvent event) throws IOException {
        // forward the user back to the seller profile page
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seller-profile-page.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This initializes method initializes the pie chart after retrieving all the seller's products from the database,
     * play the fade in, slide up animation for both the pie chart and the bar chart, and
     * set up event handler for each segment of the pie chart
     */
    @FXML
    public void initialize() {
        // create a list to store sales data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // create an array to store the number of ratings for the bar chart
        int[] ratings = new int[5];

        // create a series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // connect to the database and retrieve all the seller's products
        Connection connection = null;
        PreparedStatement psGetProducts = null;
        ResultSet resultset = null;

        int totalNumOfSales = 0;
        try {
            DatabaseConnection db = new DatabaseConnection();
            connection = db.getConnection();
            psGetProducts = connection.prepareStatement("SELECT * FROM product_info WHERE sellerEmail = ?");
            psGetProducts.setString(1, Seller.getEmail());
            resultset = psGetProducts.executeQuery();

            // read the data from the result set and add the data into the list
            if (resultset.isBeforeFirst()) {
                while (resultset.next()) {
                    pieChartData.add(new PieChart.Data(resultset.getString("name"), resultset.getInt("numberOfSales")));
                    totalNumOfSales += resultset.getInt("numberOfSales");
                }
            } else {
                System.out.println("No products");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            if (resultset != null) {
                try {
                    resultset.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psGetProducts != null) {
                try {
                    psGetProducts.close();
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

        // display the pie chart in a fade-in and slide-up animation
        pieChart.setTitle("Number of Sales");
        pieChart.setData(pieChartData);
        TranslateTransition translate = new TranslateTransition(Duration.seconds(1), pieChart);
        translate.setFromY(pieChart.getLayoutY() + 10);
        translate.setToY(pieChart.getLayoutY());
        translate.play();
        FadeTransition fadeInPieChart = new FadeTransition(Duration.seconds(1), pieChart);
        fadeInPieChart.setFromValue(0);
        fadeInPieChart.setToValue(1);
        fadeInPieChart.play();

        // display the bar chart in a fade-in and slide-up animation
        TranslateTransition translateBar = new TranslateTransition(Duration.seconds(1.5), barChart);
        translateBar.setFromY(barChart.getLayoutY() + 10);
        translateBar.setToY(barChart.getLayoutY());
        translateBar.play();
        FadeTransition fadeInBarChart = new FadeTransition(Duration.seconds(1.5), barChart);
        fadeInBarChart.setFromValue(0);
        fadeInBarChart.setToValue(1);
        fadeInBarChart.play();

        // set up the property for the label
        percentageLabel.setTextFill(Color.BLACK);
        percentageLabel.setStyle("-fx-font: 15 arial;");

        // set event handler for every segment of the pie chart
        for (final PieChart.Data data : pieChart.getData()) {
            int finalTotalNumOfSales = totalNumOfSales;
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                // if segment clicked,
                // locate the percentage label on mouse clicked
                percentageLabel.setTranslateX(event.getSceneX() - percentageLabel.getLayoutX());
                percentageLabel.setTranslateY(event.getSceneY() - percentageLabel.getLayoutY());

                // set up the percentage value
                percentageLabel.setText(String.format("%.2f%%", data.getPieValue() * 100 / finalTotalNumOfSales));

                // display the label in a fade in transition
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(.6), percentageLabel);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();

                // retrieve the rating of that particular product
                Connection connection1 = null;
                PreparedStatement psGetRating = null;
                ResultSet resultSet = null;

                try {
                    // search for the specific product in the database
                    DatabaseConnection databaseConnection = new DatabaseConnection();
                    connection1 = databaseConnection.getConnection();
                    psGetRating = connection1.prepareStatement("SELECT * FROM product_info WHERE name = ? AND sellerEmail = ?");
                    psGetRating.setString(1, data.getName());
                    psGetRating.setString(2, Seller.getEmail());
                    resultSet = psGetRating.executeQuery();

                    if (resultSet.next()) {
                        // store the product's ratings in the ratings array
                        if (resultSet.getString("numOfOneStars") == null) {
                            ratings[0] = 0;
                        } else {
                            ratings[0] = Integer.parseInt(resultSet.getString("numOfOneStars"));
                        }
                        if (resultSet.getString("numOfTwoStars") == null) {
                            ratings[1] = 0;
                        } else {
                            ratings[1] = Integer.parseInt(resultSet.getString("numOfTwoStars"));
                        }
                        if (resultSet.getString("numOfThreeStars") == null) {
                            ratings[2] = 0;
                        } else {
                            ratings[2] = Integer.parseInt(resultSet.getString("numOfThreeStars"));
                        }
                        if (resultSet.getString("numOfFourStars") == null) {
                            ratings[3] = 0;
                        } else {
                            ratings[3] = Integer.parseInt(resultSet.getString("numOfFourStars"));
                        }
                        if (resultSet.getString("numOfFiveStars") == null) {
                            ratings[4] = 0;
                        } else {
                            ratings[4] = Integer.parseInt(resultSet.getString("numOfFiveStars"));
                        }
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
                    if (psGetRating != null) {
                        try {
                            psGetRating.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection1 != null) {
                        try {
                            connection1.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // update the bar graph
                updateBarChart(ratings);
            });
        }
    }

    /**
     * This method is used to update the bar chart whenever it is called
     *
     * @param ratings an array which stores the number of ratings for each category
     *                index 0 -> one-star rating
     *                index 1 -> two-star rating
     *                index 2 -> three-star rating
     *                index 3 -> four-star rating
     *                index 4 -> five-star rating
     * @author XiangLun
     */
    private void updateBarChart(int[] ratings) {
        // clear the data displayed before
        barChart.getData().clear();
        barChart.layout();

        // create a series for the bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // pass-in the data
        series.getData().add(new XYChart.Data<>("1", ratings[0]));
        series.getData().add(new XYChart.Data<>("2", ratings[1]));
        series.getData().add(new XYChart.Data<>("3", ratings[2]));
        series.getData().add(new XYChart.Data<>("4", ratings[3]));
        series.getData().add(new XYChart.Data<>("5", ratings[4]));

        // update the bar chart
        barChart.getData().add(series);
    }
}
