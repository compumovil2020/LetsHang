package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class MusicEvent extends Event {

    /**
     * name of the music type which will be played at the event
     */
    private String music;
    private String artists;


    public MusicEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String music, String artists, LatLng location, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
        this.music = music;
        this.artists = artists;
        this.type = EventsEnum.MUSIC;
    }

    public MusicEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate, long price,
                      int maximumCapacity, Collection<String> tags, LatLng location, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }
}
