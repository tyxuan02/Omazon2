module com.example.omazonproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires mysql.connector.java;
    requires java.sql;
    requires json.simple;
    requires java.mail;
    requires commons.io;
    requires activation;
    requires javafx.swing;
    requires org.controlsfx.controls;

    opens com.example.omazonproject to javafx.fxml;
    exports com.example.omazonproject;
}