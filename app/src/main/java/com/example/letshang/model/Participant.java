package com.example.letshang.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Participant extends User {


    private List<Event> pastEvents;
    private Preference preferences;

    public Participant(String name, String email, Date birthDate, String phone,
                        String facebook, String instagram,
                       String twitter, String youtube, String linkedIn, Preference preferences, List<Event> pastEvents) {

        super(name, email, birthDate, phone, facebook, instagram, twitter, youtube, linkedIn);
        this.preferences = preferences;
        this.pastEvents = pastEvents;
    }

    /**
     *
     * @return the last event in which the the user participated
     */
    public Event lastEvent(){
        if(pastEvents.size() == 0){
            return null;
        }
        Event answer = pastEvents.get(0);
        for(Event e: pastEvents){
            if(answer.getDate().compareTo(e.getDate()) < 0 ){
                answer = e;
            }
        }
        return answer;
    }
}
