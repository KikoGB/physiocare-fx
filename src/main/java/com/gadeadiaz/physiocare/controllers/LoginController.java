package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.services.LoginService;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
public class LoginController implements Initializable, CloseController{

    private Stage stage;

    @FXML
    private AnchorPane sideBar;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    private Gson gson = new Gson();

    private double x = 0,y = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        sideBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });
    }


    @FXML
    public void loginClick() throws IOException {
        login();
    }

    private void login() throws IOException {
        String login = txtLogin.getText();
        String password = txtPassword.getText();

        // Temporary hardcoded credentials (for testing/demo purposes)
        // Admin credentials
//        login = "admin";
//        password = "1234567";

        // Physio credentials
        login = "Sergi";
        password = "1234567";

        if (Validations.validateLogin(login) && Validations.validatePassword(password)) {
            if (LoginService.login(login, password)) {
                showHome();
            }
        }
    }


    private void showHome() {
        try {
            SceneLoader.loadScreen("home.fxml", new Stage(), false);
            this.stage.close();
        } catch (IOException e) {
            System.out.println("Show home error");
        }
    }

    @FXML
    void closeProgram() {
        if (stage != null) {
            stage.close();
        }
    }

    @Override
    public void setOnCloseListener(Stage stage) {
        stage.setOnCloseRequest(Event::consume);
    }


    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
