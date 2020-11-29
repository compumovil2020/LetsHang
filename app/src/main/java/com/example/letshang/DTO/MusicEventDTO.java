package com.example.letshang.DTO;

import java.util.List;

public class MusicEventDTO extends EventDTO{

    private String music;
    private String artists;

    public MusicEventDTO(String music, String artists) {
        this.music = music;
        this.artists = artists;
    }

    public MusicEventDTO(double latitud, double longitud, String title, String description,
                         long startDate, long endDate, long price, int maximumCapacity,
                         List<String> tags, String music, String artists, String hostName, String locationName) {
        super(latitud, longitud, title, description, startDate, endDate, price, maximumCapacity, tags, hostName, locationName);
        this.music = music;
        this.artists = artists;
    }

    public MusicEventDTO() {
    }

    public String getMusic() {
        return music;
    }

    public String getArtists() {
        return artists;
    }
}
