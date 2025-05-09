package com.gadeadiaz.physiocare.controllers;

import com.gadeadiaz.physiocare.controllers.interfaces.AddMedicalRecordListener;
import com.gadeadiaz.physiocare.controllers.interfaces.DetailListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RecordItemController {
    @FXML
    private Label lblRecordPatient;
    @FXML
    private Label lblMedicalRecord;
    @FXML
    private Button btnDetail;
    @FXML
    private Button btnAddMedicalRecord;

    private int recordId;
    private DetailListener detailListener;
    private AddMedicalRecordListener addMedicalRecordListener;

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setDetailListener(DetailListener detailListener) {
        this.detailListener = detailListener;
    }

    public void setAddMedicalRecordListener(AddMedicalRecordListener addMedicalRecordListener) {
        this.addMedicalRecordListener = addMedicalRecordListener;
    }

    public void setLblRecordPatientText(String patientFullName) {
        this.lblRecordPatient.setText("Expediente de " + patientFullName);
    }

    public void setLblMedicalRecordText(String medicalRecord) {
        this.lblMedicalRecord.setText(medicalRecord);
    }

    public void setBtnAddMedicalRecordVisibility(boolean isVisible) {
        this.btnAddMedicalRecord.setVisible(isVisible);
    }

    public void detailClick() {
        detailListener.onDetailClick(recordId);
    }

    public void addMedicalRecord() {
        addMedicalRecordListener.onAddMedicalRecordClick(recordId);
    }
}
