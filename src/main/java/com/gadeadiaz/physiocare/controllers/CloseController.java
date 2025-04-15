package com.gadeadiaz.physiocare.controllers;

import javafx.stage.Stage;

/**
 * Interface to assist controllers in handling window closing actions,
 * typically used to return to the previous window or view.
 */
public interface CloseController {

    /**
     * Sets a listener that will handle closing the current stage.
     *
     * @param stage the current JavaFX stage to be closed
     */
    void setOnCloseListener(Stage stage);

    /**
     * Assigns the current stage to the controller.
     *
     * @param stage the current JavaFX stage
     */
    void setStage(Stage stage);
}
