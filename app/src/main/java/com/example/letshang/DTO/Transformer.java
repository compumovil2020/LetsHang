package com.example.letshang.DTO;

import android.provider.Telephony;

import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.Event;
import com.example.letshang.model.GameEvent;
import com.example.letshang.model.Host;
import com.example.letshang.model.MusicEvent;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SocialEvent;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Transformer {


    // object to DTO

    // transforms a SportEvent into something that firebase can understand
    public static SportEventDTO transform(SportEvent e, String hostName){


        return new SportEventDTO(e.getLocation().latitude, e.getLocation().longitude, e.getTitle(),
                e.getDescription(), e.getStartDate().getTimeInMillis(),
                e.getEndDate().getTimeInMillis(), e.getPrice(), e.getMaximumCapacity(),
                transformTags(e.getTags()), e.getSport(), e.getLevel(), e.getTeamSize(), hostName, e.getLocationName());

    }

    public static AcademicEventDTO transform(AcademicEvent e, String hostName){


        return new AcademicEventDTO(e.getLocation().latitude, e.getLocation().longitude, e.getTitle(),
                e.getDescription(), e.getStartDate().getTimeInMillis(),
                e.getEndDate().getTimeInMillis(), e.getPrice(), e.getMaximumCapacity(),
                transformTags(e.getTags()),e.getSubject(), e.getLevel(), e.getTypeAcademicalEvent(), e.getLanguages(), hostName, e.getLocationName());

    }

    public static GameEventDTO transform(GameEvent e, String hostName){


        return new GameEventDTO(e.getLocation().latitude, e.getLocation().longitude, e.getTitle(),
                e.getDescription(), e.getStartDate().getTimeInMillis(),
                e.getEndDate().getTimeInMillis(), e.getPrice(), e.getMaximumCapacity(),
                transformTags(e.getTags()),e.getGame(), e.getLevel(), e.getKind(), e.getPrize(), e.isAdult(), e.getAgeRange(), hostName, e.getLocationName());

    }

    public static MusicEventDTO transform(MusicEvent e, String hostName){


        return new MusicEventDTO(e.getLocation().latitude, e.getLocation().longitude, e.getTitle(),
                e.getDescription(), e.getStartDate().getTimeInMillis(),
                e.getEndDate().getTimeInMillis(), e.getPrice(), e.getMaximumCapacity(),
                transformTags(e.getTags()),e.getMusic(), e.getArtists(), hostName, e.getLocationName());

    }

    public static SocialEventDTO transform(SocialEvent e, String hostName){


        return new SocialEventDTO(e.getLocation().latitude, e.getLocation().longitude, e.getTitle(),
                e.getDescription(), e.getStartDate().getTimeInMillis(),
                e.getEndDate().getTimeInMillis(), e.getPrice(), e.getMaximumCapacity(),
                transformTags(e.getTags()),e.getMusicGenre(), e.getTheme(), e.getMinimumAge(), e.getRules(), hostName , e.getLocationName());

    }





    private static List<String> transformTags(Collection<String> tags){

        List<String> lista = new ArrayList<>();
        for(String i:tags){
            lista.add(i);
        }
        return lista;
    }


    public static SportEvent transform(SportEventDTO dto) {

        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar endDate = new GregorianCalendar();
        startDate.setTimeInMillis(dto.getStartDate());
        endDate.setTimeInMillis(dto.getEndDate());
        SportEvent e = new SportEvent(dto.getTitle(), dto.getDescription(), startDate, endDate,
                dto.getPrice(), dto.getMaximumCapacity(), dto.getTags(), dto.getSport(),
                dto.getLevel(), dto.getTeamSize(), new LatLng(dto.getLatitud(), dto.getLongitud()), dto.getLocationName());

        return  e;
    }

    public static AcademicEvent transform(AcademicEventDTO dto) {

        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar endDate = new GregorianCalendar();
        startDate.setTimeInMillis(dto.getStartDate());
        endDate.setTimeInMillis(dto.getEndDate());
        AcademicEvent e = new AcademicEvent(dto.getTitle(), dto.getDescription(), startDate, endDate,
                dto.getPrice(), dto.getMaximumCapacity(), dto.getTags(),new LatLng(dto.getLatitud(),
                dto.getLongitud()), dto.getSubject(), dto.getLevel(), dto.getTypeAcademicalEvent(),dto.getLanguages(), dto.getLocationName());
        
        return e;
    }

    public static GameEvent transform(GameEventDTO dto) {

        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar endDate = new GregorianCalendar();
        startDate.setTimeInMillis(dto.getStartDate());
        endDate.setTimeInMillis(dto.getEndDate());
        GameEvent e = new GameEvent(dto.getTitle(), dto.getDescription(), startDate, endDate,
                dto.getPrice(), dto.getMaximumCapacity(), dto.getTags(),new LatLng(dto.getLatitud(),
                dto.getLongitud()), dto.getGame(), dto.getLevel(), dto.getKind(), dto.getPrize(), dto.isAdult(), dto.getAgeRange(), dto.getLocationName());

        return e;
    }

    public static MusicEvent transform(MusicEventDTO dto) {
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar endDate = new GregorianCalendar();
        startDate.setTimeInMillis(dto.getStartDate());
        endDate.setTimeInMillis(dto.getEndDate());
        MusicEvent e = new MusicEvent(dto.getTitle(), dto.getDescription(), startDate, endDate,
                dto.getPrice(), dto.getMaximumCapacity(), dto.getTags(), dto.getMusic(), dto.getArtists(),new LatLng(dto.getLatitud(),
                dto.getLongitud()), dto.getLocationName());

        return e;

    }

    public static SocialEvent transform(SocialEventDTO dto) {

        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar endDate = new GregorianCalendar();
        startDate.setTimeInMillis(dto.getStartDate());
        endDate.setTimeInMillis(dto.getEndDate());
        SocialEvent e = new SocialEvent(dto.getTitle(), dto.getDescription(), startDate, endDate,
                dto.getPrice(), dto.getMaximumCapacity(), dto.getTags(), new LatLng(dto.getLatitud(),
                dto.getLongitud()), dto.getMusicGenre(), dto.getRules(),dto.getMinimumAge(), dto.getRules(), dto.getLocationName());

        return e;
    }

    public static UserDTO transform(Participant u){
        return new ParticipantDTO(u.getName(), u.getEmail(),
                u.getPhone(), u.getFacebook(), u.getInstagram(), u.getTwitter(), u.getYoutube(),
                u.getLinkedIn(), u.getLocation().latitude, u.getLocation().longitude,
                u.getBirthDate().getTimeInMillis(), u.getPreferences().getInterests(),
                u.getPreferences().getCategories() );
    }


    public static Participant transform(ParticipantDTO dto) {
        if(dto == null){
            return null;
        }
        GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.setTimeInMillis(dto.getBirthDate());
        return new Participant(dto.getName(), dto.getEmail(),birthDate , dto.getPhone(),
                dto.getFacebook(), dto.getInstagram(), dto.getTwitter(), dto.getYoutube(),
                dto.getLinkedIn(), new Preference(dto.getCategories(), dto.getInterests()),
                null);
    }
}
