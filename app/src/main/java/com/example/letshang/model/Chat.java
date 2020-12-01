package com.example.letshang.model;

public class Chat {

    String idUsuario;
    String nombre;
    String cuerpo;
    String fecha;

    public Chat(){

    }

    public Chat(String idUsuario, String nombre, String cuerpo, String fecha) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
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
}
