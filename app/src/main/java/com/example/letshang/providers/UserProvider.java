package com.example.letshang.providers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.letshang.DTO.ParticipantDTO;
import com.example.letshang.DTO.Transformer;
import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Host;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.example.letshang.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;


//*******************
// Esta clase tiene valores de mentiras, despues esos
// metodos se van a cambiar para que traigan datos de firebase
//*******************

//singleton class
public class UserProvider {

    private User currentUser;
    private ArrayList<Event> myEvents;
    private static UserProvider instance = null;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("users");
    private static final  String TAG = "UserProvider";
    final ArrayList<String> eventsKeys = new ArrayList<>();



    public static UserProvider getInstance(){
        if(instance == null){
            instance = new UserProvider();
        }
        return instance;
    }




    /**
     * initializest user
     */
    private UserProvider(){

        if(mAuth.getUid() != null){
            getCurrentUserFromDB();
        }
    }


    private void getCurrentUserFromDB(){
        // Read from the database
        userRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUser = Transformer.transform(dataSnapshot.getValue(ParticipantDTO.class));
                currentUser = (Participant)currentUser;

                //TODO: cambiar esto por el metodo que de verdad hace la query por los eventos pasados

                ((Participant) currentUser).setPastEvents( new ArrayList<Event>());
                if(((Participant) currentUser).getPreferences().getInterests() == null){
                    ((Participant) currentUser).getPreferences().setInterests(new ArrayList<String>());
                }
                Log.d(TAG, "CurrentUser = " + currentUser.getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void updateCurrentUser(User user){

        currentUser = user;
    }

    /**
     *
     * @return user currently logged into the aplication
     */
    public User getCurrentUser(){
        if(currentUser ==null){
            getCurrentUserFromDB();
        }
        return currentUser;

    }

    /**
     *
     * @param uID user ID
     * @return user with uID
     */
    public User getUserByID(String uID){
        // ahorita retorna null, esto deberia hacer una query en la DB
        return null;
    }


    /**
     * Helper function
     * @return list of events
     */
    private List<Event> generatePastEvents(){
        List<Event> ans = new ArrayList<Event>();

        /**ArrayList<String> tags = new ArrayList<String>();
        tags.add("futbol");
        tags.add("parque");
        tags.add("juvenil");

        ans.add(new SportEvent("Partido de futbol",
                "Clase para niños y adolecentes entre 10 y 14 años. " +
                        " Exelente forma de pasar el fin de semana! Terminamos la clase con un partido amistoso.",
                new GregorianCalendar(2020, 10, 11, 10, 00),
                new GregorianCalendar(2020,10,11, 15,00),
                10000, 100, tags, "Futbol", SportEventLevel.BEGINNER,
                11, new LatLng(4.700234, -74.059253))
        );*/
        return ans;
    }


    public void setUser(Participant user, String uid) {
        Log.i(TAG, "setUser: " + uid);
        userRef.child(uid).setValue(Transformer.transform(user));
    }

    private void getUserPastEvents(){
        final ArrayList<Event> pastEvents = new ArrayList<>();

        ((Participant) currentUser).setPastEvents(pastEvents);
        DatabaseReference pastEventsRef = database.getReference().child("user-event-list").child(mAuth.getUid());
        pastEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: leyendo los eventos del usuario");
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    eventsKeys.add(data.getValue(String.class));
                }

                fillUserPastEvents();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    private void fillUserPastEvents() {
        for(String i: eventsKeys) {
            DatabaseReference sportEventsRef = database.getReference().child("event").child("sport-event");
            DatabaseReference academicEventRef = database.getReference().child("event").child("academic-event");
            DatabaseReference musicEventRef = database.getReference().child("event").child("music-event");
            DatabaseReference socialEventRef = database.getReference().child("event").child("social-event");
            DatabaseReference cameEventRef = database.getReference().child("event").child("game-event");
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        eventsKeys.add(data.getValue(String.class));
                    }

                    fillUserPastEvents();


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            };
        }

    }
}
