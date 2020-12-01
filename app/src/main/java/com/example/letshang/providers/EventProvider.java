package com.example.letshang.providers;

import android.location.Location;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.letshang.DTO.AcademicEventDTO;
import com.example.letshang.DTO.EventDTO;
import com.example.letshang.DTO.GameEventDTO;
import com.example.letshang.DTO.HostDTO;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


// Singleton class
public class EventProvider {

    private List<Event> eventsList;
    private static EventProvider instance = null;
    private Map<String, String> hostName;
    private UserProvider userProvider;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference eventsRef = database.getReference("events");
    private DatabaseReference hostsRef = database.getReference("host-event");
    private DatabaseReference userEventsRef = database.getReference("user-event-list");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

    private EventProvider() {
        eventsList = new ArrayList<>();
        hostName = new HashMap<>();
        userProvider = userProvider.getInstance();
        addEventListeners();
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
    public Event getEventByID(String eventID){
        for(Event e: eventsList){
            if(e.getID().equals(eventID)){
                return e;
            }
        }
        return null;
    }


    /**
     * creates an event and puts it in the DB
     * @param event
     * @param hostID
     */
    public void createEvent(Event event, String hostID){
        String eventID = addEventToFirebase(event);
        addEventHostToFirebase(eventID, hostID);
    }

    private void addEventHostToFirebase(String eventID, String hostID) {
        Log.i(TAG, "addEventHostToFirebase: " + eventID);
        hostsRef.child(hostID).child(eventID).setValue(true);

    }

    private String addEventToFirebase(Event event) {

        mAuth.getInstance();
        String myName = userProvider.getCurrentUser().getName();

        DatabaseReference ref;
        String key = null;

        if(event.getClass() == SportEvent.class){
            Log.i(TAG, "addEventToFirebase: sport");
            ref = eventsRef.child("sport-event");
            key = ref.push().getKey();
            ref.child(key).setValue(Transformer.transform((SportEvent)event,  myName));
        }
        else if(event.getClass() == AcademicEvent.class){
            Log.i(TAG, "addEventToFirebase: academic");

            ref = eventsRef.child("academic-event");
            key = ref.push().getKey();
            ref.child(key).setValue(Transformer.transform((AcademicEvent) event,  myName));

        }
        else if(event.getClass() == MusicEvent.class){
            Log.i(TAG, "addEventToFirebase: music");

            ref = eventsRef.child("music-event");
            key = ref.push().getKey();
            ref.child(key).setValue(Transformer.transform((MusicEvent) event,  myName));
        }
        else if(event.getClass() == SocialEvent.class){
            Log.i(TAG, "addEventToFirebase: spcial");

            ref = eventsRef.child("social-event");
            key = ref.push().getKey();
            ref.child(key).setValue(Transformer.transform((SocialEvent) event,  myName));

        }
        else if(event.getClass() == GameEvent.class){
            Log.i(TAG, "addEventToFirebase: game");

            ref = eventsRef.child("game-event");
            key = ref.push().getKey();
            ref.child(key).setValue(Transformer.transform((GameEvent) event,  myName));

        }

        hostName.put(key, myName);

        return key;
    }


    /**
     * returns the name of the host associated to the event ID
     * Does not return the whloe Host object to save
     * @param eventID
     * @return
     */
    public String getEventHostName(String eventID){
        //Log.i(TAG, "hostnameMap: " + hostName.toString());
        if(hostName.containsKey(eventID)){
            //Log.i(TAG, "getEventHostName: encontrado ->" + hostName.get(eventID));
           return hostName.get(eventID);
        }
        //Log.i(TAG, "getEventHostName: nombre no encontrado");
        return null;


        ///////////

    }


    /**
     *     Sets a listener for all 5 database references to listen for all types of events
     */
    private void addEventListeners(){


        ChildEventListener listenerSport = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                SportEventDTO dto = dataSnapshot.getValue(SportEventDTO.class);
                hostName.put(dataSnapshot.getKey() , dto.getHostName());
                SportEvent se = Transformer.transform(dto);
                se.setID(dataSnapshot.getKey());
                eventsList.add(se);
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
                AcademicEventDTO dto = dataSnapshot.getValue(AcademicEventDTO.class);
                hostName.put(dataSnapshot.getKey() , dto.getHostName());
                AcademicEvent e = Transformer.transform(dto);
                e.setID(dataSnapshot.getKey());

                eventsList.add(e);
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
                GameEventDTO dto = dataSnapshot.getValue(GameEventDTO.class);
                hostName.put(dataSnapshot.getKey() , dto.getHostName());
                GameEvent e = Transformer.transform(dto);
                e.setID(dataSnapshot.getKey());
                eventsList.add(e);            }

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
                MusicEventDTO dto = dataSnapshot.getValue(MusicEventDTO.class);
                hostName.put(dataSnapshot.getKey() , dto.getHostName());
                MusicEvent e = Transformer.transform(dto);
                e.setID(dataSnapshot.getKey());
                eventsList.add(e);            }

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
                SocialEventDTO dto = dataSnapshot.getValue(SocialEventDTO.class);
                hostName.put(dataSnapshot.getKey() , dto.getHostName());
                SocialEvent e = Transformer.transform(dto);
                e.setID(dataSnapshot.getKey());
                eventsList.add(e);            }

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


    /**
     * adds the id of the event to the list of current events in firebase
     * @param idUser
     * @param idEvento
     */
    public void inscribirEvento(String idUser, String idEvento){
        DatabaseReference userEventList = database.getReference("user-event-list").child(idUser).child(idEvento);
        userEventList.setValue(true);

    }

    /**
     * quita de firebase la relacion del usuario con el evento
     * @param event
     */
    public void desinscribirEvento(Event event) {
        userProvider.desinscribirEvento(event);
        userEventsRef.child(mAuth.getUid()).child(event.getID()).setValue(null);

    }
}
