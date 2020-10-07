package com.example.letshang.model;

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
                      String game, GameEventLevel level, int teamSize) {
        super(title, description, startDate, endDate, price, maximumCapacity, tags);
        this.game = game;
        this.level = level;
        this.teamSize = teamSize;
    }

}
