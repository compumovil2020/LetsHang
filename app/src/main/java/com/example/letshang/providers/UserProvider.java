package com.example.letshang.providers;

import com.example.letshang.model.Event;
import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.SportEventLevel;
import com.example.letshang.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;


//*******************
// Esta clase tiene valores de mentiras, despues esos
// metodos se van a cambiar para que traigan datos de firebase
//*******************

public class UserProvider {

    /**
     *
     * @return user currently logged into the aplication
     */
    public User getCurrentUser(){

        List<Event> pastEvents = generateEvents();
        EnumMap<EventsEnum , Double> mapa = new EnumMap<EventsEnum, Double>(EventsEnum.class);
        mapa.put(EventsEnum.ACADEMIC , 2.6);
        mapa.put(EventsEnum.SPORTS , 4.96);
        mapa.put(EventsEnum.PARTY , 3.7);
        mapa.put(EventsEnum.MUSIC , 4.37);
        Preference preferences = new Preference(mapa , new String[]{"futbol" , "parque" , "yoga", "fit"});

        return new Participant("Juan Perez","juan@perez.com",
                new Date(1998, 5,5),"3177963053",
                "juan.perez","perez99","@perez",
                null,null,preferences,pastEvents );
    }

    /**
     * Helper function
     * @return list of events
     */
    private List<Event> generateEvents(){
        List<Event> ans = new ArrayList<Event>();

        ArrayList<String> tags = new ArrayList<String>();
        tags.add("futbol");
        tags.add("parque");
        tags.add("juvenil");

        ans.add(new SportEvent("Partido de futbol",
                "Clase para niños y adolecentes entre 10 y 14 años.  Exelente forma de pasar el fin de semana! Terminamos la clase con un partido amistoso.",
                new Date(2020, 10, 11, 10, 00), new Date(2020,10,11, 15,00),
                10000, 100, tags, "Futbol", SportEventLevel.BEGINER, 11)
        );
        return ans;
    }
}
