package com.example.letshang.model;

import android.graphics.Bitmap;

public class Chat {

    String idUsuario;
    String nombre;
    String cuerpo;
    long fecha;
    Bitmap foto = null;

    public Chat(){

    }

    public Chat(String idUsuario, String nombre, String cuerpo, long fecha, Bitmap foto) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
        this.foto = foto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
