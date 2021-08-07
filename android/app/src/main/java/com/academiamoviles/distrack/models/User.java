package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @Expose
    private String id;
    @Expose
    @PrimaryKey
    private Integer idusuario;
    @Expose
    private String usuario;
    @Expose
    private String nombre;
    @Expose
    private Integer max_foto;
    @Expose
    private String organizacion;

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMax_foto() {
        return max_foto;
    }

    public void setMax_foto(Integer max_foto) {
        this.max_foto = max_foto;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", idusuario=" + idusuario +
                ", usuario='" + usuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", max_foto=" + max_foto +
                ", organizacion='" + organizacion + '\'' +
                '}';
    }

}
