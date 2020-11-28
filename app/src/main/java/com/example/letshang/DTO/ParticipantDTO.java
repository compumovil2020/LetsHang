package com.example.letshang.DTO;

import java.util.List;
import java.util.Map;

public class ParticipantDTO extends UserDTO{

    private List<String> interests;
    private Map<String, String> categories;

    public ParticipantDTO() {
    }

    public ParticipantDTO(String name, String email, String phone, String facebook,
                          String instagram, String twitter, String youtube, String linkedIn,
                          double latitud, double longitud, long birthDate, List<String> interests,
                          Map<String, String> categories) {
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

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }
}
