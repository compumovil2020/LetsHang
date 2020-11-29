package com.example.letshang.DTO;

import com.example.letshang.model.EventsEnum;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ParticipantDTO extends UserDTO{

    private List<String> interests;
    private EnumMap<EventsEnum, Double> categories;

    public ParticipantDTO() {
    }

    public ParticipantDTO(String name, String email, String phone, String facebook,
                          String instagram, String twitter, String youtube, String linkedIn,
                          double latitud, double longitud, long birthDate, List<String> interests,
                          EnumMap<EventsEnum, Double> categories) {
        super(name, email, phone, facebook, instagram, twitter, youtube, linkedIn, latitud, longitud, birthDate);
        this.interests = interests;
        this.categories = categories;
    }


    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public EnumMap<EventsEnum, Double>getCategories() {
        return categories;
    }

    public void setCategories(EnumMap<EventsEnum, Double> categories) {
        this.categories = categories;
    }
}
