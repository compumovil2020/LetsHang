package com.example.letshang.DTO;

import com.example.letshang.model.GameEventLevel;

import java.util.List;

public class GameEventDTO  extends EventDTO{

    private String game;
    private GameEventLevel level;
    private String kind;
    private String prize;
    private boolean adult;
    private String ageRange;

    public GameEventDTO() {
    }

    public GameEventDTO(double latitud, double longitud, String title, String description,
                        long startDate, long endDate, long price, int maximumCapacity,
                        List<String> tags, String game, GameEventLevel level, String kind,
                        String prize, boolean adult, String ageRange, String hostName, String locationName) {
        super(latitud, longitud, title, description, startDate, endDate, price, maximumCapacity, tags, hostName, locationName);
        this.game = game;
        this.level = level;
        this.kind = kind;
        this.prize = prize;
        this.adult = adult;
        this.ageRange = ageRange;

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
