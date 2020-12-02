package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

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
     * Type of academical event, such as, Diplomes, Workshop, etc
     * */
    private AcademicType typeAcademicalEvent;


    /**
     * What is the languages of the event
     * */
    private String languages;

    /**
     *
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param maximumCapacity
     * @param tags
     */
    public AcademicEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate,
                         long price, int maximumCapacity, Collection<String> tags, LatLng location, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
        this.subject = subject;
        this.level = level;
        this.type = EventsEnum.ACADEMIC;
    }

    public AcademicEvent(String title, String description, GregorianCalendar startDate,
                         GregorianCalendar endDate, long price, int maximumCapacity,
                         Collection<String> tags, LatLng location, String subject,
                         AcademicEventLevel level, AcademicType typeAcademicalEvent,
                         String languages, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
        this.subject = subject;
        this.level = level;
        this.typeAcademicalEvent = typeAcademicalEvent;
        this.languages = languages;
        this.type = EventsEnum.ACADEMIC;

    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AcademicEventLevel getLevel() {
        return level;
    }

    public void setLevel(AcademicEventLevel level) {
        this.level = level;
    }

    public AcademicType getTypeAcademicalEvent() {
        return typeAcademicalEvent;
    }

    public void setTypeAcademicalEvent(AcademicType typeAcademicalEvent) {
        this.typeAcademicalEvent = typeAcademicalEvent;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }
}
