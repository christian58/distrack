package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Motivo extends RealmObject {
    @Expose
    private String label;

    @PrimaryKey
    @Expose
    private String codigo;

    @Expose
    private String finaliza;

    public String getFinaliza() {
        return finaliza;
    }

    public void setFinaliza(String finaliza) {
        this.finaliza = finaliza;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "Motivo{" +
                "label='" + label + '\'' +
                ", codigo='" + codigo + '\'' +
                ", finaliza='" + finaliza + '\'' +
                '}';
    }
}
