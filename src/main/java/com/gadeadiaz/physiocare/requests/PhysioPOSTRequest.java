package com.gadeadiaz.physiocare.requests;

import com.gadeadiaz.physiocare.models.Physio;
import com.gadeadiaz.physiocare.models.User;

public class PhysioPOSTRequest {
    private User user;
    private Physio physio;

    public PhysioPOSTRequest(User user, Physio physio) {
        this.user = user;
        this.physio = physio;
    }
}
