package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

/**
 * Created by maacs on 05/10/2017.
 */

public class ResponseFirma {
    @Expose
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
