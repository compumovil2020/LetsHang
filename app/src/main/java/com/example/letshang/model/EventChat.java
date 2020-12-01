package com.example.letshang.model;

import android.graphics.Bitmap;

public class EventChat {

    String idUsuario;
    String nombre;
    String cuerpo;
    String fecha;
    Bitmap foto = null;

    public EventChat(){

    }

    public EventChat(String idUsuario,String nombre, String cuerpo, String fecha, Bitmap foto) {
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
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
