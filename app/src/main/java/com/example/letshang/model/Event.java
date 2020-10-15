package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;

public abstract class Event {
    protected LatLng location;
    protected String title;
    protected String description;
    protected Date startDate;
    protected Date endDate;
    protected long price;
    protected int maximumCapacity;
    protected Collection<String> tags;
    protected static EventsEnum type;



    public Event(String title, String description, Date startDate, Date endDate, long price,
                 int maximumCapacity, Collection<String> tags, LatLng location) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.maximumCapacity = maximumCapacity;
        this.tags = tags;
        this.location = location;
    }

    public static EventsEnum getType() {
        return type;
    }

    public LatLng getLocation() {
        return location;
    }


    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

}
