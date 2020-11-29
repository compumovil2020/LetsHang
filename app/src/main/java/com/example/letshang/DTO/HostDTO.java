package com.example.letshang.DTO;

import com.example.letshang.model.EventsEnum;
import com.google.android.gms.maps.model.LatLng;

import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class HostDTO extends ParticipantDTO{

    private String webPAgeURL;

    public HostDTO(String name, String email, String phone, String facebook, String instagram,
                   String twitter, String youtube, String linkedIn, double latitud, double longitud,
                   long birthDate, List<String> interests, EnumMap<EventsEnum, Double> categories,
                   String webPAgeURL) {
        super(name, email, phone, facebook, instagram, twitter, youtube, linkedIn, latitud,
                longitud, birthDate, interests, categories);
        this.webPAgeURL = webPAgeURL;
    }

    public HostDTO(){};

    public String getWebPAgeURL() {
        return webPAgeURL;
    }

    public void setWebPAgeURL(String webPAgeURL) {
        this.webPAgeURL = webPAgeURL;
    }
}
