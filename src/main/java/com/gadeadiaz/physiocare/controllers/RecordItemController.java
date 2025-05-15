package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.callbacks.ShowRecordFormCallback;
import com.gadeadiaz.physiocare.callbacks.ShowDetailCallback;
import com.gadeadiaz.physiocare.models.Record;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RecordItemController {
    @FXML
    private Label lblRecordPatient;
    @FXML
    private Label lblMedicalRecord;
    @FXML
    private Button btnAddMedicalRecord;

    private Record record;

    private ShowDetailCallback detailListener;
    private ShowRecordFormCallback showRecordFormCallback;

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setDetailListener(ShowDetailCallback detailListener) {
        this.detailListener = detailListener;
    }

    public void setShowRecordFormCallback(ShowRecordFormCallback showRecordFormCallback) {
        this.showRecordFormCallback = showRecordFormCallback;
    }

    public void setLblRecordPatientText(String patientFullName) {
        this.lblRecordPatient.setText("Expediente de " + patientFullName);
    }

    public void setLblMedicalRecordText(String text) {
        this.lblMedicalRecord.setText(text);
    }

    public void setBtnAddMedicalRecordVisibility(boolean isVisible) {
        this.btnAddMedicalRecord.setVisible(isVisible);
    }

    public void showRecordDetail() {
        detailListener.showDetail(record.getId());
    }

    public void showRecordForm() {
        showRecordFormCallback.showRecordForm(record);
    }
}
