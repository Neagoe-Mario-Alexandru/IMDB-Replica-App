package org.example;

public class Rating {
    private String username;
    private int rating;
    private String comment;

    public Rating(){

    }

    public Rating(String username, int rating, String comment){
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }


    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String toString(){
        return "Username: " + this.username + "\n" + "Rating: " + this.rating + "\n" + "Comment: " + this.comment + "\n";
    }
}
