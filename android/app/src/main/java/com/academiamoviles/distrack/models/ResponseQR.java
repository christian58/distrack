package com.academiamoviles.distrack.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseQR {
    @Expose
    private Boolean success;
    @Expose
    private Tarea pedido;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Tarea getPedido() {
        return pedido;
    }

    public void setPedido(Tarea pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "ResponseQR{" +
                "success=" + success +
                ", pedido=" + pedido +
                '}';
    }
}
