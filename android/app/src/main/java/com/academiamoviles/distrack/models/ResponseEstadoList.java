package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maacs on 04/10/2017.
 */

public class ResponseEstadoList {
    @Expose
    private Boolean success;

    @Expose
    private List<Estado> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Estado> getData() {
        return data;
    }

    public void setData(List<Estado> data) {
        this.data = data;
    }
}
