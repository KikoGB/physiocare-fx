package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.controllers.interfaces.AcceptAppointmentCallback;
import com.gadeadiaz.physiocare.controllers.interfaces.AppointmentDetailListener;
import com.gadeadiaz.physiocare.controllers.interfaces.DenyAppointmentCallback;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class AppointmentItemController {
    @FXML
    private ImageView icAppointmentItem;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblDiagnosis;
    @FXML
    private Button btnAccept;
    @FXML
    private Button btnDeny;
    @FXML
    private HBox hboxPhysio;
    @FXML
    private ImageView avatarPhysio;
    @FXML
    private Label lblPhysioName;

    private int appointmentId;

    private AppointmentDetailListener appointmentDetailListener;
    private AcceptAppointmentCallback acceptAppointmentCallback;
    private DenyAppointmentCallback denyAppointmentCallback;


    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAppointmentDetailListener(AppointmentDetailListener appointmentDetailListener) {
        this.appointmentDetailListener = appointmentDetailListener;
    }

    public void setAcceptAppointmentCallback(AcceptAppointmentCallback acceptAppointmentCallback) {
        this.acceptAppointmentCallback = acceptAppointmentCallback;
    }

    public void setDenyAppointmentCallback(DenyAppointmentCallback denyAppointmentCallback) {
        this.denyAppointmentCallback = denyAppointmentCallback;
    }

    public void setIcAppointmentItemImage(Image image) {
        this.icAppointmentItem.setImage(image);
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

    // no hace falta setter de esto en principio
    public HBox getHboxPhysio() {
        return hboxPhysio;
    }

    public void setAvatarPhysio(Image image) {
        avatarPhysio.setImage(image);
    }

    public Label getLblPhysioName() {
        return lblPhysioName;
    }

    public void setLblPhysioName(Label lblPhysioName) {
        this.lblPhysioName = lblPhysioName;
    }

    public void appointmentClick(MouseEvent mouseEvent) {
        appointmentDetailListener.onAppointmentDetailClick(appointmentId);
    }

    public void acceptAppointment() {
        acceptAppointmentCallback.acceptAppointment(appointmentId);
    }

    public void denyAppointment() {
        denyAppointmentCallback.denyAppointment(appointmentId);
    }
}
