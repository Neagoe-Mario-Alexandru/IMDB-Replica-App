package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//DONE

public class Actor implements AllTypes {

    private String name;
    private List<Map.Entry<String, String>> performances = new ArrayList<>();

    private String biography;

    public Actor(String name, List <Map.Entry<String, String>> moviePairs, String biography){
        this.name = name;
        this.performances = moviePairs;
        this.biography = biography;
    }
    public Actor(String name){
        this.name = name;
    }

    public Actor() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public void setMoviePair(String title, String type) {
        Map.Entry<String, String> moviePair = Map.entry(title, type);
        performances.add(moviePair);
    }

    public void deleteMoviePair(String title, String type) {
        Map.Entry<String, String> moviePair = Map.entry(title, type);
        performances.remove(moviePair);
    }

    public List<Map.Entry<String, String>> getMoviePair() {
        return Collections.unmodifiableList(performances);
    }

    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        if (biography == null){
            this.biography = "No biography for this actor, unfortunately";
        }
        else{
            this.biography = biography;
        }
    }

    @Override
    public String toString() {
        return "Name: " + getName() + "\n" + "Biography: " + getBiography()  + "\n" + "Productions: " + getMoviePair() + "\n";
    }

    @Override
    public String giveString() {
        return getName();
    }
}
