package com.gadeadiaz.physiocare.models.patient;

import com.gadeadiaz.physiocare.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  Class PatientListResponse Represents a API response containing a list of patients.
 */
public class PatientListResponse extends BaseResponse {
    @SerializedName("result")
    private List<Patient> result;

    public List<Patient> getPatients() {
        return result;
    }
}
