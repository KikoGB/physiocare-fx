package com.gadeadiaz.physiocare.callbacks;

/**
 * Listener interface for handling user detail click events.
 * <p>
 * This interface should be implemented by any class that needs
 * to respond to clicks on a user's detail item (e.g., in a list or card).
 */
public interface ShowDetailCallback {
    void showDetail(int id);
}
