package com.example.letshang;

import com.example.letshang.model.Event;
import com.example.letshang.model.Participant;
import com.example.letshang.model.Preference;
import com.example.letshang.model.SportEvent;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParticipantUnitTest {

    @Test
    public void lastEventShouldReturnLastEvent(){

        // prepare the data
        Preference preference = new Preference(null, null);
        ArrayList<Event> events = new ArrayList<Event>();
        Event event1 = new SportEvent("Evento 1",null, new Date(2000,01,01),
                null, 0, 0, null, null, null);

        Event event2 = new SportEvent("Evento 2",null, new Date(2020,01,01),
                null, 0, 0, null, null, null);

        Event event3 = new SportEvent("Evento 3",null, new Date(1900,01,01),
                null, 0, 0, null, null, null);

        Event event4 = new SportEvent("Evento 4",null, new Date(2010,01,01),
                null, 0, 0, null, null, null);

        events.add(event1);
        events.add(event2);
        events.add(event3);
        events.add(event4);



        Participant p = new Participant("Juan Perez","juan@perez.com",new Date(1998, 15,06),
                "318938928", "camiloserr", "camilo.serr", "cams", null, null, preference, events);

        // Execute operation
        Event answer = p.lastEvent();

        // Assert
        assertEquals(event2, answer);
    }

    @Test
    public void lastEventShouldReturnNullWithEmptyEventList(){

        // prepare the data
        Preference preference = new Preference(null, null);
        ArrayList<Event> events = new ArrayList<Event>();

        Participant p = new Participant("Juan Perez","juan@perez.com",new Date(1998, 15,06),
                "318938928", "camiloserr", "camilo.serr", "cams", null, null, preference, events);

        // Execute operation
        Event answer = p.lastEvent();

        // Assert
        assertNull( answer);
    }
}
