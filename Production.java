package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Production implements Comparable, AllTypes {
    private String title;
    private List<String> directors = new ArrayList<>();
    private List<String> actors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();

    private List<Rating> ratings = new ArrayList<>();
    private String plot;

    private double averageRating;

    public Production (){
    }

    protected Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String plot, double AverageRating) {
        this.title = title;
        this.directors = new ArrayList<String>();
        this.actors = new ArrayList<String>();
        this.genres = new ArrayList<Genre>();
        this.ratings = new ArrayList<Rating>();
        this.plot = plot;
        this.averageRating = averageRating;
    }

    public Production (String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public void setAverageRating(double plot){
        this.averageRating = averageRating;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void addDirectors(String director){
        if (director != null){
            directors.add(director);
        }
    }

    public void addActors(String actor){
        if (actor != null){
            actors.add(actor);
        }
    }

    public void setPlot(String plot){
        this.plot = plot;
    }

    public String getPlot(){
        return this.plot;
    }

    public void addGenres(Genre genre){
        if (genre != null){
            genres.add(genre);
        }
    }

    public abstract void displayInfo();

    public void addRatingProd(Rating rating) {
        ratings.add(rating);
    }

    public int compareTo(Object o){
        return this.title.compareTo(((Production)o).title);
    }
}
