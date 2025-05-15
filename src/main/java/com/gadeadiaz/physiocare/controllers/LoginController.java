package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.services.LoginService;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.gadeadiaz.physiocare.utils.SceneLoader;
import com.gadeadiaz.physiocare.utils.Validations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller responsible for handling all actions and data
 * related to the login window of the application.
 * <p>
 * Implements {@link CloseController} to support clean window closing logic.
 */
public class LoginController implements Initializable, CloseController {

    /** Text field for entering the user's login/username. */
    @FXML
    private TextField txtLogin;

    /** Text field for entering the user's password. */
    @FXML
    private TextField txtPassword;

    /** Gson instance for JSON parsing (currently unused). */
    private Gson gson = new Gson();

    /**
     * Called automatically after the FXML is loaded.
     * Used to initialize any required UI logic.
     *
     * @param url not used
     * @param resourceBundle not used
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic can go here
    }

    /**
     * Triggered when the login button is clicked.
     *
     * @param actionEvent the event object containing source info
     * @throws IOException if screen loading fails
     */
    @FXML
    public void click(ActionEvent actionEvent) throws IOException {
        login(actionEvent);
    }

    /**
     * Attempts to log in the user by validating input fields and
     * calling the login service. If successful, navigates to the home screen.
     *
     * @param actionEvent the triggering event used to obtain the current window
     * @throws IOException if the home screen fails to load
     */
    private void login(ActionEvent actionEvent) throws IOException {
        String login = txtLogin.getText();
        String password = txtPassword.getText();

        // Temporary hardcoded credentials (for testing/demo purposes)
        // Admin credentials
        login = "admin";
        password = "1234567";

        // Physio credentials
//        login = "Sergi";
//        password = "1234567";

        if (Validations.validateLogin(login) && Validations.validatePassword(password)) {
            if (LoginService.login(login, password)) {
                showHome(actionEvent);
            }
        }
    }

    /**
     * Loads the main application menu (home screen).
     *
     * @param event the event used to obtain the current stage
     * @throws IOException if the FXML file cannot be loaded
     */
    private void showHome(ActionEvent event) throws IOException {
        SceneLoader.loadScreen("home.fxml",
                (Stage)((Node) event.getSource()).getScene().getWindow());
    }

    /**
     * Assigns a close request listener to the stage that closes the window.
     *
     * @param stage the current JavaFX stage
     */
    @Override
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(e -> stage.close());
    }

    /**
     * Stores or initializes the JavaFX stage if needed.
     * Currently unused.
     *
     * @param stage the current JavaFX stage
     */
    @Override
    public void setStage(Stage stage) {
        // No-op
    }

    /**
     * Handles mouse click actions on buttons.
     * Currently a placeholder with no implementation.
     *
     * @param mouseEvent the mouse event
     */
    public void handleButtonAction(MouseEvent mouseEvent) {
        // No-op
    }
}
