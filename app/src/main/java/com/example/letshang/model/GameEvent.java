package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class GameEvent extends Event{

    /**
     * name of the game which will be played at the event
     */
    private String game;

    /**
     * Enum represents the participants required level of expertise
     */
    private GameEventLevel level;

    /**
     * type of game (Strategy, Interaction, Mindfulness, etc)
     */
    private String kind;

    /**
     * prize of the game (can be none)
     */
    private String prize;

    /**
     * if the game requires only adult people
     */
    private boolean adult;

    private String ageRange;


    /**
     *
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param maximumCapacity
     * @param tags
     * @param game
     * @param level
     * @param location
     */
    public GameEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String game, GameEventLevel level, LatLng location, String locationName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locationName);
        this.game = game;
        this.level = level;
        this.type = EventsEnum.GAME;
    }


    public GameEvent(String title, String description, GregorianCalendar startDate,
                     GregorianCalendar endDate, long price, int maximumCapacity,
                     Collection<String> tags, LatLng location, String game, GameEventLevel level,
                     String kind, String prize, boolean adult, String ageRange, String locatinoName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locatinoName);
        this.game = game;
        this.level = level;
        this.kind = kind;
        this.prize = prize;
        this.adult = adult;
        this.ageRange = ageRange;
        this.type = EventsEnum.GAME;

    }

    /**
     * Basic constructor, just call super
     * @param title
     * @param description
     * @param startDate
     * @param endDate
     * @param price
     * @param maximumCapacity
     * @param tags
     * @param location
     */
    public GameEvent(String title, String description, GregorianCalendar startDate, GregorianCalendar endDate, long price,
                     int maximumCapacity, Collection<String> tags, LatLng location, String locatinoName) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location, locatinoName);
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public GameEventLevel getLevel() {
        return level;
    }

    public void setLevel(GameEventLevel level) {
        this.level = level;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }
}
