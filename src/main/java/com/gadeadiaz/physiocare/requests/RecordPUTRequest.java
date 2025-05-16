package com.gadeadiaz.physiocare.requests;

public class RecordPUTRequest {
    private int id;
    private String medicalRecord;

    public RecordPUTRequest(int id, String medicalRecord) {
        this.id = id;
        this.medicalRecord = medicalRecord;
    }
}
