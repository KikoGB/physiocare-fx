package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.ErrorResponse;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PatientService {
    private static final Gson gson = new Gson();

    // Explicar que cuando lo que devuelve el servidor es un array se debe controlar con try/catch, pero cuando
    // sea un solo objeto se hace mirando si las propiedades de ErrorResponse son null o no
    public static CompletableFuture<List<Patient>> getPatients(String surnameText)
            throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients" + (surnameText.isEmpty()? "":"/find?" + surnameText),
                null,
                "GET"
        ).thenApply(response -> {
            try {
                ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
                throw new RequestErrorException(errorResponse);
            } catch (JsonSyntaxException _) {}
            Type listType = new TypeToken<List<Patient>>() {}.getType();
            return gson.fromJson(response, listType);
        });
    }

    public static CompletableFuture<Patient> getPatientById(int id) throws RequestErrorException {
        return ServiceUtils.getResponseAsync(
                ServiceUtils.SERVER + "patients/" + id,
                null,
                "GET"
        ).thenApply(response -> {
            ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
            if (errorResponse.getMessage() != null) {
                throw new RequestErrorException(errorResponse);
            }

            return gson.fromJson(response, Patient.class);
        });
    }
}
