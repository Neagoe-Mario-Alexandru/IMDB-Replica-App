package org.example;



public class Regular extends User implements RequestsManager{


    @Override
    public void createRequest(Request r) {
        requests.add(r);
    }


    @Override
    public void removeRequest(Request r) {
        requests.remove(r);
    }
    public void addRating(String username, int rateInt, String commentReview, Production production){
        Rating r = new Rating();
        r.setUserName(username);
        r.setRating(rateInt);
        r.setComment(commentReview);
        production.addRatingProd(r);
    }


}
