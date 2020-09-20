package com.example.letshang.model;


public class Preference {
    /**
     * likeness score for every kind of event
     *
     */
    private double[] categories;

    /**
     * contains key words of potential events
     */
    private String[] interests;

    public Preference(double[] categories, String[] interests) {
        this.categories = categories;
        this.interests = interests;
    }

}
