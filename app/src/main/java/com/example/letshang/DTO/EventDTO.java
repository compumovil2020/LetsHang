package com.example.letshang.DTO;

import java.util.List;

public abstract class EventDTO {
    protected double latitud;
    protected double longitud;
    protected String title;
    protected String description;
    protected long startDate;
    protected long endDate;
    protected long price;
    protected int maximumCapacity;
    protected List<String> tags;
    protected String hostName;
    protected String locationName;

    public EventDTO() {
    }

    public EventDTO(double latitud, double longitud, String title, String description,
                    long startDate, long endDate, long price, int maximumCapacity, List<String> tags,
                    String hostName, String locationName) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.maximumCapacity = maximumCapacity;
        this.tags = tags;
        this.hostName = hostName;
        this.locationName = locationName;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
