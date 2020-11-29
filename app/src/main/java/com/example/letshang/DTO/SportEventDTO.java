package com.example.letshang.DTO;

import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.SportEventLevel;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is ment store a sport event class in basic data types that firebase can understand
 */
public class SportEventDTO extends  EventDTO{


    private String sport;

    private SportEventLevel level;

    private int teamSize;


    public SportEventDTO(double latitud, double longitud, String title, String description,
                         long startDate, long endDate, long price, int maximumCapacity,
                         List<String> tags, String sport, SportEventLevel level, int teamSize, String hostName, String locationName) {
        super( latitud,longitud,title, description, startDate, endDate, price, maximumCapacity, tags, hostName, locationName);
        this.sport = sport;
        this.level = level;
        this.teamSize = teamSize;
    }

    public SportEventDTO() {
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


    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public SportEventLevel getLevel() {
        return level;
    }

    public void setLevel(SportEventLevel level) {
        this.level = level;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    @Override
    public String toString() {
        return "SportEventDTO{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", maximumCapacity=" + maximumCapacity +
                ", tags=" + tags +
                ", sport='" + sport + '\'' +
                ", level=" + level +
                ", teamSize=" + teamSize +
                '}';
    }
}
