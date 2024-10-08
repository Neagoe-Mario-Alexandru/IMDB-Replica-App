package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class User {


    void setInformation(Information information) {
        this.information = information;
    }

    public static class Information {
        private Credentials credentials;

        private String name;
        private String country;
        private int age = 0;
        private char gender;
        private LocalDateTime birthDate;

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        private Information (InformationBuilder builder){
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }

        public static Information fromJson(JsonNode jsonNode) {
            InformationBuilder builder = new InformationBuilder(
                    new Credentials(jsonNode.get("credentials").get("email").asText(), jsonNode.get("credentials").get("password").asText()),
                    jsonNode.get("name").asText()
            );

            // Set other properties if they exist in the JSON
            if (jsonNode.has("country")) {
                builder.country(jsonNode.get("country").asText());
            }

            if (jsonNode.has("age")) {
                builder.age(jsonNode.get("age").asInt());
            }

            if (jsonNode.has("gender")) {
                builder.gender(jsonNode.get("gender").asText().charAt(0));
            }

            if (jsonNode.has("birthDate")) {
                builder.birthDate(LocalDateTime.parse(jsonNode.get("birthDate").asText()));
            }

            return builder.build();
        }


        public String getCredEmail(Credentials credentials) {
            return credentials.getEmail();
        }

        public String getCredPassword(Credentials credentials) {
            return credentials.getPassword();
        }

        public Credentials getCred(){
            return credentials;
        }

        public String getNameInfo() {
            return name;
        }

        public String getCountry() {
            return country;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }

        public LocalDateTime getBirth() {
            return birthDate;
        }

        public void setCountry (String country){
            this.country = country;
        }

        public void setAge (int age){
            this.age = age;
        }

        public void setGender (char gender){
            this.gender = gender;
        }

        public void setBirthDate (LocalDateTime birthDate){
            this.birthDate = birthDate;
        }
        public static class InformationBuilder{
            private Credentials credentials;
            private String name;
            private String country; //Opt
            private int age; //Opt
            private char gender; //Opt
            private LocalDateTime birthDate; //Opt

            public InformationBuilder(Credentials credentials, String name){
                this.credentials = credentials;
                this.name = name;
            }

            public InformationBuilder country(String country){
                this.country = country;
                return this;
            }

            public InformationBuilder age(int age){
                this.age = age;
                return this;
            }

            public InformationBuilder gender(char gender){
                this.gender = gender;
                return this;
            }

            public InformationBuilder birthDate(LocalDateTime birthDate){
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(this);
            }


        }
    }

    private Information information;
    private AccountType userType;
    private String username;
    private int experience;

    protected List<Request> requests = new ArrayList<>();


    private List<String> notifications;

    private SortedSet<AllTypes> favorites = new TreeSet<>(new AllTypesComparator());

    public void printFavorites() {
        System.out.println("Favorites:");
        for (AllTypes favorite : favorites) {
            System.out.println(favorite);
        }
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public AllTypes findFavoriteByName(String nameTitle) {
        for (AllTypes prodActor : favorites) {
            if (prodActor.giveString().equals(nameTitle)) {
                return prodActor;
            }
        }
        return null;
    }


    public int removeFavoriteByName(String nameTitle) {
        AllTypes itemToRemove = findFavoriteByName(nameTitle);

        if (itemToRemove != null) {
            favorites.remove(itemToRemove);
            return 1;
        }

        return 0;
    }

    public void addFavorite(AllTypes ActorProd) {
        favorites.add(ActorProd);
    }


    public void setUsername(String username){
        this.username = username;
    }

    public void setExperience(int experience){
        this.experience = experience;
    }

    public int getExperience(){
        return this.experience;
    }

    public AccountType getAccountType(){
        return this.userType;
    }

    public void setUserType(AccountType userType){
        this.userType = userType;
    }

    public LocalDateTime stringToLDT(String date){
        if (date != null){
            return LocalDateTime.parse(date, formatter);
        }
        return null;
    }

    public String getEmail() {
        return information.getCredEmail(information.credentials);
    }
    public String getPassword(){
        return information.getCredPassword(information.credentials);
    }

    public String getInfoName(){
        return information.getNameInfo();
    }

    public String getUsername(){
        return this.username;
    }

    public void addReq(Request r){
        requests.add(r);
    }

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

    public void updateInformationCountry (String country){
        if (this.information == null){
            this.information = new Information.InformationBuilder(new Credentials("", ""), "").country(country).build();
        }else if (this.information.getCountry() == null || this.information.getCountry().isEmpty()){
            this.information.setCountry(country);
        }else{
            this.information.setCountry(country);
        }
    }

    public void updateInformationAge (int age){
        if (this.information == null){
            this.information = new Information.InformationBuilder(new Credentials("", ""), "").age(age).build();
        }else if (this.information.getAge() == 0 || this.information.getCountry().isEmpty()){
            this.information.setAge(age);
        }else{
            this.information.setAge(age);
        }
    }

    public void updateInformationGender (String gender){
        char firstChar = 'U';
        if (gender != null && !gender.isEmpty()) {
            firstChar = gender.charAt(0);
        }
        if (this.information == null){
            this.information = new Information.InformationBuilder(new Credentials("", ""), "").gender(firstChar).build();
        }else{
            this.information.setGender(firstChar);
        }
    }

    public void updateInformationBirthDate (String birth){
        LocalDateTime birthDate;
        birthDate = stringToLDT(birth);
        if (this.information == null){
            this.information = new Information.InformationBuilder(new Credentials("", ""), "").birthDate(birthDate).build();
        }else if (this.information.getBirth() == null){
            this.information.setBirthDate(birthDate);
        }else{
            this.information.setBirthDate(birthDate);
        }
    }

    public void updateInformationCredentialsName (String email, String password, String name){
        if (this.information == null){
            this.information = new Information.InformationBuilder(new Credentials(email, password), name).build();
        }
    }


    public String toString() {
        String aux = "";

        aux += "Username: " + getUsername() + "\n";
        aux += "Experience: " + "\n";
        aux += "Information: \n\n" + "Email: " + getEmail() + "\n";
        aux += "Password: " + getPassword() + "\n";
        aux += "Name: " +  getInfoName() + "\n";
        aux += "Country: " +"\n";
        aux += "Age: " + "\n";
        aux += "Gender: " +  "\n";
        aux += "Birthdate: "  + "\n\n";
        aux += "User type: "  + "\n\n";
        return aux;
    }
}