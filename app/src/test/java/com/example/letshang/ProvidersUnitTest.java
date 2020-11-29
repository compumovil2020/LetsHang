package com.example.letshang;

import com.example.letshang.model.Event;
import com.example.letshang.model.Host;
import com.example.letshang.model.SportEvent;
import com.example.letshang.model.User;
import com.example.letshang.providers.EventProvider;
import com.example.letshang.providers.UserProvider;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

public class ProvidersUnitTest {

    // esto es porque me da pereza crear un evento entonces solo creo un mock
    @Mock
    SportEvent mockEvent;

    @Mock
    Host mockHost;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    /*@Test
    public void UserProviderShouldReturnCurrentUser(){
        UserProvider up = UserProvider.getInstance();

        User u = up.getCurrentUser();
        assertNotNull(u);
    }

     */

    //@Test
    /*public void EventProviderShouldAddEvent(){
        EventProvider ep = EventProvider.getInsatance();


       ep.createEvent(mockEvent, mockHost);

       int ID = mockEvent.getID();

       Event e = ep.getEventByID(ID);

       assertEquals(e,mockEvent);

    }*/
}
