package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Estado extends RealmObject {

    @PrimaryKey
    private Integer id;

    @Expose
    private String estado;
    @Expose
    private RealmList<Motivo> motivos = null;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public RealmList<Motivo> getMotivos() {
        return motivos;
    }

    public void setMotivos(RealmList<Motivo> motivos) {
        this.motivos = motivos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
