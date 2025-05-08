package com.gadeadiaz.physiocare.models.appointment;

import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.BaseResponse;

/**
 * Class AppointmentResponse  Represents a API response object that contains the details of an appointment.
 */
public class AppointmentResponse extends BaseResponse {
    private Appointment result;

    public Appointment getResult() {
        return result;
    }
}
