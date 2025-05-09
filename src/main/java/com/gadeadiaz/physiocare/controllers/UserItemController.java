package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.controllers.interfaces.DeleteListener;
import com.gadeadiaz.physiocare.controllers.interfaces.DetailListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
    private Button btnDetailClick;
    @FXML
    private Button btnDeleteClick;

    private int userId;
    private DetailListener detailListener;
    private DeleteListener deleteListener;
    private Stage stage;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setDetailListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

    public void setDeleteListener(DeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
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

    public void setBtnDeleteVisibility(boolean visibility) {
        this.btnDeleteClick.setVisible(visibility);
    }

    public void detailClick() {
        detailListener.onDetailClick(userId);
    }

    public void deleteClick() {
        deleteListener.onDeletetClick(userId);
    }
}
