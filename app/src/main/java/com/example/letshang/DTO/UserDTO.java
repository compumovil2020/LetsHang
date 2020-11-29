package com.example.letshang.DTO;

import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;

public class UserDTO {

    protected String name;
    protected String email;
    protected String phone;
    protected String facebook;
    protected String instagram;
    protected String twitter;
    protected String youtube;
    protected String linkedIn;
    protected double latitud;
    protected double longitud;
    protected long birthDate;

    public UserDTO(String name, String email, String phone, String facebook, String instagram,
                   String twitter, String youtube, String linkedIn, double latitud, double longitud,
                   long birthDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
        this.youtube = youtube;
        this.linkedIn = linkedIn;
        this.latitud = latitud;
        this.longitud = longitud;
        this.birthDate = birthDate;
    }

    public UserDTO(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }
}
