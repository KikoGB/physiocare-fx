package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.models.auth.AuthResponse;
import com.gadeadiaz.physiocare.models.auth.LoginRequest;
import com.gadeadiaz.physiocare.utils.Message;
import com.gadeadiaz.physiocare.utils.ServiceUtils;
import com.gadeadiaz.physiocare.utils.Storage;
import com.google.gson.Gson;

import static com.gadeadiaz.physiocare.utils.ServiceUtils.getResponse;

public class LoginService {
    public static boolean login(String username, String password) {
        try {
            Gson gson = new Gson();
            String response = getResponse(
                    ServiceUtils.SERVER + "auth/login",
                    gson.toJson(new LoginRequest(username, password)),
                    "POST"
            );

            AuthResponse authResponse = gson.fromJson(response, AuthResponse.class);
            Storage storage = Storage.getInstance();
            if (!(authResponse.getToken().isEmpty() || authResponse.getRol().isEmpty())) {
                if (authResponse.getRol().equals("patient")) {
                    Message.showError(
                            "Patient trying to login",
                            "Patients can not login in this app"
                    );
                } else {
                    storage.setUserdata(authResponse.getToken(), authResponse.getRol());
                    return true;
                }
            } else {
                Message.showError("Credenciales invalidas", "Credenciales invalidas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Message.showError(
                    "Unexpected error", "An unexpected error has happened, please try again."
            );
        }
        return false;
    }

}
