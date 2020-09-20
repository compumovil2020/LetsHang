package com.example.letshang.model;

import java.util.Date;

public class User {

    // TODO: falta ubicacion
    private String name;
    private String email;
    private String phone;
    private String facebook;
    private String instagram;
    private String twitter;
    private String youtube;
    private String linkedIn;
    private Date birthDate;


    public User(String name, String email, Date birthDate,
                String phone, String facebook,
                String instagram, String twitter, String youtube,
                String linkedIn) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.phone = phone;
        this.facebook = facebook;
        this.instagram = instagram;
        this.twitter = twitter;
        this.youtube = youtube;
        this.linkedIn = linkedIn;
    }
}
