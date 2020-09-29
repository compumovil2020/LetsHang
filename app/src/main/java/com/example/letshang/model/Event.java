package com.example.letshang.model;

import java.util.Collection;
import java.util.Date;

public abstract class Event {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private long price;
    private int maximumCapacity;
    private Collection<String> tags;

    public Event(String title, String description, Date startDate, Date endDate, long price, int maximumCapacity, Collection<String> tags) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.maximumCapacity = maximumCapacity;
        this.tags = tags;
    }

    public Date getDate() {
        return this.startDate;
    }
}
