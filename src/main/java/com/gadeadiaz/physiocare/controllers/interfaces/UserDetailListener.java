package com.gadeadiaz.physiocare.controllers.interfaces;

/**
 * Listener interface for handling user detail click events.
 * <p>
 * This interface should be implemented by any class that needs
 * to respond to clicks on a user's detail item (e.g., in a list or card).
 */
public interface UserDetailListener {

    /**
     * Called when a user detail item is clicked.
     *
     * @param idPatient the unique identifier of the patient whose details were clicked
     */
    void onUserDetailClick(String idPatient);
}
