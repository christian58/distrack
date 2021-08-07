package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maacs on 04/10/2017.
 */

public class ResponsePedidosPendientes {
    @Expose
    private Boolean success;
    @Expose
    private List<Tarea> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Tarea> getData() {
        return data;
    }

    public void setData(List<Tarea> data) {
        this.data = data;
    }
}
