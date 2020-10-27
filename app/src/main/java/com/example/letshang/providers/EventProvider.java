package com.example.letshang.providers;

import android.location.Location;

import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.AcademicEventLevel;
import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Host;
import com.example.letshang.model.MusicEvent;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.google.android.gms.maps.model.LatLng;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
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

        GregorianCalendar startGreo = new GregorianCalendar();
        startGreo.set(GregorianCalendar.YEAR, 2020);
        startGreo.set(GregorianCalendar.MONTH, 9);
        startGreo.set(GregorianCalendar.DAY_OF_MONTH, 10);
        startGreo.set(GregorianCalendar.HOUR, 10);
        startGreo.set(GregorianCalendar.MINUTE, 00);

        Event event1 = new SportEvent("Partido de futbol",
                "Clase para niños y adolecentes entre 10 y 14 años.  Exelente forma de " +
                        "pasar el fin de semana! Terminamos la clase con un partido amistoso.",
                new GregorianCalendar(2020, 9,10,10,00),
                new GregorianCalendar(2020, 9,10,15,00),
                10000, 100, tags, "Futbol",
                SportEventLevel.BEGINNER, 11, new LatLng(4.713103, -74.052271)
        );

        tags = new ArrayList<String>();
        tags.add("Monitoria");
        tags.add("Economía");
        tags.add("Parcial 2");

        Event event2 = new AcademicEvent("Monitoría de la clase de microeconomía",
                "Monitoria del segundo corte para la materia de microeconomia.",
                new GregorianCalendar(2020, 10, 11, 10, 00),
                new GregorianCalendar(2020, 9,10,16,00),
                10000, 100, tags, "Administración de empresas",
                AcademicEventLevel.UNIVERSITY, new LatLng(4.628640, -74.065273)
        );

        tags = new ArrayList<String>();
        tags.add("Monitoria");
        tags.add("Pensamiento Algoritmico");
        tags.add("Sistemas");
        tags.add("Parcial 3");

        Event event3 = new AcademicEvent("Monitoría de la clase de Pensamiento Algoritmico",
                "Monitoria del tercer corte para la materia de pensamiento algoritmico.",
                new GregorianCalendar(2020, 5, 1, 10, 00),
                new GregorianCalendar(2020, 9,10,11,00),
                5800, 25, tags, "Ingeniería de Sistemas",
                AcademicEventLevel.UNIVERSITY, new LatLng(4.651440, -74.095273)
        );

        tags = new ArrayList<String>();
        tags.add("Concierto");
        tags.add("Jazz");
        tags.add("Al aire");
        tags.add("Parque nacional");

        Event event4 = new MusicEvent("Concierto de Jazz",
                "Concierto de Jazz en el parque nacional",
                new GregorianCalendar(2020, 8, 30, 10, 00),
                new GregorianCalendar(2020, 9,10,12,00),
                12000, 30, tags, "Jazz & Blues",
                "Artista 1, Artista 2", new LatLng(4.671360, -74.045223)
        );

        tags = new ArrayList<String>();
        tags.add("Futbol 5");
        tags.add("Futbol");
        tags.add("Soccer");

        Event event5 = new SportEvent("Futbol 5",
                "Partido de futbol 5 en el Soccer de la 147",
                new GregorianCalendar(2020, 1, 27, 10, 00),
                new GregorianCalendar(2020, 9,10,13,00),
                4500, 15, tags, "Futbol",
                SportEventLevel.INTERMEDIATE, 5, new LatLng(4.751111, -74.092222)
        );

        tags = new ArrayList<String>();
        tags.add("Concierto");
        tags.add("Rock");
        tags.add("Al aire");
        tags.add("Unicentro");

        Event event6 = new MusicEvent("Concierto de Rock",
                "Concierto de Rock en el parqueadero del centro comercial unicentro",
                new GregorianCalendar(2021, 3, 5, 10, 00),
                new GregorianCalendar(2020, 9,10,14,00),
                11300, 60, tags, "Rock & Roll",
                "Artista 1, Artista 2, Artista 3", new LatLng(4.771360, -74.745223)
        );



        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event3);
        eventsList.add(event4);
        eventsList.add(event5);
        eventsList.add(event6);
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

        if(e.getEndDate().getTime().compareTo(minDate) < 0 ){
            return false;
        }
        if(e.getStartDate().getTime().compareTo(maxDate) > 0){
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
