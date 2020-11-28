package com.example.letshang.model;

public class EventChat {

    String idUsuario;
    String cuerpo;
    String fecha;

    public void EventChat(){

    }

    public EventChat(String idUsuario, String cuerpo, String fecha) {
        this.idUsuario = idUsuario;
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
}
