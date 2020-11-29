package com.example.letshang.DTO;

import java.util.List;

public class SocialEventDTO extends EventDTO{

    private String musicGenre;
    private String theme;
    private int minimumAge;
    private String rules;


    public SocialEventDTO(double latitud, double longitud, String title, String description,
                          long startDate, long endDate, long price, int maximumCapacity,
                          List<String> tags, String musicGenre, String theme, int minimumAge,
                          String rules, String hostName, String locationName) {
        super(latitud, longitud, title, description, startDate, endDate, price, maximumCapacity, tags, hostName, locationName);
        this.musicGenre = musicGenre;
        this.theme = theme;
        this.minimumAge = minimumAge;
        this.rules = rules;
    }

    public SocialEventDTO() {
    }

    public String getMusicGenre() {
        return musicGenre;
    }

    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
