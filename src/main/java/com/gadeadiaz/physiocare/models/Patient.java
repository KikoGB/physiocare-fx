package com.gadeadiaz.physiocare.models;

import java.util.Date;
import java.util.List;

/**
 *  Class Patient Represents a patient entity with personal and contact information.
 */
public class Patient {
    private int id;
    private String name;
    private String surname;
    private String birthdate;
    private String address;
    private String insuranceNumber;
    private String email;
    private String avatar;
    private List<Appointment> appointments;
    private Record record;

    public Patient() {}

    public Patient(String name, String surname, String birthdate, String address, String insuranceNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.birthdate = birthdate;
        this.address = address;
        this.insuranceNumber = insuranceNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInsuranceNumber() {
        return insuranceNumber;
    }

    public void setInsuranceNumber(String insuranceNumber) {
        this.insuranceNumber = insuranceNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthdate=" + birthdate +
                ", address='" + address + '\'' +
                ", insuranceNumber='" + insuranceNumber + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
