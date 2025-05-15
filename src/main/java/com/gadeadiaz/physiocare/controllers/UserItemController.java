package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.callbacks.DeleteCallback;
import com.gadeadiaz.physiocare.callbacks.ShowDetailCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserItemController {
    @FXML
    private Label lblName;
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblLicenseInsuranceNumber;
    @FXML
    private Button btnDelete;

    private int userId;

    private ShowDetailCallback showDetailCallback;
    private DeleteCallback deleteCallback;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setDetailListener(ShowDetailCallback showDetailCallback) {
        this.showDetailCallback = showDetailCallback;
    }

    public void setDeleteListener(DeleteCallback deleteCallback) {
        this.deleteCallback = deleteCallback;
    }

    public void setLblNameText(String text) {
        this.lblName.setText(text);
    }

    public void setLblSurnameText(String text) {
        this.lblSurname.setText(text);
    }

    public void setLblEmailText(String text) {
        this.lblEmail.setText(text);
    }

    public void setLblLicenseInsuranceNumberText(String text) {
        this.lblLicenseInsuranceNumber.setText(text);
    }

    public void setBtnDeleteVisibility(boolean visibility) {
        this.btnDelete.setVisible(visibility);
    }

    public void showUserDetail() {
        showDetailCallback.showDetail(userId);
    }

    public void deleteUser() {
        deleteCallback.delete(userId);
    }
}
