package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.controllers.interfaces.UserDeleteListener;
import com.gadeadiaz.physiocare.controllers.interfaces.UserDetailListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserItemController {
    @FXML
    private Label lblAttribute1;
    @FXML
    private Label lblAttribute2;
    @FXML
    private Label lblAttribute3;
    @FXML
    private Label lblAttribute4;
    @FXML
    private Label lblAttribute5;

    private String userId;

    private UserDetailListener detailListener; //Reference to the father (home) that receive the click
    private UserDeleteListener deleteListener;

    //Setter that was called from the father controller
    public void setUserId(String userId) {
        this.userId = userId;
    }

    //Setter to the listener to registrate who receives the callback
    public void setDetailListener(UserDetailListener detailListener) {
        this.detailListener = detailListener;
    }

    public void setDeleteListener(UserDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setLblAttribute1(String name) {
        this.lblAttribute1.setText(name);
    }

    public void setLblAttribute2(String surname) {
        this.lblAttribute2.setText(surname);
    }

    public void setLblAttribute3(String birthDate) {
        this.lblAttribute3.setText(birthDate);
    }

    public void setLblAttribute4(String address) {
        this.lblAttribute4.setText(address);
    }

    public void setLblAttribute5(String insuranceNumber) {
        this.lblAttribute5.setText(insuranceNumber);
    }

    public void detailClick(ActionEvent actionEvent) {
        //If we have click listener on and a user id. We warn to the father
        detailListener.onUserDetailClick(userId); //Returns the user id to the father
    }

    public void deleteClick(ActionEvent actionEvent) {
        //If we have click listener on and a user id. We warn to the father
        deleteListener.onUserDeletetClick(userId); //Returns the user id to the father
    }
}
