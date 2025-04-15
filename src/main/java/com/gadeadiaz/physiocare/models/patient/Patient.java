package com.gadeadiaz.physiocare.models.patient;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 *  Class Patient Represents a patient entity with personal and contact information.
 */
public class Patient {
    @SerializedName("_id")
    private String id;
    private String name;
    private String surname;
    private Date birthDate;
    private String address;
    private String insuranceNumber;
    private String email;
    private String image;

    public Patient() {
    }

    public Patient(String id, String name, String surname, Date birthDate, String address, String insuranceNumber, String email, String image) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.address = address;
        this.insuranceNumber = insuranceNumber;
        this.email = email;
        this.image = image;
    }

    public String getId() {
        return id;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Patient -> " + name + " " + surname + ", " + birthDate + ", " + address + ", " + insuranceNumber;
    }
}
