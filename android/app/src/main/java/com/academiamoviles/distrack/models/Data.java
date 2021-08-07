package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

import io.realm.annotations.PrimaryKey;

public class Data {


    @Expose
    private String estado;
    @Expose
    private String mensaje;





    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    @Override
    public String toString() {
        return "data{" +
                "estado='" + estado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }


}

