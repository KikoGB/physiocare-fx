package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.models.Record;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RecordService {
    private static final Gson gson = new Gson();

    public static CompletableFuture<List<Record>> getRecords(String patientSurnameText) {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "records" +
                        (patientSurnameText.isEmpty()? "":"/find?surname=" + patientSurnameText),
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Record>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Record> getRecordById(int id) {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "records/" + id,
                null,
                "GET"
        ).thenApply(response -> gson.fromJson(response, Record.class));
    }

    public static CompletableFuture<List<Appointment>> getRecordAppointments(int id)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "records/" + id + "/appointments",
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Appointment>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }
}
