package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.requests.AppointmentPOSTRequest;
import com.gadeadiaz.physiocare.requests.AppointmentPUTRequest;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AppointmentService {

    private static final Gson gson = new Gson();

    public static CompletableFuture<List<Appointment>> getAppointments()
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "appointments",
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Appointment>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Appointment> getAppointmentById(int id) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "appointments/" + id,
                null,
                "GET"
        ).thenApply(response -> gson.fromJson(response, Appointment.class));
    }

    public static CompletableFuture<Appointment> createAppointment(AppointmentPOSTRequest appointmentPOSTRequest)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "appointments",
                gson.toJson(appointmentPOSTRequest),
                "POST"
        ).thenApply(response -> gson.fromJson(response, Appointment.class));
    }

    public static CompletableFuture<Appointment> updateAppointment(int id, AppointmentPUTRequest appointmentPUTRequest)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "appointments/" + id,
                gson.toJson(appointmentPUTRequest),
                "PUT"
        ).thenApply(response -> gson.fromJson(response, Appointment.class));
    }

    public static CompletableFuture<Void> confirmAppointment(int appointmentId) {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "appointments/" + appointmentId + "/confirm",
                null,
                "PUT"
        ).thenApply(_ -> null);
    }

    public static CompletableFuture<Void> deleteAppointment(int appointmentId) {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "appointments/" + appointmentId,
                null,
                "DELETE"
        ).thenApply(_ -> null);
    }
}
