package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maacs on 04/10/2017.
 */

public class ResponseRegEstado {
    @Expose
    private String documento;
    @Expose
    private Boolean success;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseRegEstado{" +
                "documento='" + documento + '\'' +
                ", success=" + success +
                '}';
    }
}
