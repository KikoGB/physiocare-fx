package com.gadeadiaz.physiocare.models.appointment;

import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.BaseResponse;

import java.util.List;

/**
 * Class Appointment List Response represents an API List response of appointments for a medical record
 */
public class AppointmentListResponse extends BaseResponse {
    private List<Appointment> result;

    public List<Appointment> getResult() {
        return result;
    }
}
