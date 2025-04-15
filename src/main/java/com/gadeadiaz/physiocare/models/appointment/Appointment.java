package com.gadeadiaz.physiocare.models.appointment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Class Appointment represents an appointment for a user's Medical Record
 */
public class Appointment {
    @SerializedName("_id")
    private String id;
    private Date date;
    private String diagnosis;
    private String treatment;
    private String observations;

    public Appointment(String id, Date date, String diagnosis, String treatment, String observations) {
        this.id = id;
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observations = observations;
    }

    public Appointment(Date date, String diagnosis, String treatment, String observations) {
        this.date = date;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.observations = observations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public String toString() {
        return "Appointment -> " + id + ", " + date + ", " + diagnosis + ", " + treatment + ", " + observations;
    }
}
