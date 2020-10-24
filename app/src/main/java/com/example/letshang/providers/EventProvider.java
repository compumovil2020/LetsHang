package com.example.letshang.providers;

import android.location.Location;

import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.AcademicEventLevel;
import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Host;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;


// Singleton class
public class EventProvider {

    private List<Event> eventsList;

    private static EventProvider instance = null;

    /**
     * returns singleton instance of provider
     * @return
     */
    public static EventProvider getInsatance(){
        if(instance == null){
            instance = new EventProvider();
        }
        return instance;
    }

    private EventProvider(){
        eventsList = new ArrayList<>();
        ArrayList<String> tags = new ArrayList<String>();
        tags.add("futbol");
        tags.add("parque");
        tags.add("juvenil");

        Event event1 = new SportEvent("Partido de futbol",
                "Clase para niños y adolecentes entre 10 y 14 años.  Exelente forma de " +
                        "pasar el fin de semana! Terminamos la clase con un partido amistoso.",
                new Date(2020, 10, 11, 10, 00),
                new Date(2020,10,11, 15,00),
                10000, 100, tags, "Futbol",
                SportEventLevel.BEGINNER, 11, new LatLng(4.713103, -74.052271)
        );

        tags = new ArrayList<String>();
        tags.add("futbol");
        tags.add("parque");
        tags.add("juvenil");

        Event event2 = new AcademicEvent("Monitoría de la clase de microeconomía",
                "Se ",
                new Date(2020, 10, 11, 10, 00),
                new Date(2020,10,11, 15,00),
                10000, 100, tags, "Administración de empresas",
                AcademicEventLevel.UNIVERSITY, new LatLng(4.628640, -74.065273)
        );



        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event2);
        eventsList.add(event2);
        eventsList.add(event2);
        eventsList.add(event2);
    }

    /**
     *
     * @param minDate
     * @param maxDate
     * @param maxDistance
     * @param types
     * @param currentLocation
     * @return
     */
    public List<Event> getEventsByDistance(Date minDate, Date maxDate, int maxDistance,
                                           Set<EventsEnum> types, LatLng currentLocation){

        // en este momento solo se retorna un arraylist de eventos quemados
        // se deberian flitrar los eventos de eventsList con los parametros que entran

        List<Event> ans = new ArrayList<>();

        for(int i = 0 ; i < eventsList.size() ; ++i){
            if(filter(eventsList.get(i),  minDate, maxDate,maxDistance, types , currentLocation) ){
                ans.add(eventsList.get(i));
            }
        }

        return ans;
    }

    // Falta filtrar por la distancia
    public boolean filter(Event e , Date minDate, Date maxDate, int maxDistance,
                           Set<EventsEnum> types, LatLng currentLocation){

        if(e.getEndDate().compareTo(minDate) < 0 ){
            return false;
        }
        if(e.getStartDate().compareTo(maxDate) > 0){
            return false;
        }

        if(distanceBetween(currentLocation, e.getLocation()) > maxDistance){
            return false;
        }

        if(types.contains(e.getType())){
            return false;
        }


        return true;
    }


    /**
     * returns distance between two points
     * @param point1
     * @param point2
     * @return
     */
    private double distanceBetween(LatLng point1, LatLng point2) {

        double lat1 = point1.latitude;
        double lon1 = point1.longitude;

        double lat2 = point2.latitude;
        double lon2 = point2.longitude;

        double el1 = 0, el2 = 0;
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /**
     * Returns the array of events by querying the database
     * @return
     */
    public List<Event> getAllEventsFromDBB(){


        return eventsList;

    }


    /**
     * returns null if there is no event with the given ID
     * @param eventID
     * @return
     */
    public Event getEventByID(int eventID){
        for(Event e: eventsList){
            if(e.getID() == eventID){
                return e;
            }
        }
        return null;
    }


    /**
     * creates an event and puts it in the DB
     * @param event
     * @param host
     */
    public void createEvent(Event event, Host host){
        eventsList.add(event);
    }


    /**
     * returns the Host associated to the event ID
     * @param eventID
     * @return
     */
    public Host getEventHost(int eventID){

        // siempre devuelve el mismo host
        // deberia hacer una query en firebase para sacar el host
        List<Event> pastEvents = new ArrayList<>();
        EnumMap<EventsEnum , Double> mapa = new EnumMap<EventsEnum, Double>(EventsEnum.class);
        mapa.put(EventsEnum.ACADEMIC , 0.0);
        mapa.put(EventsEnum.SPORTS , 2.0);
        mapa.put(EventsEnum.MUSIC , 2.1);
        Preference preferences = new Preference(mapa , new String[]{"futbol" , "parque" , "yoga", "fit"});

        Host host = new Host("Maria Gonzalez","maria@gonzalez.com",
                new Date(1990, 2,11),"3155263542",
                "maria.gonzalez",null,null,
                null,null,preferences,pastEvents );

        try {
            host.setWebPage(new URL("https://drinkinggamezone.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }


}
