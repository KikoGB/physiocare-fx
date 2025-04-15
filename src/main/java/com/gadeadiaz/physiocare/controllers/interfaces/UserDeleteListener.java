package com.gadeadiaz.physiocare.controllers.interfaces;

/**
 * Listener interface for handling user deletion events.
 * <p>
 * Classes that implement this interface can respond to
 * user delete actions, typically from UI elements like buttons.
 */
public interface UserDeleteListener {
    void onUserDeletetClick(String idPatient);
}
