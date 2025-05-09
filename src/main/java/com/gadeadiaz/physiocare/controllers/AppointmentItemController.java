package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.controllers.interfaces.AppointmentDetailListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class AppointmentItemController {
    @FXML
    private ImageView imgAppointment;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblDiagnosis;
    @FXML
    private ImageView imgPhysio;
    @FXML
    private Label lblPhysioName;

    private int appointmentId;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public AppointmentDetailListener getAppointmentDetailListener() {
        return appointmentDetailListener;
    }

    public ImageView getImgAppointment() {
        return imgAppointment;
    }

    public void setImgAppointment(ImageView imgAppointment) {
        this.imgAppointment = imgAppointment;
    }

    public Label getLblDate() {
        return lblDate;
    }

    public void setLblDate(Label lblDate) {
        this.lblDate = lblDate;
    }

    public Label getLblDiagnosis() {
        return lblDiagnosis;
    }

    public void setLblDiagnosis(Label lblDiagnosis) {
        this.lblDiagnosis = lblDiagnosis;
    }

    public ImageView getImgPhysio() {
        return imgPhysio;
    }

    public void setImgPhysio(ImageView imgPhysio) {
        this.imgPhysio = imgPhysio;
    }

    public Label getLblPhysioName() {
        return lblPhysioName;
    }

    public void setLblPhysioName(Label lblPhysioName) {
        this.lblPhysioName = lblPhysioName;
    }

    private AppointmentDetailListener appointmentDetailListener;


    public void setAppointmentDetailListener(AppointmentDetailListener appointmentDetailListener) {
        this.appointmentDetailListener = appointmentDetailListener;
    }

    public void appointmentClick(MouseEvent mouseEvent) {
        appointmentDetailListener.onAppointmentDetailClick(appointmentId);
    }
}
