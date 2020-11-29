package com.example.letshang;

import androidx.lifecycle.Lifecycle;

import com.example.letshang.model.EventsEnum;
import com.example.letshang.model.Preference;

import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.Assert.assertEquals;

public class PreferenceUnitTest {

    private final double precision = 0.001;

    @Test
    public void getCategoryScoreShouldReturnScore(){

        // prepare data
        EnumMap<EventsEnum , Double> map = new EnumMap<EventsEnum, Double>(EventsEnum.class);
        map.put(EventsEnum.MUSIC , 3.0);
        map.put(EventsEnum.ACADEMIC , 2.8);
        map.put(EventsEnum.SOCIAL , 4.2);
        ArrayList<String>  tags  =  new ArrayList<>();
        tags.add("tag 1");
        tags.add("tag 2");
        tags.add("tag 3");

        Preference p = new Preference(map ,tags);

        // execute
        double answer = p.getCategoryScore(EventsEnum.MUSIC);

        // assert
        assertEquals(answer, 3.0, precision);
    }

    @Test
    public void getCategoryScoreShouldWithEmptyMap(){

        // prepare data
        EnumMap<EventsEnum , Double> map = new EnumMap<EventsEnum, Double>(EventsEnum.class);

        ArrayList<String>  tags  =  new ArrayList<>();
        tags.add("tag 1");
        tags.add("tag 2");
        tags.add("tag 3");
        Preference p = new Preference(map ,tags);

        // execute
        double answer = p.getCategoryScore(EventsEnum.MUSIC);

        // assert
        assertEquals(answer, 0.0, precision);
    }
}
