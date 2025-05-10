package com.gadeadiaz.physiocare;

import javafx.stage.Stage;
import com.gadeadiaz.physiocare.utils.SceneLoader;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneLoader.loadScreen("login.fxml", stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
