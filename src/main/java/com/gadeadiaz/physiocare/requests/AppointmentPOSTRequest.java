package com.gadeadiaz.physiocare.requests;

public class AppointmentPOSTRequest {
    private String date;
    private String diagnosis;
    private String treatment;
    private String observations;
    private int patientId;
    private int physioId;

    public AppointmentPOSTRequest(String date, String diagnosis, String treatment,
                                  String observations, int patientId, int physioId) {
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observations = observations;
        this.patientId = patientId;
        this.physioId = physioId;
    }
}
