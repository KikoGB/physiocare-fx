package com.gadeadiaz.physiocare.models.patient;

import com.gadeadiaz.physiocare.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  Class PatientListResponse Represents an API response containing a list of patients.
 */
public class PatientListResponse extends BaseResponse {
    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }
}
