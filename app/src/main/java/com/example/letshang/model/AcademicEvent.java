package com.example.letshang.model;

import java.util.Collection;
import java.util.Date;

public class AcademicEvent extends Event {

    /**
     * name of the music type which will be played at the event
     */
    private String subject;

    /**
     * Enum represents the participants required level of expertise
     */
    private AcademicEventLevel level;


    public AcademicEvent(String title, String description, Date startDate, Date endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String subject, AcademicEventLevel level) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags);
        this.subject = subject;
        this.level = level;
    }

}
