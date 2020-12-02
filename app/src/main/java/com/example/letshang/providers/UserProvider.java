package com.example.letshang.providers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.letshang.DTO.AcademicEventDTO;
import com.example.letshang.DTO.GameEventDTO;
import com.example.letshang.DTO.MusicEventDTO;
import com.example.letshang.DTO.ParticipantDTO;
import com.example.letshang.DTO.SocialEventDTO;
import com.example.letshang.DTO.SportEventDTO;
import com.example.letshang.DTO.Transformer;
import com.example.letshang.model.AcademicEvent;
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
import com.example.letshang.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static android.content.ContentValues.TAG;


//*******************
// Esta clase tiene valores de mentiras, despues esos
// metodos se van a cambiar para que traigan datos de firebase
//*******************

//singleton class
public class UserProvider {

    private User currentUser;
    private static UserProvider instance = null;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("users");
    private static final  String TAG = "UserProvider";
    private final ArrayList<String> eventsKeys = new ArrayList<>();
    private ArrayList<Event> pastEvents;
    private Set<String> pastEventsKeys;



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
                currentUser.setId(dataSnapshot.getKey());
                currentUser = (Participant)currentUser;


                getUserPastEvents();
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


    public void setUser(Participant user, String uid) {
        Log.i(TAG, "setUser: " + uid);
        userRef.child(uid).setValue(Transformer.transform(user));
    }

    private void getUserPastEvents(){

        pastEvents  = new ArrayList<>();
        ((Participant) currentUser).setPastEvents(pastEvents);
        DatabaseReference pastEventsRef = database.getReference().child("user-event-list").child(mAuth.getUid());

        pastEventsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        pastEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: leyendo los eventos del usuario");
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    eventsKeys.add(data.getKey());
                }

                fillUserPastEvents();
                Log.i(TAG, "lista de llaves: " + eventsKeys.toString());


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }

    private void fillUserPastEvents() {

        pastEventsKeys = new TreeSet<>();

        for(String k: eventsKeys) {
            Log.i(TAG, "buscando la llave: " + k);
            DatabaseReference sportEventsRef = database.getReference().child("events").child("sport-event").child(k);
            DatabaseReference academicEventRef = database.getReference().child("events").child("academic-event").child(k);
            DatabaseReference musicEventRef = database.getReference().child("events").child("music-event").child(k);
            DatabaseReference socialEventRef = database.getReference().child("events").child("social-event").child(k);
            DatabaseReference gameEventRef = database.getReference().child("events").child("game-event").child(k);
            ValueEventListener sportListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SportEventDTO dto = dataSnapshot.getValue(SportEventDTO.class);
                    if(dto == null){
                        return;
                    }
                    Log.i(TAG, "onDataChange: sport" + dataSnapshot.getKey());

                    SportEvent e =Transformer.transform(dto);
                    e.setID(dataSnapshot.getKey());
                    if(!pastEventsKeys.contains(e.getID())) {
                        pastEvents.add(e);
                        Log.i(TAG, "la lista tiene tam " + pastEvents.size());

                        pastEventsKeys.add(e.getID());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            };

            ValueEventListener academicListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.i(TAG, "snapshot: " + dataSnapshot.getValue().toString());
                    AcademicEventDTO dto = dataSnapshot.getValue(AcademicEventDTO.class);
                    if(dto == null){
                        return;
                    }
                    Log.i(TAG, "onDataChange: academic" + dataSnapshot.getKey());

                    AcademicEvent e = Transformer.transform(dto);
                    e.setID(dataSnapshot.getKey());

                    if(!pastEventsKeys.contains(e.getID())) {
                        pastEvents.add(e);
                        Log.i(TAG, "la lista tiene tam " + pastEvents.size());

                        pastEventsKeys.add(e.getID());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            };

            ValueEventListener musicListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MusicEventDTO dto = dataSnapshot.getValue(MusicEventDTO.class);
                    if(dto == null){
                        return;
                    }
                    Log.i(TAG, "onDataChange: music" + dataSnapshot.getKey());

                    MusicEvent e = Transformer.transform(dto);
                    e.setID(dataSnapshot.getKey());

                    if(!pastEventsKeys.contains(e.getID())) {
                        pastEvents.add(e);
                        Log.i(TAG, "la lista tiene tam " + pastEvents.size());

                        pastEventsKeys.add(e.getID());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            };

            ValueEventListener socialListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SocialEventDTO dto = dataSnapshot.getValue(SocialEventDTO.class);
                    if(dto == null){
                        return;
                    }
                    Log.i(TAG, "onDataChange: social" + dataSnapshot.getKey());

                    SocialEvent e= Transformer.transform(dto);
                    e.setID(dataSnapshot.getKey());

                    if(!pastEventsKeys.contains(e.getID())) {
                        pastEvents.add(e);
                        Log.i(TAG, "la lista tiene tam " + pastEvents.size());

                        pastEventsKeys.add(e.getID());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            };

            ValueEventListener gameListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GameEventDTO dto = dataSnapshot.getValue(GameEventDTO.class);
                    if(dto == null){
                        return;
                    }
                    Log.i(TAG, "onDataChange: game" + dataSnapshot.getKey());

                    GameEvent e = Transformer.transform(dto);
                    e.setID(dataSnapshot.getKey());

                    if(!pastEventsKeys.contains(e.getID())) {
                        pastEvents.add(e);
                        Log.i(TAG, "la lista tiene tam " + pastEvents.size());

                        pastEventsKeys.add(e.getID());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            };

            sportEventsRef.addListenerForSingleValueEvent(sportListener);
            gameEventRef.addListenerForSingleValueEvent(gameListener);
            socialEventRef.addListenerForSingleValueEvent(socialListener);
            musicEventRef.addListenerForSingleValueEvent(musicListener);
            academicEventRef.addListenerForSingleValueEvent(academicListener);
        }

    }

    public void desinscribirEvento(Event event) {
        Log.i(TAG, "tam antes: " + pastEvents.size());

        for (int i = 0 ; i < pastEvents.size() ; ++i){
            if(pastEvents.get(i).getID().equals(event.getID())){
                pastEvents.remove(i);
            }
        }
        Log.i(TAG, "tam despues: " + pastEvents.size());


    }
}
