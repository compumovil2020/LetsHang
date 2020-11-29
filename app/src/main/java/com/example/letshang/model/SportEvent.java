package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.Duration;

public class SportEvent extends Event{

    /**
     * name of the sport which will be practiced at the event
     */
    private String sport;

    /**
     * Enum represents the participants required level of expertise
     */
    private SportEventLevel level;

    /**
     * size of the team (can be 0)
     */
    private int teamSize;


    /**
     *
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param maximumCapacity
     * @param tags
     * @param sport
     * @param level
     * @param teamSize
     */
    public SportEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String sport, SportEventLevel level, int teamSize, LatLng location, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
        this.sport = sport;
        this.level = level;
        this.teamSize = teamSize;
        this.type = EventsEnum.SPORTS;
    }

    /**
     * Basic constructor just calls super
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param maximumCapacity
     * @param tags
     * @param location
     */
    public SportEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate, long price,
                      int maximumCapacity, Collection<String> tags, LatLng location, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
    }



    public void setSport(String sport) {
        this.sport = sport;
    }

    public void setLevel(SportEventLevel level) {
        this.level = level;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public String getSport() {
        return sport;
    }

    public SportEventLevel getLevel() {
        return level;
    }

    public int getTeamSize() {
        return teamSize;
    }
}

