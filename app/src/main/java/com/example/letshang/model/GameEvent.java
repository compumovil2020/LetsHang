package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.Collection;
import java.util.Date;

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


    public GameEvent(String title, String description, Date startDate, Date endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String game, GameEventLevel level, String kind, String prize, boolean adult, LatLng location) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.game = game;
        this.level = level;
        this.kind = kind;
        this.prize = prize;
        this.adult = adult;
        this.type = EventsEnum.GAME;
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
}
