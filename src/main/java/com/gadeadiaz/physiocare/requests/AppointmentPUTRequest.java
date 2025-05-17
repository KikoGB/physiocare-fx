package com.gadeadiaz.physiocare.requests;

public class AppointmentPUTRequest {
    private int id;
    private String date;
    private String diagnosis;
    private String treatment;
    private String observations;
    private int patientId;
    private int physioId;

    public AppointmentPUTRequest(int id, String date, String diagnosis, String treatment,
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
