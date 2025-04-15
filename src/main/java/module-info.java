module com.gadeadiaz.physiocare {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.jfoenix;


    opens com.gadeadiaz.physiocare to javafx.fxml;
    opens com.gadeadiaz.physiocare.models;
    //So that gson can serialize adn  deserialize clases
    opens com.gadeadiaz.physiocare.models.auth to com.google.gson;
    opens com.gadeadiaz.physiocare.models.patient to com.google.gson;
    opens com.gadeadiaz.physiocare.models.physio to com.google.gson;
    opens com.gadeadiaz.physiocare.models.appointment to com.google.gson;

    opens com.gadeadiaz.physiocare.utils;
    exports com.gadeadiaz.physiocare;
    opens com.gadeadiaz.physiocare.models.record;
    opens com.gadeadiaz.physiocare.controllers to javafx.fxml;


}
