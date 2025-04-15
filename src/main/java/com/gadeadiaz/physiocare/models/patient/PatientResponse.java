package com.gadeadiaz.physiocare.models.patient;

import com.gadeadiaz.physiocare.models.BaseResponse;

/**
 *  Class PatientResponse represents a API response object that contains the details of a single patient.
 */
public class PatientResponse extends BaseResponse {
    private Patient result;

    public Patient getResult() {
        return result;
    }
}
