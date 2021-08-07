package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

public class ResponseEntregaEspecial {

    @Expose
    private Boolean success;

    @Expose
    private Data data;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseEntregaEspecial{" +
                "success=" + success +
                ", data=" + data +
                '}';
    }
}

