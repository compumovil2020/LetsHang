package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

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




    /**
     *
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param maximumCapacity
     * @param tags
     * @param subject
     * @param level
     */
    public AcademicEvent(String title, String description, Date startDate, Date endDate,
                         long price, int maximumCapacity, Collection<String> tags,
                         String subject, AcademicEventLevel level, LatLng location) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.subject = subject;
        this.level = level;
        this.type = EventsEnum.ACADEMIC;
    }

}
