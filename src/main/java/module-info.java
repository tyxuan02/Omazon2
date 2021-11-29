module com.example.omazonproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires mysql.connector.java;
    requires java.sql;

    opens com.example.omazonproject to javafx.fxml;
    exports com.example.omazonproject;
}