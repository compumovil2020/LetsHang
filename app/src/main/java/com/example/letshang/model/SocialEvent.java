package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;

public class PartyEvent extends Event {

    /**
     * The music genre that will be played at the party
     */
    private String musicGenre;
    private String theme;
    private int minimumAge;
    private String rules;


    /**
     * General Constructor from abstract Event
     */
    public PartyEvent(String title, String description, Date startDate, Date endDate, long price, int maximumCapacity, Collection<String> tags, LatLng location) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);

    }

    /**
     * Specific Constructor from Party Event
     */
    public PartyEvent(String title, String description, Date startDate, Date endDate, long price, int maximumCapacity, Collection<String> tags, LatLng location, String musicGenre, String theme, int minimumAge, String rules) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.musicGenre = musicGenre;
        this.theme = theme;
        this.minimumAge = minimumAge;
        this.rules = rules;
    }

    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
