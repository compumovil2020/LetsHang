package com.example.letshang.providers;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.letshang.DTO.AcademicEventDTO;
import com.example.letshang.DTO.GameEventDTO;
import com.example.letshang.DTO.MusicEventDTO;
import com.example.letshang.DTO.SocialEventDTO;
import com.example.letshang.DTO.SportEventDTO;
import com.example.letshang.DTO.Transformer;
import com.example.letshang.model.AcademicEvent;
import com.example.letshang.model.AcademicEventLevel;
import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.GameEvent;
import com.example.letshang.model.Host;
import com.example.letshang.model.MusicEvent;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SocialEvent;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference eventsRef = database.getReference("events");
    private static final  String TAG = "eventProvider";

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

        addListeners();

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
                new GregorianCalendar(1990, 2,11),"3155263542",
                "maria.gonzalez",null,null,
                null,null,preferences,pastEvents );

        try {
            host.setWebPage(new URL("https://drinkinggamezone.com/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return host;
    }


    // Sets a listener for all 5 database references to listen for all types of events
    private void addListeners(){


        ChildEventListener listenerSport = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                eventsList.add(Transformer.transform(dataSnapshot.getValue(SportEventDTO.class)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Log.i(TAG, "onCancelled: Failed to load data");
            }
        };

        ChildEventListener listenerAcademic = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                eventsList.add(Transformer.transform(dataSnapshot.getValue(AcademicEventDTO.class)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Log.i(TAG, "onCancelled: Failed to load data");
            }
        };

        ChildEventListener listenerGame = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                eventsList.add(Transformer.transform(dataSnapshot.getValue(GameEventDTO.class)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Log.i(TAG, "onCancelled: Failed to load data");
            }
        };

        ChildEventListener listenerMusic = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                eventsList.add(Transformer.transform(dataSnapshot.getValue(MusicEventDTO.class)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Log.i(TAG, "onCancelled: Failed to load data");
            }
        };


        ChildEventListener listenerSocial = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                eventsList.add(Transformer.transform(dataSnapshot.getValue(SocialEventDTO.class)));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Log.i(TAG, "onCancelled: Failed to load data");
            }
        };

        eventsRef.child("sport-event").addChildEventListener(listenerSport);
        eventsRef.child("academic-event").addChildEventListener(listenerAcademic);
        eventsRef.child("game-event").addChildEventListener(listenerGame);
        eventsRef.child("social-event").addChildEventListener(listenerSocial);
        eventsRef.child("music-event").addChildEventListener(listenerMusic);


    }





}
