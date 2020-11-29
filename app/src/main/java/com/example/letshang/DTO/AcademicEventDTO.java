package com.example.letshang.DTO;

import com.example.letshang.model.AcademicEventLevel;
import com.example.letshang.model.AcademicType;

import java.util.List;

public class AcademicEventDTO extends EventDTO {


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



    public AcademicEventDTO(double latitud, double longitud, String title, String description,
                            long startDate, long endDate, long price, int maximumCapacity,
                            List<String> tags, String subject, AcademicEventLevel level,
                            AcademicType typeAcademicalEvent, String languages, String hostname, String locationName) {
        super(latitud, longitud, title, description, startDate, endDate, price, maximumCapacity, tags, hostname, locationName);
        this.subject = subject;
        this.level = level;
        this.typeAcademicalEvent = typeAcademicalEvent;
        this.languages = languages;
    }

    public AcademicEventDTO() {
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
