package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;

public class PartyEvent extends Event {

    /**
     * The music genre that will be played at the party
     */
    private String musicGenre;


    public PartyEvent(String title, String description, Date startDate, Date endDate, long price, int maximumCapacity, Collection<String> tags, LatLng location, String musicGenre) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.musicGenre = musicGenre;
        this.type = EventsEnum.PARTY;
    }
}
