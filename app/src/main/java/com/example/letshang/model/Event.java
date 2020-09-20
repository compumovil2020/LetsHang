package com.example.letshang.model;

import java.util.Collection;
import java.util.Date;

import javax.xml.datatype.Duration;

public abstract class Event {
    private String title;
    private String description;
    private Date date;
    private Duration duration;
    private long price;
    private int maximumCapacity;
    private Collection<String> tags;

    public Event(String title, String description, Date date, Duration duration, long price, int maximumCapacity, Collection<String> tags) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.duration = duration;
        this.price = price;
        this.maximumCapacity = maximumCapacity;
        this.tags = tags;
    }

    public Date getDate() {
        return this.date;
    }
}
