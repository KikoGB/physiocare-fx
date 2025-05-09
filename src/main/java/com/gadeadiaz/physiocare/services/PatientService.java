package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.exceptions.RequestErrorException;
import com.gadeadiaz.physiocare.models.Patient;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

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
                ServiceUtils.SERVER + "patients" +
                        (surnameText.isEmpty()? "":"/find?surname" + surnameText),
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
}
