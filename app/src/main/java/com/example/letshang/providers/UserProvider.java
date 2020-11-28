package com.example.letshang.providers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Host;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.example.letshang.model.User;
import com.google.android.gms.maps.model.LatLng;
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

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = database.getReference("users");
    private static final  String TAG = "UserProvider";




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

        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentUser = dataSnapshot.getValue(User.class);
                Log.d(TAG, "CurrentUser = " + currentUser.getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // este tiene que hacer una query a la base de datos
        // estos datos son quemados
        myEvents = new ArrayList<>();
        EnumMap<EventsEnum , Double> mapa = new EnumMap<EventsEnum, Double>(EventsEnum.class);
        mapa.put(EventsEnum.ACADEMIC , 2.6);
        mapa.put(EventsEnum.SPORTS , 4.96);
        mapa.put(EventsEnum.MUSIC , 4.37);
        Preference preferences = new Preference(mapa , new String[]{"futbol" , "parque" , "yoga", "fit"});

        currentUser = new Participant("Juan Perez","juan@perez.com",
                new GregorianCalendar(1998, 5,5),"3177963053",
                "juan.perez","perez99","@perez",
                null,null,preferences, myEvents );

    }

    public void updateCurrentUser(User user){
        currentUser = user;
    }

    /**
     *
     * @return user currently logged into the aplication
     */
    public User getCurrentUser(){
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

        ArrayList<String> tags = new ArrayList<String>();
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
        );
        return ans;
    }


}
