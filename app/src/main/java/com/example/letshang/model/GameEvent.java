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
     * size of the team of the game (can be 0)
     */
    private int teamSize;


    public GameEvent(String title, String description, Date startDate, Date endDate,
                      long price, int maximumCapacity, Collection<String> tags,
                      String game, GameEventLevel level, int teamSize, LatLng location) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags, location);
        this.game = game;
        this.level = level;
        this.teamSize = teamSize;
        this.type = EventsEnum.GAME;
    }

}
