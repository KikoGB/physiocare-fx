package com.gadeadiaz.physiocare.callbacks;

/**
 * Listener interface for handling user deletion events.
 * <p>
 * Classes that implement this interface can respond to
 * user delete actions, typically from UI elements like buttons.
 */
public interface DeleteCallback {
    void delete(int id);
}
