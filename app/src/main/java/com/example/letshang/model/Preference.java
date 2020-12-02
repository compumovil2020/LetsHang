package com.example.letshang.model;


import java.util.EnumMap;
import java.util.List;

public class Preference {

    /**
     * likeness score for every kind of event
     *
     */
    private EnumMap<EventsEnum , Double> categories;

    /**
     * contains key words of potential events
     */
    private List<String> interests;

    public Preference(EnumMap<EventsEnum , Double> categories, List<String> interests) {
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

    public EnumMap<EventsEnum, Double> getCategories() {
        return categories;
    }

    public void setCategories(EnumMap<EventsEnum, Double> categories) {
        this.categories = categories;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }


    @Override
    public String toString() {
        return "Preference{" +
                "categories=" + categories +
                ", interests=" + interests +
                '}';
    }
}
