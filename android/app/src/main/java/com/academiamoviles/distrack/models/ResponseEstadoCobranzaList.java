package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ResponseEstadoCobranzaList {
    @Expose
    private Boolean success;
    @Expose
    private List<EstadoCobranza> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<EstadoCobranza> getData() {
        return data;
    }

    public void setData(List<EstadoCobranza> data) {
        this.data = data;
    }
}
