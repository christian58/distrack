package com.academiamoviles.distrack.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by maacs on 29/09/2017.
 */

public class Foto extends RealmObject {
    private String url;
    private String idpedido;
    private String nombre;
    private String documentopedido;
    private Boolean sincronizar = false;

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    private Boolean cerrado = false;

    public Foto() {
    }

    public String getDocumentopedido() {
        return documentopedido;
    }

    public void setDocumentopedido(String documentopedido) {
        this.documentopedido = documentopedido;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public Boolean getSincronizar() {
        return sincronizar;
    }

    public void setSincronizar(Boolean sincronizar) {
        this.sincronizar = sincronizar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Foto{" +
                "url='" + url + '\'' +
                ", idpedido='" + idpedido + '\'' +
                '}';
    }
}
