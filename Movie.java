package org.example;

public class Movie extends Production implements AllTypes {

    private String duration;

    private int releaseYear;

    public Movie(String duration, int release){
        super();
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public Movie() {

    }

    public void setDuration(String duration){
        this.duration = duration;
    }
    public void setReleaseYear(int releaseYear){
        this.releaseYear = releaseYear;
    }
    @Override
    public void displayInfo() {
        System.out.println(duration);
        System.out.println(releaseYear);
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
                + getActors() + "\n" + "Genres: " + getGenres() + "Release Year: " + getReleaseYear() + "\n" + "Plot: " + getPlot() + "\n" + "Ratings: " + getRatings() + "\n";
    }
}
