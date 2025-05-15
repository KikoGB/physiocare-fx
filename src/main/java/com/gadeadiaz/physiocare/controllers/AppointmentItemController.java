package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.callbacks.AcceptAppointmentCallback;
import com.gadeadiaz.physiocare.callbacks.DenyAppointmentCallback;
import com.gadeadiaz.physiocare.callbacks.ShowDetailCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AppointmentItemController {
    @FXML
    private ImageView ivAppointmentItem;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblDiagnosis;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnDeny;
    @FXML
    private HBox hBoxPhysio;
    @FXML
    private ImageView ivAvatarPhysio;
    @FXML
    private Label lblPhysioName;

    private int appointmentId;

    private ShowDetailCallback showDetailCallback;
    private AcceptAppointmentCallback acceptAppointmentCallback;
    private DenyAppointmentCallback denyAppointmentCallback;


    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setShowDetailCallback(ShowDetailCallback showDetailCallback) {
        this.showDetailCallback = showDetailCallback;
    }

    public void setAcceptAppointmentCallback(AcceptAppointmentCallback acceptAppointmentCallback) {
        this.acceptAppointmentCallback = acceptAppointmentCallback;
    }

    public void setDenyAppointmentCallback(DenyAppointmentCallback denyAppointmentCallback) {
        this.denyAppointmentCallback = denyAppointmentCallback;
    }

    public void setIvAppointmentItemImage(Image image) {
        this.ivAppointmentItem.setImage(image);
    }

    public void setLblDateText(String text) {
        this.lblDate.setText(text);
    }

    public void setLblDiagnosisText(String text) {
        this.lblDiagnosis.setText(text);
    }

    public Button getBtnAcceptAppointmentItem() {
        return btnAccept;
    }

    public Button getBtnDenyAppointmentItem() {
        return btnDeny;
    }

    public HBox getHBoxPhysio() {
        return hBoxPhysio;
    }

    public void setIvAvatarPhysioImage(Image image) {
        ivAvatarPhysio.setImage(image);
    }

    public void setLblPhysioNameText(String text) {
        this.lblPhysioName.setText(text);
    }

    public void appointmentClick() {
        showDetailCallback.showDetail(appointmentId);
    }

    public void acceptAppointment() {
        acceptAppointmentCallback.acceptAppointment(appointmentId);
    }

    public void denyAppointment() {
        denyAppointmentCallback.denyAppointment(appointmentId);
    }
}
