package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Request {

    private RequestTypes type;
    private LocalDateTime createdDate;

    private String nameActorProduction;

    private String description;

    private String username;

    private String to;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public Request() {
    }

    public RequestTypes getType() {
        return type;
    }

    public void setType(RequestTypes type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate(){
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate;
    }

    public String getNameActorProduction(){
        return nameActorProduction;
    }

    public void setNameActorProduction(String nameActorProduction){
        this.nameActorProduction = nameActorProduction;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void stringToLDT(String date){
        if (date != null){
            this.createdDate = LocalDateTime.parse(date, formatter);
        }
    }

    public static class RequestsHolder{
        private List<Request> adminRequests = new ArrayList<>();
        public void addAdminRequest (Request r){
            adminRequests.add(r);
        }
        public List<Request> getAdminRequests() {
            return adminRequests;
        }

    }

    public String toString() {
        return "Type: " + getType() + "\n" + "createdDate: " + getCreatedDate()
                + "\n" + "Username" + getUsername() + "\n"
                + "NameActorMovie: " + getNameActorProduction() + "\n"
                + "To: " + getTo() + "\n"
                + "Description: " + getDescription() + "\n";
    }
}
