package com.gadeadiaz.physiocare.models;

import java.util.List;

public class Record {
    private int id;
    private String medicalRecord;
    private String patient;
    private List<Appointment> appointments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public String toString() {
        return "Record -> " + id + ", " + patient + ", " + medicalRecord + ", " + appointments;
    }
}
