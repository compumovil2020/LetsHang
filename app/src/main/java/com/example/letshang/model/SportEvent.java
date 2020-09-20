package com.example.letshang.model;

import java.util.Collection;
import java.util.Date;

import javax.xml.datatype.Duration;

public class SportEvent extends Event{

    /**
     * name of the sport wich will be practiced at the event
     */
    private String sport;

    /**
     * Enum represents the participants required level of expertise
     */
    private SportEventLevel level;

    /**
     * size of the team
     */
    private int teamSize;


    /**
     *
     * @param title
     * @param description
     * @param date
     * @param duration
     * @param price
     * @param maximumCapacity
     * @param tags
     * @param sport
     * @param level
     */
    public SportEvent(String title, String description, Date date,
                      Duration duration, long price, int maximumCapacity,
                      Collection<String> tags, String sport, SportEventLevel level) {
        super(title, description, date, duration, price, maximumCapacity, tags);
        this.sport = sport;
        this.level = level;

    }
}

enum SportEventLevel{
    BEGINER,
    AMATEUR,
    INTERMEDIATE,
    ADVANCED,
    PROFESIONAL
}