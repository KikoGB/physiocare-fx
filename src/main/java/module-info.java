module com.gadeadiaz.physiocare {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires kernel;
    requires layout;
    requires java.desktop;
    requires jsch;
    requires org.apache.commons.net;
    requires io.github.cdimascio.dotenv.java;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.client.auth;
    requires google.api.client;
    requires com.google.api.services.gmail;
    requires com.google.api.client.extensions.java6.auth;
    requires jakarta.mail;
    requires com.google.api.client.extensions.jetty.auth;


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
