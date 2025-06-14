package com.gadeadiaz.physiocare.models;

import java.util.List;

/**
 * Class Physio represents a physio entity with personal, professional, and contact details.
 */
public class Physio {
    private int id;
    private String name;
    private String surname;
    private String specialty;
    private String licenseNumber;
    private String email;
    private String avatar;
    private List<Appointment> appointments;

    public Physio() {}

    public Physio(String name, String surname, String specialty, String licenseNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
        this.email = email;
    }

    public Physio(int id, String name, String surname, String specialty, String licenseNumber, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.specialty = specialty;
        this.licenseNumber = licenseNumber;
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

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
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

    public String getFullName(){
        return this.getName() + " " + this.getSurname();
    }

    @Override
    public String toString() {
        return getName() + " " + getSurname() + " | " + getLicenseNumber();
    }
}
