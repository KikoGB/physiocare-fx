package com.gadeadiaz.physiocare.utils;

import javafx.util.Pair;

public class Storage {
    private static Storage INSTANCE;
    // Representa el Token y el Rol del usuario logeado
    private Pair<String, String> userdata = new Pair<>("", "");

    private Storage() {}

    public static Storage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Storage();
        }

        return INSTANCE;
    }

    public Pair<String, String> getUserdata() {
        return userdata;
    }

    public void setUserdata(String token, String rol) {
        this.userdata = new Pair<>(token, rol);
    }
}
