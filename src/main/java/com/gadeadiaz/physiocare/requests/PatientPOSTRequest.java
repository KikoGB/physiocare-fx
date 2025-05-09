package com.gadeadiaz.physiocare.requests;

import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.models.User;

public class PatientPOSTRequest {
    private User user;
    private Patient patient;

    public PatientPOSTRequest(User user, Patient patient) {
        this.user = user;
        this.patient = patient;
    }
}
