package com.example.letshang.model;


import java.util.EnumMap;

public class Preference {

    /**
     * likeness score for every kind of event
     *
     */
    private EnumMap<EventsEnum , Double> categories;

    /**
     * contains key words of potential events
     */
    private String[] interests;

    public Preference(EnumMap<EventsEnum , Double> categories, String[] interests) {
        this.categories = categories;
        this.interests = interests;
    }

    /**
     * sets the likeness for a specific event type
     * @param eventType
     * @param score
     */
    public void setCategoryScore(EventsEnum eventType, double score){
        this.categories.put(eventType, score);
    }

    /**
     * gets likeness for a specific event type
     * @param category
     * @return  likeness for a specific event type
     */
    public double getCategoryScore(EventsEnum category){
        try{
            return this.categories.get(category).doubleValue();
        }
        catch (Exception e){
            return 0;
        }
    }

}
