package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.models.auth.AuthResponse;
import com.gadeadiaz.physiocare.models.auth.LoginRequest;
import com.gadeadiaz.physiocare.utils.Message;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.gadeadiaz.physiocare.utils.ServiceUtils.getResponse;

public class LoginService {
    public static boolean login(String login, String password) {

        try {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()  // Solo serializa los campos @Expose
                    .create();
            String credentials = gson.toJson(new LoginRequest(login, password));
            String jsonResponse = getResponse(ServiceUtils.SERVER + "auth/login", credentials, "POST");

            AuthResponse authResponse = new Gson().fromJson(jsonResponse, AuthResponse.class);
            if (authResponse != null && authResponse.isOk()) {
                ServiceUtils.setToken(authResponse.getToken());
                return true;
            }
            if (authResponse != null && !authResponse.isOk()){
                Message.apiErrorResponse(authResponse.getError());
            }

        } catch (Exception e) {
            System.out.println("Error De login: " + e.getMessage());
            Message.showError("Login", e.getMessage());
        }
        return false;
    }

}
