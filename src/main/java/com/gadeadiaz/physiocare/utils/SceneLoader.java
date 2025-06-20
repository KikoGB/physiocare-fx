package com.gadeadiaz.physiocare.utils;

import com.gadeadiaz.physiocare.Application;
import com.gadeadiaz.physiocare.controllers.CloseController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Utility class responsible for loading and displaying FXML-based scenes.
 * This class provides a method to load a given FXML file, create a new scene,
 * and display it in the provided stage.
 */
public class SceneLoader {

    public static void loadScreen(String viewPath, Stage stage, boolean transparent)
            throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(Application.class.getResource(viewPath));
        Parent root = loader.load();

        // Create a new scene and set it in the stage
        Scene viewScene = new Scene(root);
        if (transparent) {
            viewScene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
        }
        stage.setScene(viewScene);

        stage.setTitle("PHYSIOCARE");
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();

        // Get the controller from the loaded FXML and set up the window close listener
        CloseController controller = loader.getController();
        controller.setStage(stage);
        controller.setOnCloseListener(stage);
    }
}
