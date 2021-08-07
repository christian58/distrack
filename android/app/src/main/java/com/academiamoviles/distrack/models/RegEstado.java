package com.academiamoviles.distrack.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class RegEstado extends RealmObject {
    @SerializedName("documento")
    @Expose
    private String documento;

    @SerializedName("usuario")
    @Expose
    private String usuario;

    @SerializedName("usuario_id")
    @Expose
    private String usuario_id;

    @SerializedName("estado")
    @Expose
    private String estado;


    @SerializedName("motivo")
    @Expose
    private String motivo;

    @SerializedName("cobranza")
    @Expose
    private String cobranza;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("latitud")
    @Expose
    private String latitud;

    @SerializedName("longitud")
    @Expose
    private String longitud;

    @SerializedName("comentario")
    @Expose
    private String comentario;

    @SerializedName("cod_estado")
    @Expose
    private String cod_estado;

    @SerializedName("cod_motivo")
    @Expose
    private String cod_motivo;

    @SerializedName("cod_cobranza")
    @Expose
    private String cod_cobranza;

    public RegEstado() {
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCobranza() {
        return cobranza;
    }

    public void setCobranza(String cobranza) {
        this.cobranza = cobranza;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCod_estado() {
        return cod_estado;
    }

    public void setCod_estado(String cod_estado) {
        this.cod_estado = cod_estado;
    }

    public String getCod_motivo() {
        return cod_motivo;
    }

    public void setCod_motivo(String cod_motivo) {
        this.cod_motivo = cod_motivo;
    }

    public String getCod_cobranza() {
        return cod_cobranza;
    }

    public void setCod_cobranza(String cod_cobranza) {
        this.cod_cobranza = cod_cobranza;
    }

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    @Override
    public String toString() {
        return "RegEstado{" +
                "documento='" + documento + '\'' +
                ", usuario='" + usuario + '\'' +
                ", usuario_id='" + usuario_id + '\'' +
                ", estado='" + estado + '\'' +
                ", motivo='" + motivo + '\'' +
                ", cobranza='" + cobranza + '\'' +
                ", fecha='" + fecha + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", comentario='" + comentario + '\'' +
                ", cod_estado='" + cod_estado + '\'' +
                ", cod_motivo='" + cod_motivo + '\'' +
                ", cod_cobranza='" + cod_cobranza + '\'' +
                '}';
    }

}
