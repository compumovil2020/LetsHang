package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;

public class MusicEvent extends Event {

    /**
     * name of the music type which will be played at the event
     */
    private String music;


    public MusicEvent(String title, String description, Date startDate, Date endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String music, LatLng location) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.music = music;
        this.type = EventsEnum.MUSIC;
    }
}
