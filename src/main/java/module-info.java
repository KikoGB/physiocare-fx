module com.gadeadiaz.physiocare {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.jfoenix;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires jsch;
    requires org.apache.commons.net;
    requires io.github.cdimascio.dotenv.java;


    opens com.gadeadiaz.physiocare to javafx.fxml;
    exports com.gadeadiaz.physiocare;
    opens com.gadeadiaz.physiocare.models;
    exports com.gadeadiaz.physiocare.models;
    //So that gson can serialize adn  deserialize clases
    opens com.gadeadiaz.physiocare.utils;
    opens com.gadeadiaz.physiocare.controllers to javafx.fxml;
    opens com.gadeadiaz.physiocare.responses;
    opens com.gadeadiaz.physiocare.requests to com.google.gson;


}
