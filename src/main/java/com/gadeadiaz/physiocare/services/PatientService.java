package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.requests.PatientPOSTRequest;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PatientService {
    private static final Gson gson = new Gson();

    public static CompletableFuture<List<Patient>> getPatients(String surnameText)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients" +
                        (surnameText.isEmpty()? "":"/find?surname=" + surnameText),
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Patient>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<List<Patient>> getPatientsWithAllData()
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients/with-all-data",
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Patient>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Patient> getPatientById(int id) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients/" + id,
                null,
                "GET"
        ).thenApply(response -> gson.fromJson(response, Patient.class));
    }

    public static CompletableFuture<List<Appointment>> getPatientAppointments(int id)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients/" + id + "/appointments",
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Appointment>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Patient> createPatient(PatientPOSTRequest patient)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients",
                gson.toJson(patient),
                "POST"
        ).thenApply(response -> {
            Type listType = new TypeToken<Patient>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Patient> updatePatient(int id, Patient patient)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients/" + id,
                gson.toJson(patient),
                "PUT"
        ).thenApply(response -> {
            Type listType = new TypeToken<Patient>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Void> deletePatient(int id) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients/" + id,
                null,
                "DELETE"
        ).thenApply(_ -> null);
    }
}
