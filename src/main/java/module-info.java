module com.example.beautysalon {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.beautysalon to javafx.fxml;
    opens com.example.beautysalon.controllers to javafx.fxml;
    opens com.example.beautysalon.models to javafx.base;
    exports com.example.beautysalon;
    exports com.example.beautysalon.controllers;
    exports com.example.beautysalon.models;
    exports com.example.beautysalon.services;
}
