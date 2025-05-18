package com.gadeadiaz.physiocare.services;

import com.gadeadiaz.physiocare.responses.LoginResponse;
import com.gadeadiaz.physiocare.requests.LoginRequest;
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

            LoginResponse authResponse = gson.fromJson(response, LoginResponse.class);
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
                Message.showError("Invalid Credentials", "Please enter a valid username and password.");
            }
        } catch (Exception e) {
            Message.showError("Invalid Credentials", "Please enter a valid username and password.");
        }
        return false;
    }

}
