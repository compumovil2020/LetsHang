package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Event {
    protected int ID;
    protected LatLng location;
    protected String title;
    protected String description;
    protected Date startDate;
    protected Date endDate;
    protected long price;
    protected int maximumCapacity;
    protected Collection<String> tags;
    protected static EventsEnum type;
    private static AtomicInteger nextId = new AtomicInteger();



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
        setId();
    }

    private void setId() {
        this.ID = nextId.incrementAndGet();
    }

    public int getID() { return ID; }

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

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getMaximumCapacity() {
        return maximumCapacity;
    }

    public void setMaximumCapacity(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
    }

    public Collection<String> getTags() {
        return tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public static void setType(EventsEnum type) {
        Event.type = type;
    }
}
