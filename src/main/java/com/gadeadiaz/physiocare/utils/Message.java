package com.gadeadiaz.physiocare.utils;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

/**
 * Class Message represents an alert message to inform the user with customize messages
 */
public class Message {

    public static void showMessage(Alert.AlertType type, String title, String header, String message){
        Alert dialog = new Alert(type);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.showAndWait();
    }

    public static void showError(String header, String message){
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        dialog.getDialogPane().setPrefSize(400, 300);
        dialog.setTitle("Error");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.showAndWait();
    }

    public static void apiErrorResponse(String message) {
        showMessage(
                Alert.AlertType.ERROR,
                "Error",
                "Invalid request!",
                message
        );
    }

}
