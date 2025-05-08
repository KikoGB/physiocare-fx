package com.gadeadiaz.physiocare.models.user;

import com.gadeadiaz.physiocare.models.BaseResponse;
import com.gadeadiaz.physiocare.models.User;

/**
 *  * Represents an API response object containing user data.
 */
public class UserResponse extends BaseResponse {
    private User result;

    public User getResult(){
        return result;
    }

}
