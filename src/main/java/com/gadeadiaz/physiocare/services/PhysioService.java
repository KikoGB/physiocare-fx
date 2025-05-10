package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Appointment;
import com.gadeadiaz.physiocare.models.Physio;
import com.gadeadiaz.physiocare.requests.PhysioPOSTRequest;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PhysioService {
    private static final Gson gson = new Gson();

    public static CompletableFuture<List<Physio>> getPhysios(String specialtyText) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "physios" +
                        (specialtyText.isEmpty()? "":"/find?specialty=" + specialtyText),
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Physio>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Physio> getPhysioById(int id) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "physios",
                null,
                "GET"
        ).thenApply(response -> gson.fromJson(response, Physio.class));
    }

    public static CompletableFuture<List<Appointment>> getPhysioAppointments(int id)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "physios/" + id + "/appointments",
                null,
                "GET"
        ).thenApply(response -> {
            Type listType = new TypeToken<List<Appointment>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Physio> create(PhysioPOSTRequest physioPOSTRequest)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "physios",
                gson.toJson(physioPOSTRequest),
                "POST"
        ).thenApply(response -> gson.fromJson(response, Physio.class));
    }

    public static CompletableFuture<Void> deletePhysio(int id) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "physios/" + id,
                null,
                "DELETE"
        ).thenApply(null);
    }
}
