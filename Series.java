package org.example;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Series extends Production implements AllTypes {
    private int releaseYear;

    private int numSeasons;

    private Map<String, List<Episode>> episodeMap = new HashMap<>();

    public Series(){

    }

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String subject, double grade) {
        super(title, directors, actors, genres, ratings, subject, grade);
    }

    public void setReleaseYear(int releaseYear){
        this.releaseYear = releaseYear;
    }

    public void setNumSeasons(int numSeasons){
        this.numSeasons = numSeasons;
    }


    public void addSeason(String season, List<Episode> episodes) {
        episodeMap.put(season, episodes);
    }

    @Override
    public void displayInfo() {
        System.out.println(releaseYear);
        System.out.println(numSeasons);
    }

    @Override
    public String giveString() {
        return getTitle();
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String toString() {
        return "Title: " + getTitle() + "\n" + "Directors: "+ getDirectors() + "\n" + "Actors: "
                + getActors() + "\n" + "Genres: " + getGenres() + "\n" + "Release Year: " + getReleaseYear() + "\n" + "Episodes: " +episodeMap + "\n" + "Ratings: " + getRatings() + "\n";
    }
}
