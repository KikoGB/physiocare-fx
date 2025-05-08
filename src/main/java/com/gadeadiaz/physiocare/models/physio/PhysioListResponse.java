package com.gadeadiaz.physiocare.models.physio;

import com.gadeadiaz.physiocare.models.BaseResponse;
import com.gadeadiaz.physiocare.models.Physio;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class PhysioListResponse represents an API response containing a list of physios.
 */
public class PhysioListResponse extends BaseResponse {
    @SerializedName("result")
    private List<Physio> result;

    public List<Physio> getPhysios() {
        return result;
    }
}
