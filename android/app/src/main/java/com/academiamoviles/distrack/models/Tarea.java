package com.academiamoviles.distrack.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Tarea extends RealmObject {

    private String idpedido = "";
    private String cliente = "";
    @PrimaryKey
    private String documento = "";
    private String peso = "";
    private String cantidad = "";
    private String dircliente = "";
    private String aux5 = "";
    private Boolean sincronizar = false;
    private Boolean llegada = false;
    private Boolean iniciado1 = false;
    private Boolean iniciado2 = false;
    private String tareaEstado = "";
    private String tareaCodEstado = "";
    private String tareaMotivo = "";
    private String tareaCodMotivo = "";
    private String comentario = "";
    private Boolean encontrado = false;

    public Tarea() {
    }

    public Boolean getEncontrado() {
        return encontrado;
    }

    public void setEncontrado(Boolean encontrado) {
        this.encontrado = encontrado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getTareaCodEstado() {
        return tareaCodEstado;
    }

    public void setTareaCodEstado(String tareaCodEstado) {
        this.tareaCodEstado = tareaCodEstado;
    }

    public String getTareaCodMotivo() {
        return tareaCodMotivo;
    }

    public void setTareaCodMotivo(String tareaCodMotivo) {
        this.tareaCodMotivo = tareaCodMotivo;
    }

    public Boolean getSincronizar() {
        return sincronizar;
    }

    public void setSincronizar(Boolean sincronizar) {
        this.sincronizar = sincronizar;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdpedido() {
        return idpedido;
    }

    public void setIdpedido(String idpedido) {
        this.idpedido = idpedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }



    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getDircliente() {
        return dircliente;
    }

    public void setDircliente(String dircliente) {
        this.dircliente = dircliente;
    }

    public String getAux5() {
        return aux5;
    }

    public void setAux5(String aux5) {
        this.aux5 = aux5;
    }

    public Boolean getLlegada() {
        return llegada;
    }

    public void setLlegada(Boolean llegada) {
        this.llegada = llegada;
    }

    public Boolean getIniciado1() {
        return iniciado1;
    }

    public void setIniciado1(Boolean iniciado1) {
        this.iniciado1 = iniciado1;
    }

    public Boolean getIniciado2() {
        return iniciado2;
    }

    public void setIniciado2(Boolean iniciado2) {
        this.iniciado2 = iniciado2;
    }

    public String getTareaEstado() {
        return tareaEstado;
    }

    public void setTareaEstado(String tareaEstado) {
        this.tareaEstado = tareaEstado;
    }

    public String getTareaMotivo() {
        return tareaMotivo;
    }

    public void setTareaMotivo(String tareaMotivo) {
        this.tareaMotivo = tareaMotivo;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "idpedido='" + idpedido + '\'' +
                ", cliente='" + cliente + '\'' +
                ", documento='" + documento + '\'' +
                ", peso='" + peso + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", dircliente='" + dircliente + '\'' +
                ", aux5='" + aux5 + '\'' +
                ", sincronizar=" + sincronizar +
                ", llegada=" + llegada +
                ", iniciado1=" + iniciado1 +
                ", iniciado2=" + iniciado2 +
                ", tareaEstado='" + tareaEstado + '\'' +
                ", tareaCodEstado='" + tareaCodEstado + '\'' +
                ", tareaMotivo='" + tareaMotivo + '\'' +

                ", tareaCodMotivo='" + tareaCodMotivo + '\'' +
                '}';
    }
}
