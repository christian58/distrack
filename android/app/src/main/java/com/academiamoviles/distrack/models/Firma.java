package com.academiamoviles.distrack.models;

import io.realm.RealmObject;

/**
 * Created by maacs on 12/10/2017.
 */

public class Firma extends RealmObject {
    private String url;
    private String nombre;
    private String documentopedido;
    private Boolean sincronizar = false;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumentopedido() {
        return documentopedido;
    }

    public void setDocumentopedido(String documentopedido) {
        this.documentopedido = documentopedido;
    }

    public Boolean getSincronizar() {
        return sincronizar;
    }

    public void setSincronizar(Boolean sincronizar) {
        this.sincronizar = sincronizar;
    }
}
