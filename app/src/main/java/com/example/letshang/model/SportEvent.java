package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;

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
    public SportEvent(String title, String description, Date startDate, Date endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String sport, SportEventLevel level, int teamSize, LatLng location) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.sport = sport;
        this.level = level;
        this.teamSize = teamSize;
        this.type = EventsEnum.SPORTS;
    }
}

