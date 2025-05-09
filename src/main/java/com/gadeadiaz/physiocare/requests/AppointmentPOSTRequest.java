package com.gadeadiaz.physiocare.requests;

import java.util.Date;

public class AppointmentPOSTRequest {
    private int id;
    private Date date;
    private String diagnosis;
    private String treatment;
    private String observations;
    private int patientId;
    private int physioId;

    public AppointmentPOSTRequest(int id, Date date, String diagnosis, String treatment,
                                  String observations, int patientId, int physioId) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observations = observations;
        this.patientId = patientId;
        this.physioId = physioId;
    }
}
