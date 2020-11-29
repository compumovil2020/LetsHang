package com.example.letshang.model;

import com.google.android.gms.maps.model.LatLng;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Host extends Participant {

    private URL webPage;
    private List<Event> hostedEvents;



    public Host(String name, String email, GregorianCalendar birthDate, String phone, String facebook, String instagram, String twitter, String youtube, String linkedIn, Preference preferences, List<Event> pastEvents) {
        super(name, email, birthDate, phone, facebook, instagram, twitter, youtube, linkedIn, preferences, pastEvents);
    }

    public Host(String name, String email, GregorianCalendar birthDate, String phone, String facebook, String instagram, String twitter, String youtube, String linkedIn, Preference preferences, List<Event> pastEvents, URL webPage, List<Event> hostedEvents) {
        super(name, email, birthDate, phone, facebook, instagram, twitter, youtube, linkedIn, preferences, pastEvents);
        this.webPage = webPage;
        this.hostedEvents = hostedEvents;
    }



    public boolean createEvent(String title, String description, Date startDate, Date endDate, long price,
                               int maximumCapacity, Collection<String> tags, LatLng location, String type){
        //TODO: Catch what type of event it is and create the event as the type.

            //GameEvent gameEvent = new GameEvent("Juego de Catan","Este es un juego de estrategia para 4 o 6 personas",new GregorianCalendar(2020,11,22),new GregorianCalendar(2020,11,22),12000,6,null,"Catan",GameEventLevel.AMATEUR, location);
        //hostedEvents.add(gameEvent);
        return true;
    }

    public boolean cancelEvent(String id){
        //TODO: Delete the event with ID <id> and send email to participants
        for(Event e:hostedEvents){
            if (e.getID() == id){
                hostedEvents.remove(e);
                return true;
            }
        }
        return false;
    }

    public boolean modifyEvent(int id,String title, String description, Date startDate, Date endDate, long price,
                               int maximumCapacity, Collection<String> tags, LatLng location){
        //TODO: Modify the event with ID <id> and send email to participants
        return true;
    }

    public void setWebPage(URL webPage) {
        this.webPage = webPage;
    }

    public void setHostedEvents(List<Event> hostedEvents) {
        this.hostedEvents = hostedEvents;
    }
}
