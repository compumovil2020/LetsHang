package com.example.letshang.model;

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


    public SportEvent(String title, String description, Date startDate, Date endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String sport, SportEventLevel level, int teamSize) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags);
        this.sport = sport;
        this.level = level;
        this.teamSize = teamSize;
    }
}

