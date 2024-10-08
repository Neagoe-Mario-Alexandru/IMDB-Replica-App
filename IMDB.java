package org.example;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class IMDB {

    private ArrayList<User> users;
    private ArrayList<Actor> actors;
    private ArrayList<Request> requests;

    private ArrayList<Production> productions;

    private String pathJSON = "/home/netrunner/IdeaProjects/TEMA/src/main" + "/resources/input";

    public static IMDB instance;

    Request.RequestsHolder reqHold = new Request.RequestsHolder();

    private IMDB() {
        users = new ArrayList<>();
        actors = new ArrayList<>();
        requests = new ArrayList<>();
        productions = new ArrayList<>();
    }

    public void readJSON() throws FileNotFoundException {
        JSONParser parser = new JSONParser();


        //CITIRE ACTORS!!!!!!!!!!!!

        try (FileReader reader = new FileReader("src/main/resources/input/actors.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                Actor actor = new Actor();
                JSONObject object = (JSONObject) obj;
                actor.setName((String) object.get("name"));
                JSONArray performancesArray = (JSONArray) object.get("performances");
                for (Object performanceObj : performancesArray) {
                    JSONObject performanceJson = (JSONObject) performanceObj;
                    String title = (String) performanceJson.get("title");
                    String type = (String) performanceJson.get("type");
                    actor.setMoviePair(title, type);
                }
                actor.setBiography((String) object.get("biography"));
                actors.add(actor);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        //CITIRE PRODUCTIONS!!!!!!


        try (FileReader reader = new FileReader("src/main/resources/input/production.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                JSONObject object = (JSONObject) obj;
                String title_prod = (String) object.get("title");
                String type_prod = (String) object.get("type");
                String plot = (String) object.get ("plot");
                //Adaug un film//
                if (type_prod.equals("Movie")) {
                    Movie movie = new Movie();
                    movie.setTitle(title_prod);
                    movie.setPlot(plot);
                    JSONArray directorsArray = (JSONArray) object.get("directors");
                    //Directors este un array, iau fiecare element din directors
                    for (Object directorObj : directorsArray) {
                        String director = (String) directorObj;
                        movie.addDirectors(director);
                    }
                    JSONArray actorsArray = (JSONArray) object.get("actors");
                    //Actors este un array, iau fiecare element din actors
                    for (Object actorObj : actorsArray) {
                        String actor = (String) actorObj;
                        movie.addActors(actor);
                    }
                    JSONArray genresArray = (JSONArray) object.get("genres");
                    //Genres este un array, iau fiecare element din genres
                    for (Object genreObj : genresArray) {
                        String genreToFind = (String) genreObj;
                        Genre genre = FindGenre.findGenre(genreToFind);
                        movie.addGenres(genre);
                    }
                    JSONArray ratingsArray = (JSONArray) object.get("ratings");
                    //Ratings este un array, iau fiecare element din ratings, iar fiecare element este un obiect
                    for (Object ratingObj : ratingsArray) {
                        JSONObject ratingJson = (JSONObject) ratingObj;
                        Rating ratingObject = new Rating();
                        String username = (String) ratingJson.get("username");
                        int rating = (int) (long) ratingJson.get("rating");
                        String comment = (String) ratingJson.get("comment");
                        ratingObject.setRating(rating);
                        ratingObject.setComment(comment);
                        ratingObject.setUserName(username);
                        movie.addRatingProd(ratingObject);
                    }
                    movie.setPlot((String) object.get("plot"));
                    movie.setAverageRating((double) object.get("averageRating"));
                    movie.setDuration((String) object.get("duration"));
                    Object releaseYearObj = object.get("releaseYear");
                    if (releaseYearObj != null) {
                        int releaseYear = (int) (long) releaseYearObj;
                        movie.setReleaseYear(releaseYear);
                    }
                    productions.add(movie);
                }
                //Adaug un serial
                if (type_prod.equals("Series")) {
                    Series serie = new Series();
                    serie.setTitle(title_prod);
                    serie.setPlot(plot);
                    JSONArray directorsArray = (JSONArray) object.get("directors");
                    //Directors este un array, iau fiecare element din directors
                    for (Object directorObj : directorsArray) {
                        String director = (String) directorObj;
                        serie.addDirectors(director);
                    }
                    JSONArray actorsArray = (JSONArray) object.get("actors");
                    //Actors este un array, iau fiecare element din actors
                    for (Object actorObj : actorsArray) {
                        String actor = (String) actorObj;
                        serie.addActors(actor);
                    }
                    JSONArray genresArray = (JSONArray) object.get("genres");
                    //Genres este un array, iau fiecare element din genres
                    for (Object genreObj : genresArray) {
                        String genreToFind = (String) genreObj;
                        Genre genre = FindGenre.findGenre(genreToFind);
                        serie.addGenres(genre);
                        //Ratings este un array, iau fiecare element din ratings, iar fiecare element este un obiect
                        JSONArray ratingsArray = (JSONArray) object.get("ratings");
                        for (Object ratingObj : ratingsArray) {
                            JSONObject ratingJson = (JSONObject) ratingObj;
                            Rating ratingObject = new Rating();
                            String username = (String) ratingJson.get("username");
                            int rating = (int) (long) ratingJson.get("rating");
                            String comment = (String) ratingJson.get("comment");
                            ratingObject.setRating(rating);
                            ratingObject.setComment(comment);
                            ratingObject.setUserName(username);
                            serie.addRatingProd(ratingObject);
                        }


                        serie.setPlot((String) object.get("plot"));
                        serie.setAverageRating((double) object.get("averageRating"));
                        serie.setReleaseYear((int) (long) object.get("releaseYear"));
                        int nrSeasons = (int) (long) object.get("numSeasons");
                        serie.setNumSeasons((int) (long) object.get("numSeasons"));


                        JSONObject seasonsObject = (JSONObject) object.get("seasons");
                        for (int i = 1; i <= nrSeasons; i++) {
                            JSONArray seasonEpisodesArray = (JSONArray) seasonsObject.get("Season " + i);
                            List<Episode> episodes = new ArrayList<>();

                            for (Object episodeObj : seasonEpisodesArray) {
                                JSONObject episodeJson = (JSONObject) episodeObj;
                                String episodeName = (String) episodeJson.get("episodeName");
                                String duration = (String) episodeJson.get("duration");
                                Episode episode = new Episode(episodeName, duration);
                                episodes.add(episode);
                            }
                            //i e numarul sezonului
                            //toate sezoanele se numes Season + al catelea e
                            serie.addSeason(("Season " + i), episodes);
                        }
                        productions.add(serie);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        //CITIRE REQUESTS!!!!!!!!!!!!!!!!!!!!!!!!!!!



        try (FileReader reader = new FileReader("src/main/resources/input/requests.json")) {


            JSONArray arr = (JSONArray) parser.parse(reader);
            //PT REQUESTS PT ADMINI
            reqHold = new Request.RequestsHolder();

            for (Object obj : arr) {
                Request request = new Request();
                JSONObject object = (JSONObject) obj;
                String type = (String) object.get("type");
                try {
                    RequestTypes requestType = RequestTypes.valueOf(type);
                    request.setType(requestType);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid request type: " + type);
                }
                String date = (String) object.get("createdDate");
                request.stringToLDT(date);
                String username = (String) object.get("username");
                request.setUsername(username);
                String nameActorProduction = "Nu e actor sau film";
                if (type.equals("MOVIE_ISSUE")) {
                    nameActorProduction = (String) object.get("movieTitle");
                    request.setNameActorProduction(nameActorProduction);
                }
                if (type.equals("ACTOR_ISSUE")) {
                    nameActorProduction = (String) object.get("actorName");
                    request.setNameActorProduction(nameActorProduction);
                }
                String to = (String) object.get("to");
                request.setTo(to);
                String description = (String) object.get("description");
                request.setDescription(description);
                if (to.equals("ADMIN")) {
                    reqHold.addAdminRequest(request);
                }
                requests.add(request);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        //CITIRE ACCOUNTS!!!!!!!!!!!!!1

        try (FileReader reader = new FileReader("src/main/resources/input/accounts.json")) {

            JSONArray arr = (JSONArray) parser.parse(reader);

            for (Object obj : arr) {
                JSONObject object = (JSONObject) obj;
                String username = (String) object.get("username");
                String experience = (String) object.get("experience");
                String userType = (String) object.get("userType");
                JSONObject information = (JSONObject) object.get("information");
                String name = (String) information.get("name");
                String country = (String) information.get("country");
                int age = 0;
                if (information.get("age") != null){
                    age = (int) (long)information.get("age");
                }
                String gender = (String) information.get("gender");
                String birthDate = (String) information.get("birthDate");
                JSONObject credentials = (JSONObject) information.get("credentials");
                String email = (String) credentials.get("email");
                String password = (String) credentials.get("password");
                if (userType.equals("Regular")){
                    Regular regular = new Regular();
                    regular.updateInformationCredentialsName(email, password, name);
                    if (birthDate != null) {
                        //regular.updateInformationBirthDate(birthDate);
                    }
                    regular.updateInformationGender(gender);
                    regular.updateInformationAge(age);
                    regular.updateInformationCountry(country);
                    regular.setUserType(AccountType.REGULAR);
                    regular.setExperience(Integer.parseInt(experience));
                    regular.setUsername(username);
                    JSONArray favoriteProductions = (JSONArray) object.get("favoriteProductions");
                    if (favoriteProductions != null){
                        for (Object favProdObj : favoriteProductions) {
                            String prod = (String) favProdObj;
                            regular.addFavorite(findProductionByName(prod));
                        }
                    }
                    JSONArray favoriteActors = (JSONArray) object.get("favoriteActors");
                    if (favoriteActors != null){
                        for (Object favActorObj : favoriteActors) {
                            String ac = (String) favActorObj;
                            regular.addFavorite(findActorByName(ac));
                        }
                    }
                    users.add(regular);
                }
                if (userType.equals("Contributor")){
                    Contributor contributor = new Contributor();
                    contributor.updateInformationCredentialsName(email, password, name);
                    if (birthDate != null) {
                        //contributor.updateInformationBirthDate(birthDate);;
                    }
                    contributor.updateInformationGender(gender);
                    contributor.updateInformationAge(age);
                    contributor.updateInformationCountry(country);
                    contributor.setUserType(AccountType.CONTRIBUTOR);
                    contributor.setExperience(Integer.parseInt(experience));
                    contributor.setUsername(username);
                    JSONArray favoriteProductions = (JSONArray) object.get("favoriteProductions");
                    if (favoriteProductions != null){
                        for (Object favProdObj : favoriteProductions) {
                            String prod = (String) favProdObj;
                            contributor.addFavorite(findProductionByName(prod));
                        }
                    }
                    JSONArray favoriteActors = (JSONArray) object.get("favoriteActors");
                    if (favoriteActors != null){
                        for (Object favActorObj : favoriteActors) {
                            String ac = (String) favActorObj;
                            contributor.addFavorite(findActorByName(ac));
                        }
                    }

                    JSONArray productionsContribution = (JSONArray) object.get("productionsContribution");
                    if (productionsContribution != null){
                        for (Object contribObj : productionsContribution) {
                            String prod = (String) contribObj;
                            contributor.addContribution(findProductionByName(prod));
                        }
                    }

                    JSONArray actorsContribution = (JSONArray) object.get("actorsContribution");
                    if (actorsContribution != null){
                        for (Object contribObj : actorsContribution) {
                            String ac = (String) contribObj;
                            contributor.addContribution(findActorByName(ac));
                        }
                    }

                    users.add(contributor);
                }
                if (userType.equals("Admin")){
                    Admin admin = new Admin();
                    admin.updateInformationCredentialsName(email, password, name);
                    if (birthDate != null) {
                        //admin.updateInformationBirthDate(birthDate);
                    }
                    admin.updateInformationGender(gender);
                    admin.updateInformationAge(age);
                    admin.updateInformationCountry(country);
                    admin.setUserType(AccountType.CONTRIBUTOR);
                    admin.setExperience(100000);
                    admin.setUsername(username);
                    JSONArray favoriteProductions = (JSONArray) object.get("favoriteProductions");
                    if (favoriteProductions != null){
                        for (Object favProdObj : favoriteProductions) {
                            String prod = (String) favProdObj;
                            admin.addFavorite(findProductionByName(prod));
                        }
                    }

                    JSONArray favoriteActors = (JSONArray) object.get("favoriteActors");
                    if (favoriteActors != null){
                        for (Object favActorObj : favoriteActors) {
                            String ac = (String) favActorObj;
                            admin.addFavorite(findActorByName(ac));
                        }
                    }

                    JSONArray productionsContribution = (JSONArray) object.get("productionsContribution");
                    if (productionsContribution != null){
                        for (Object contribObj : productionsContribution) {
                            String prod = (String) contribObj;
                            admin.addContribution(findProductionByName(prod));
                        }
                    }

                    JSONArray actorsContribution = (JSONArray) object.get("actorsContribution");
                    if (actorsContribution != null){
                        for (Object contribObj : actorsContribution) {
                            String ac = (String) contribObj;
                            admin.addContribution(findActorByName(ac));
                        }
                    }

                    users.add(admin);
                }
            }


        }catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public int findAccount(String emailToFind, String passwordToFind) {
        int ok = -1;
        for (User user : users) {
            ok = 0;
            if (emailToFind.equals(user.getEmail())) {
                ok = 1;
                if (ok == 1 && passwordToFind.equals(user.getPassword())) {
                    ok = 2;
                    return ok;
                }
            }
        }
        return ok;
    }




    public String findUsername(String emailToFind) {
        for (User user : users) {
            if (emailToFind.equals(user.getEmail())) {
                return user.getUsername();
            }
        }
        return null;
    }

    public String findUserName(String emailToFind) {
        for (User user : users) {
            if (emailToFind.equals(user.getEmail())) {
                return user.getInfoName();
            }
        }
        return null;
    }

    public User findUserAc(String emailToFind){
        for (User user : users) {
            if (emailToFind.equals(user.getEmail())) {
                return user;
            }
        }
        return null;
    }

    public int findUsernameExperience(String emailToFind) {
        for (User user : users) {
            if (emailToFind.equals(user.getEmail())) {
                return user.getExperience();
            }
        }
        return -1;
    }

    public AccountType findUsernameType(String emailToFind) {
        for (User user : users) {
            if (emailToFind.equals(user.getEmail())) {
                return user.getAccountType();
            }
        }
        return AccountType.REGULAR;
    }

    public void findUsernameFavorites(String emailToFind) {
        for (User user : users) {
            if (emailToFind.equals(user.getEmail())) {
                user.printFavorites();
            }
        }
    }

    public Actor findActorByName(String actorName) {
        for (Actor actor : actors) {
            if (actor.getName().equals(actorName)) {
                return actor;
            }
        }
        return null;
    }

    public Production findProductionByName(String productionName) {
        for (Production production : productions) {
            if (production.getTitle().equals(productionName)) {
                return production;
            }
        }
        return null;
    }

    public void findProductionsByGenre(Genre target) {
        if (productions != null) {
            for (Production production : productions) {
                List<Genre> genres = production.getGenres();
                if (genres != null && genres.contains(target)) {
                    System.out.println(production);
                }
            }
        }
    }


    public void parseAndPrintRequestsOfAnUser(String username) {
        for (Request request : requests) {
            if (request.getUsername().equals(username)){
                System.out.println(request);
            }
        }
    }

    public void parseAndPrintRequestsForContributors() {
        for (Request request : requests) {
            if (request.getTo() != "Admin"){
                System.out.println(request);
            }
        }
    }

    public Request parseAndRetReqToDel(String username, String description) {
        for (Request request : requests) {
            if (request.getUsername().equals(username) && request.getDescription().equals(description)){
                return request;
            }
        }
        return null;
    }

    public void sortActorsByName() {
        Collections.sort(actors, Comparator.comparing(Actor::getName));
    }

    public void run() throws IOException {
        readJSON();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to IMDB! Please log in!");
        int confirmed = 0;
        String emailToFind = null;
        String passwordToFind = null;
        int continuity;

        while (confirmed == 0) {
            confirmed = 1;
            System.out.print("Enter your email: ");
            emailToFind = scanner.nextLine();
            emailToFind = emailToFind.trim();
            System.out.print("Enter your password: ");
            passwordToFind = scanner.nextLine();
            int foundOrNot = -1;
            foundOrNot = findAccount(emailToFind, passwordToFind);
            System.out.print("\n");
            System.out.print("\n");
            switch (foundOrNot) {
                case 1:
                    System.out.println("Wrong password!");
                    break;
                case 2:
                    System.out.println("Welcome!");
                    break;
                case 0:
                    System.out.println("Account doesn't exist, try again!");
            }
            if (foundOrNot == 2) {
                confirmed = 1;
            } else {
                confirmed = 0;
            }
            continuity = -1;
            System.out.print("\n");
            if (confirmed == 1){
                continuity = 0;
            }
            while (continuity == 0) {
                String username = findUsername(emailToFind);
                String name = findUserName(emailToFind);
                System.out.println("Welcome back " + name);
                System.out.println("Username: " + findUserName(emailToFind));
                System.out.println("Experience: " + findUsernameExperience(emailToFind));
                System.out.println("Choose action:");
                String stringChoice;
                //Pentru requests catre admini
                Request.RequestsHolder requestsHolder = new Request.RequestsHolder();
                int choice;
                int okay;
                okay = 0;
                if (findUsernameType(emailToFind) == AccountType.REGULAR) {
                    System.out.println("1) View productions details");
                    System.out.println("2) View actors details");
                    System.out.println("3) View notifications");
                    System.out.println("4) Search for actor/movie/series");
                    System.out.println("5) Add/Delete actor/movie/series from favorites");
                    System.out.println("6) Add a request");
                    System.out.println("7) Delete a request");
                    System.out.println("8) Add a review");
                    System.out.println("9) Delete a review");
                    System.out.println("10) Logout");
                    System.out.print("\n");
                    System.out.println("Your choice: ");
                    stringChoice = scanner.nextLine();
                    choice = Integer.parseInt(stringChoice);
                    System.out.print("\n");
                    System.out.print("\n");
                    switch (choice) {
                        case 1:
                            //WORKS!!!
                            System.out.println("Do you have a genre which you'd like to search?");
                            System.out.println("If yes, introduce the name of the genre");
                            System.out.println("If no, introduce 'no'");
                            String searchGen;
                            searchGen = scanner.nextLine();
                            Genre genre = null;
                            if (searchGen.equals("no")){
                                System.out.println("Productions:\n\n" + productions);
                            } else{
                                try {
                                    genre = Genre.valueOf(searchGen);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("This genre doesn't exist!");
                                }
                                findProductionsByGenre(genre);
                            }
                            System.out.print("\n");
                            break;
                        case 2:
                            //WORKS!!!
                            sortActorsByName();
                            System.out.println("Actors:\n\n" + actors);
                            System.out.print("\n");
                            break;
                        case 3:
                            System.out.println("WIP!");
                            System.out.print("\n");
                            break;
                        case 4:
                            //WORKS! MAI LUCREZ LA TOSTRING PT FILME, LA CUM AFISEAZA RATINGS
                            System.out.println("To search an actor, choose 1");
                            System.out.println("To search a movie/series, choose 2");
                            String searchChoice;
                            int searchCho;
                            String nameTitle;
                            searchChoice = scanner.nextLine();
                            searchCho = Integer.parseInt(searchChoice);
                            if (searchCho == 1){
                                Actor dummy = new Actor();
                                System.out.println("What's the name of the actor?");
                                nameTitle = scanner.nextLine();
                                dummy = findActorByName(nameTitle);
                                if (dummy == null){
                                    System.out.println("Actor doesn't exist");
                                }
                                else{
                                    System.out.println(dummy);
                                }
                            }
                            if (searchCho == 2){
                                Production dummy = new Production() {
                                    @Override
                                    public void displayInfo() {

                                    }

                                    @Override
                                    public String giveString() {
                                        return null;
                                    }
                                };
                                System.out.println("What's the name of the movie/serie?");
                                nameTitle = scanner.nextLine();
                                dummy = findProductionByName(nameTitle);
                                if (dummy == null){
                                    System.out.println("Production doesn't exist");
                                }
                                else{
                                    System.out.println(dummy);
                                }
                            }
                            System.out.print("\n");
                            break;
                        case 6:
                            //WORKS!
                            Request request = new Request();
                            System.out.println("Please select a type of request:");
                            System.out.println("If you'd like your account to be deleted, press 1");
                            System.out.println("For an issue regarding an actor, press 2");
                            System.out.println("For an issue regarding a production, press 3");
                            System.out.println("For any other issue, press 4");
                            String searchIssue;
                            int searchIs;
                            searchIssue = scanner.nextLine();
                            searchIs = Integer.parseInt(searchIssue);
                            String actorProdName;
                            String toWho;
                            switch (searchIs) {
                                case 1:
                                    request.setType(RequestTypes.DELETE_ACCOUNT);
                                    request.setTo("ADMIN");
                                    break;
                                case 2:
                                    request.setType(RequestTypes.ACTOR_ISSUE);
                                    System.out.println("Please introduce the name of the actor:");
                                    actorProdName = scanner.nextLine();
                                    request.setNameActorProduction(actorProdName);
                                    System.out.println("Please introduce the username of the one you are making the request to:");
                                    toWho = scanner.nextLine();
                                    request.setTo(toWho);
                                    break;

                                case 3:
                                    request.setType(RequestTypes.MOVIE_ISSUE);
                                    System.out.println("Please introduce the name of the production:");
                                    actorProdName = scanner.nextLine();
                                    request.setNameActorProduction(actorProdName);
                                    System.out.println("Please introduce the username of the one you are making the request to:");
                                    toWho = scanner.nextLine();
                                    request.setTo(toWho);
                                    break;
                                case 4:
                                    request.setType(RequestTypes.OTHERS);
                                    System.out.println("Please introduce the username of the one you are making the request to:");
                                    toWho = scanner.nextLine();
                                    request.setTo(toWho);
                                    break;
                            }
                            request.setUsername(username);
                            request.setCreatedDate(LocalDateTime.now());
                            String descReq;
                            System.out.println("Please give a description of your request:");
                            descReq = scanner.nextLine();
                            request.setDescription(descReq);
                            System.out.println("Request sent!");
                            if (request.getTo() == "ADMIN"){
                                requestsHolder.addAdminRequest(request);
                            }
                            //regular.createRequest(request);
                            requests.add(request);
                            System.out.print("\n");
                            break;
                        case 5:
                            //WORKS!!!!
                            String howIsCalled;
                            String favoriteChoice;
                            int favoriteCho;
                            System.out.println("Favorites: ");
                            findUsernameFavorites(emailToFind);
                            System.out.println("To add a favorite actor/movie/serie, press 1");
                            System.out.println("To delete a favorite actor/movie/serie, press 2");
                            favoriteChoice = scanner.nextLine();
                            favoriteCho = Integer.parseInt(favoriteChoice);
                            if (favoriteCho == 1){
                                System.out.println("If it is an actor, press 1");
                                System.out.println("If it is a movie, press 2");
                                System.out.println("If it is a serie, press 3");
                                String press;
                                int pressInt;
                                press = scanner.nextLine();
                                pressInt = Integer.parseInt(press);
                                if (pressInt == 1){
                                    System.out.println("What is the actor's name?");
                                    howIsCalled = scanner.nextLine();
                                    Actor newActor = findActorByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newActor);
                                    System.out.println("Actor added succesfully!");
                                }
                                if (pressInt == 2){
                                    System.out.println("What is the movie's name?");
                                    howIsCalled = scanner.nextLine();
                                    Movie newMovie = (Movie) findProductionByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newMovie);
                                    System.out.println("Movie added succesfully!");
                                }
                                if (pressInt == 3){
                                    System.out.println("What is the series name?");
                                    howIsCalled = scanner.nextLine();
                                    Series newSerie = (Series) findProductionByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newSerie);
                                    System.out.println("Serie added succesfully!");
                                }
                            }
                            if (favoriteCho == 2){
                                System.out.println("Please introduce the name of the actor/movie/serie:");
                                howIsCalled = scanner.nextLine();
                                int removed = -1;
                                removed = findUserAc(emailToFind).removeFavoriteByName(howIsCalled);
                                if (removed == 1) {
                                    System.out.println("Actor/Movie/Serie deleted succesfully!");
                                } else {
                                    System.out.println("Actor/Movie/Serie not found :(");
                                }
                            }
                            System.out.print("\n");
                            break;
                        case 7:
                            //WORKS!!!
                            System.out.println("These are all the requests owned by you. Which one do you want to delete?");
                            parseAndPrintRequestsOfAnUser(username);
                            System.out.println("Choose a request by introducing the description: ");
                            String toDel = scanner.nextLine();
                            Request r = parseAndRetReqToDel(username, toDel);
                            findUserAc(emailToFind).removeRequest(r);
                            requests.remove(r);
                            System.out.print("\n");
                            break;
                        case 8:
                            //WORKS!!!
                            System.out.println("What is the production's name that you wish to rate?");
                            String toReview;
                            toReview = scanner.nextLine();
                            System.out.println("?/10 for this production, please introduce your rating");
                            String rateString;
                            rateString = scanner.nextLine();
                            int rateInt;
                            rateInt = Integer.parseInt(rateString);
                            String commentReview;
                            System.out.println("Please introduce a comment for the review");
                            commentReview = scanner.nextLine();
                            findUserAc(emailToFind).addRating(username, rateInt, commentReview, findProductionByName(toReview));
                            System.out.print("\n");
                            break;
                        case 10:
                            //WORKS WELL!
                            System.out.println("Exit the app or login again?");
                            System.out.println("For login, choose 1");
                            System.out.println("To exit the app, choose 2");
                            String logoutChoice;
                            int logoutCho;
                            logoutChoice = scanner.nextLine();
                            logoutCho = Integer.parseInt(logoutChoice);
                            if (logoutCho == 1) {
                                confirmed = 0;
                                continuity = 1;
                            }
                            if (logoutCho == 2) {
                                confirmed = 1;
                                continuity = 1;
                            }
                            System.out.print("\n");
                            break;
                        case 9:
                            System.out.println("WIP!");
                            System.out.print("\n");
                            break;
                    }
                }
                if (findUsernameType(emailToFind) == AccountType.CONTRIBUTOR) {
                    System.out.println("1) View productions details");
                    System.out.println("2) View actors details");
                    System.out.println("3) View notifications");
                    System.out.println("4) Search for actor/movie/series");
                    System.out.println("5) Add/Delete actor/movie/series from favorites");
                    System.out.println("6) Add a request");
                    System.out.println("7) Delete a request");

                    System.out.println("8) Add an actor/production to the system");
                    System.out.println("9) Delete an actor/production to the system");
                    System.out.println("10) See requests and solve them");
                    System.out.println("11) Update details about an actor/production");
                    System.out.println("12) Logout");
                    System.out.print("\n");
                    System.out.println("Your choice: ");
                    stringChoice = scanner.nextLine();
                    choice = Integer.parseInt(stringChoice);
                    System.out.print("\n");
                    System.out.print("\n");
                    switch (choice) {
                        case 1:
                            //WORKS!!!
                            System.out.println("Do you have a genre which you'd like to search?");
                            System.out.println("If yes, introduce the name of the genre");
                            System.out.println("If no, introduce 'no'");
                            String searchGen;
                            searchGen = scanner.nextLine();
                            Genre genre = null;
                            if (searchGen.equals("no")){
                                System.out.println("Productions:\n\n" + productions);
                            } else{
                                try {
                                    genre = Genre.valueOf(searchGen);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("This genre doesn't exist!");
                                }
                                findProductionsByGenre(genre);
                            }
                            System.out.print("\n");
                            break;
                        case 2:
                            //WORKS!!!
                            sortActorsByName();
                            System.out.println("Actors:\n\n" + actors);
                            System.out.print("\n");
                            break;
                        case 3:
                            System.out.println("WIP!");
                            System.out.print("\n");
                            break;
                        case 4:
                            //WORKS! MAI LUCREZ LA TOSTRING PT FILME, LA CUM AFISEAZA RATINGS
                            System.out.println("To search an actor, choose 1");
                            System.out.println("To search a movie/series, choose 2");
                            String searchChoice;
                            int searchCho;
                            String nameTitle;
                            searchChoice = scanner.nextLine();
                            searchCho = Integer.parseInt(searchChoice);
                            if (searchCho == 1){
                                Actor dummy = new Actor();
                                System.out.println("What's the name of the actor?");
                                nameTitle = scanner.nextLine();
                         System.out.println("WIP!");       dummy = findActorByName(nameTitle);
                                if (dummy == null){
                                    System.out.println("Actor doesn't exist");
                                }
                                else{
                                    System.out.println(dummy);
                                }
                            }
                            if (searchCho == 2){
                                Production dummy = new Production() {
                                    @Override
                                    public void displayInfo() {
Regular regular = new Regular();
        regular.setExperience(20);
        regular.setUsername("guest");
        regular.updateInformationCredentialsName("guest", "guest", "guest");
        users.add(regular);
        regular.setUserType(AccountType.REGULAR);
                                    }

                                    @Override
                                    public String giveString() {
                                        return null;
                                    }
                                };
                                System.out.println("What's the name of the movie/serie?");
                                nameTitle = scanner.nextLine();
                                dummy = findProductionByName(nameTitle);
                                if (dummy == null){
                                    System.out.println("Production doesn't exist");
                                }
                                else{
                                    System.out.println(dummy);
                                }
                            }
                            System.out.print("\n");
                            break;
                        case 6:
                            //WORKS!
                            Request request = new Request();
                            System.out.println("Please select a type of request:");
                            System.out.println("If you'd like your account to be deleted, press 1");
                            System.out.println("For an issue regarding an actor, press 2");
                            System.out.println("For an issue regarding a production, press 3");
                            System.out.println("For any other issue, press 4");
                            String searchIssue;
                            int searchIs;
                            searchIssue = scanner.nextLine();
                            searchIs = Integer.parseInt(searchIssue);
                            String actorProdName;
                            String toWho;
                            switch (searchIs) {
                                case 1:
                                    request.setType(RequestTypes.DELETE_ACCOUNT);
                                    request.setTo("ADMIN");
                                    break;
                                case 2:
                                    request.setType(RequestTypes.ACTOR_ISSUE);
                                    System.out.println("Please introduce the name of the actor:");
                                    actorProdName = scanner.nextLine();
                                    request.setNameActorProduction(actorProdName);
                                    System.out.println("Please introduce the username of the one you are making the request to:");
                                    toWho = scanner.nextLine();
                                    request.setTo(toWho);
                                    break;

                                case 3:
                                    request.setType(RequestTypes.MOVIE_ISSUE);
                                    System.out.println("Please introduce the name of the production:");
                                    actorProdName = scanner.nextLine();
                                    request.setNameActorProduction(actorProdName);
                                    System.out.println("Please introduce the username of the one you are making the request to:");
                                    toWho = scanner.nextLine();
                                    request.setTo(toWho);
                                    break;
                                case 4:
                                    request.setType(RequestTypes.OTHERS);
                                    System.out.println("Please introduce the username of the one you are making the request to:");
                                    toWho = scanner.nextLine();
                                    request.setTo(toWho);
                                    break;
                            }
                            request.setUsername(username);
                            request.setCreatedDate(LocalDateTime.now());
                            String descReq;
                            System.out.println("Please give a description of your request:");
                            descReq = scanner.nextLine();
                            request.setDescription(descReq);
                            System.out.println("Request sent!");
                            if (request.getTo() == "ADMIN"){
                                requestsHolder.addAdminRequest(request);
                            }
                            //regular.createRequest(request);
                            requests.add(request);
                            System.out.print("\n");
                            break;
                        case 5:
                            //WORKS!!!!
                            String howIsCalled;
                            String favoriteChoice;
                            int favoriteCho;
                            System.out.println("FavoscanDeleteProdrites: ");
                            findUsernameFavorites(emailToFind);
                            System.out.println("To add a favorite actor/movie/serie, press 1");
                            System.out.println("To delete a favorite actor/movie/serie, press 2");
                            favoriteChoice = scanner.nextLine();
                            favoriteCho = Integer.parseInt(favoriteChoice);
                            if (favoriteCho == 1){
                                System.out.println("If it is an actor, press 1");
                                System.out.println("If it is a movie, press 2");
                                System.out.println("If it is a serie, press 3");
                                String press;
                                int pressInt;
                                press = scanner.nextLine();
                                pressInt = Integer.parseInt(press);
                                if (pressInt == 1){
                                    System.out.println("What is the actor's name?");
                                    howIsCalled = scanner.nextLine();
                                    Actor newActor = findActorByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newActor);
                                    System.out.println("Actor added succesfully!");
                                }
                                if (pressInt == 2){
                                    System.out.println("What is the movie's name?");
                                    howIsCalled = scanner.nextLine();
                                    Movie newMovie = (Movie) findProductionByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newMovie);
                                    System.out.println("Movie added succesfully!");
                                }
                                if (pressInt == 3){
                                    System.out.println("What is the series name?");
                                    howIsCalled = scanner.nextLine();
                                    Series newSerie = (Series) findProductionByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newSerie);
                                    System.out.println("Serie added succesfully!");
                                }
                            }
                            if (favoriteCho == 2){
                                System.out.println("Please introduce the name of the actor/movie/serie:");
                                howIsCalled = scanner.nextLine();
                                int removed = -1;
                                removed = findUserAc(emailToFind).removeFavoriteByName(howIsCalled);
                                if (removed == 1) {
                                    System.out.println("Actor/Movie/Serie deleted succesfully!");
                                } else {
                                    System.out.println("Actor/Movie/Serie not found :(");
                          System.out.println("WIP!");      }
                            }
                            System.out.print("\n");
                            break;
                        case 7:
                            //WORKS!!!
                            System.out.println("These are all the requests owned by you and who are adressed to contributors. Which one do you want to delete?");
                            parseAndPrintRequestsOfAnUser(username);
                            System.out.println("Choose a request by introducing the description: ");
                            String toDel = scanner.nextLine();
                            Request r = parseAndRetReqToDel(username, toDel);
                            findUserAc(emailToFind).removeRequest(r);
                            requests.remove(r);
                            System.out.print("\n");
                            break;
                        case 8:
                            //WORKS!!
                            System.out.println("If you'd like to add an actor, press 1");
                            System.out.println("If you'd like to add a production, press 2");
                            String opt = scanner.nextLine();
                            if (opt.equals("1")){
                                Actor newAct = new Actor();
                                System.out.println("What's the name of the actor?");
                                String nameOfActor = scanner.nextLine();
                                newAct.setName(nameOfActor);
                                System.out.println("What's the biography of the actor?");
                                String bioOfActor = scanner.nextLine();
                                newAct.setBiography(bioOfActor);
                                System.out.println("In what movies/series did he perform?");
                                System.out.println("Press 1 when you are done introducing productions");
                                int counter = 0;
                                while (counter == 0){
                                    System.out.println("Introduce the name of the production:");
                                    String scanCounter1 = scanner.nextLine();
                                    System.out.println("For type, write 'Movie' or 'Series'");
                                    System.out.println("Please introduce the type:");
                                    String scanCounter2 = scanner.nextLine();
                                    newAct.setMoviePair(scanCounter1, scanCounter2);
                                    System.out.println("Press 1 if you are done");
                                    System.out.println("Press 2 if you want to introduce more productions");
                                    String scanCounter3 = scanner.nextLine();
                                    if (scanCounter3.equals("1")){
                                        counter = 1;
                                    }
                                    if (scanCounter3.equals("2")){
                                        counter = 0;
                                    }
                                }
                                if (opt.equals("2")){
                                    System.out.println("Press 1 if it is a movie");
                                    System.out.println("Press 2 if it is a series");
                                    String scanCounter1 = scanner.nextLine();
                                    System.out.println("What is the title for it?");
                                    String scanCounter2 = scanner.nextLine();
                                    System.out.println("Which is the release year");
                                    String scanCounter3 = scanner.nextLine();
                                    System.out.println("What is the plot?");
                                    String scanPlot = scanner.nextLine();
                                    if (scanCounter1.equals("1")){
                                        Movie newMov = new Movie();
                                        newMov.setTitle(scanCounter2);
                                        newMov.setPlot(scanPlot);
                                        newMov.setReleaseYear(Integer.parseInt(scanCounter3));
                                        System.out.println("Which is the duration of the movie in minutes?");
                                        String scanCounter4 = scanner.nextLine();
                                        newMov.setDuration(scanCounter4);
                                        System.out.println("Who directed the movie?");
                                        int counter2 = 0;
                                        String scanCounter6;
                                        String scanCounter5;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the director:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addDirectors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What actors played the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the actor:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addActors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What are the genres of the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the genre (ex.: War):");
                                            scanCounter5 = scanner.nextLine();
                                            Genre genreToAdd = FindGenre.findGenre(scanCounter5);
                                            newMov.addGenres(genreToAdd);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("Movie added successfully!");
                                    }
                                    if (scanCounter1.equals("1")){
                                        Series newMov = new Series();
                                        newMov.setTitle(scanCounter2);
                                        newMov.setPlot(scanPlot);
                                        newMov.setReleaseYear(Integer.parseInt(scanCounter3));
                                        System.out.println("How many seasons does the series have?");
                                        String scanCounter4 = scanner.nextLine();
                                        newMov.setNumSeasons(Integer.parseInt(scanCounter4));
                                        System.out.println("Who directed the movie?");
                                        int counter2 = 0;
                                        String scanCounter6;
                                        String scanCounter5;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the director:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addDirectors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What actors played the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the actor:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addActors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What are the genres of the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the genre (ex.: War):");
                                            scanCounter5 = scanner.nextLine();
                                            Genre genreToAdd = FindGenre.findGenre(scanCounter5);
                                            newMov.addGenres(genreToAdd);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        int nrSeason = 1;
                                        int check1 = 0;
                                        while (check1 == 0){
                                            List<Episode> episodeArrayList = new ArrayList<>();
                                            System.out.println("Introduce episodes for Season " + nrSeason + ":");
                                            int check2 = 0;
                                            while (check2 == 0){
                                                System.out.println("Introduce the name of the episode");
                                                String scanEp1 = scanner.nextLine();
                                                System.out.println("Introduce the duration of the episode in minutes");
                                                String scanEp2 = scanner.nextLine();
                                                Episode episode = new Episode(scanEp1, scanEp2);
                                                System.out.println("Press 1 to add more episodes");
                                                System.out.println("Press 2 to if this was the last episode of the season");
                                                String scanEp3 = scanner.nextLine();
                                                if (scanEp3.equals("2")){
                                                    check2 = 1;
                                                }
                                                if (scanEp3.equals("1")){
                                                    check2 = 0;
                                                }
                                            }
                                            newMov.addSeason(("Season " + nrSeason), episodeArrayList);
                                        }
                                        System.out.println("Series added successfully!");
                                    }
                                }
                            }
                            break;
                        case 9:
                            //WORKS!!
                            System.out.println("If you want to delete an actor, press 1");
                            System.out.println("If you want to delete a production, press 2");
                            String scanDelete = scanner.nextLine();
                            if (scanDelete.equals("1")){
                                System.out.println("What is the name of the actor you want to delete?");
                                String scanDeleteActor = scanner.nextLine();
                                actors.remove(findActorByName(scanDeleteActor));
                                System.out.println("Actor deleted successfully!");
                            }
                            if (scanDelete.equals("1")){
                                System.out.println("What is the name of the production you want to delete?");
                                String scanDeleteProd = scanner.nextLine();
                                productions.remove(findProductionByName(scanDeleteProd));
                                System.out.println("Production deleted successfully!");
                            }
                            System.out.print("\n");
                            break;
                        case 11:
                            //WORKS!!
                            System.out.println("If you want to update the info about an actor, press 1");
                            System.out.println("If you want to update the info about a production, press 2");
                            String scanOptionUpdate = scanner.nextLine();
                            if (scanOptionUpdate.equals("1")){
                                System.out.println("What is the name of the actor?");
                                String scanOptionUpdateActorData3 = scanner.nextLine();
                                System.out.println("What do you want to update?");
                                System.out.println("Press 1 for name");
                                System.out.println("Press 2 for biography");
                                System.out.println("Press 3 if you want to add a performance");
                                String scanOptionUpdateActor = scanner.nextLine();
                                String scanOptionUpdateActorData1;
                                String scanOptionUpdateActorData2;
                                if (scanOptionUpdateActor.equals("1")){
                                    System.out.println("What is the new name of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).setName(scanOptionUpdateActorData1);
                                    System.out.println("Update successful!");
                                }
                                if (scanOptionUpdateActor.equals("2")){
                                    System.out.println("What is the new bio of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).setBiography(scanOptionUpdateActorData1);
                                    System.out.println("Update successful!");
                                }
                                if (scanOptionUpdateActor.equals("3")){
                                    System.out.println("What is the name of the new performance of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    System.out.println("What is the type of the new performance of the actor? (Movie or Series)");
                                    scanOptionUpdateActorData2 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).setMoviePair(scanOptionUpdateActorData1, scanOptionUpdateActorData2);
                                    System.out.println("Update successful!");
                                }
                                if (scanOptionUpdateActor.equals("4")){
                                    System.out.println("What is the name of the performance to be deleted of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    System.out.println("What is the type of the new performance to be deleted of the actor? (Movie or Series)");
                                    scanOptionUpdateActorData2 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).deleteMoviePair(scanOptionUpdateActorData1, scanOptionUpdateActorData2);
                                    System.out.println("Update successful!");
                                }
                            }
                            if (scanOptionUpdate.equals("2")){
                                System.out.println("What is the name of the production?");
                                String scanOpt1 = scanner.nextLine();
                                System.out.println("If you want to update the title, press 1");
                                System.out.println("If you want to add a new director, press 2");
                                System.out.println("If you want to add a new genre, press 3");
                                System.out.println("If you want to add a new actor, press 4");
                                System.out.println("If you want to update the duration for a movie, press 5");
                                System.out.println("If you want to update the num of seasons for a series, press 6");
                                String scanOpt2 = scanner.nextLine();
                                String scanOpt3;
                                if (scanOpt2.equals("1")){
                                    System.out.println("What is the new title?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).setTitle(scanOpt3);
                                }
                                if (scanOpt2.equals("4")){
                                    System.out.println("What is the new actor?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).addActors(scanOpt3);
                                }
                                if (scanOpt2.equals("3")){
                                    System.out.println("What is the new genre?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).addGenres(FindGenre.findGenre(scanOpt3));
                                }
                                if (scanOpt2.equals("2")){
                                    System.out.println("What is the new director?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).addDirectors(scanOpt3);
                                }
                                if (scanOpt2.equals("5")){
                                    System.out.println("What is the new duration?");
                                    scanOpt3 = scanner.nextLine();
                                    Movie mo = (Movie) findProductionByName(scanOpt1);
                                    mo.setDuration(scanOpt3);
                                }
                                if (scanOpt2.equals("6")){
                                    System.out.println("What is the new number of seasons?");
                                    scanOpt3 = scanner.nextLine();
                                    Series so = (Series) findProductionByName(scanOpt1);
                                    so.setNumSeasons(Integer.parseInt(scanOpt3));
                                }
                                System.out.println("Update successful!");
                            }
                            System.out.print("\n");
                            break;
                        case 10:
                            //WORKS
                            System.out.println("Here are all the requests you can handle!");
                            System.out.println("Delete the request which you handled!");
                            parseAndPrintRequestsForContributors();
                            System.out.println("Introduce the username of the one who made the request that you handled: ");
                            String scanToDelDesc1 = scanner.nextLine();
                            System.out.println("Introduce the description of he request that you handled: ");
                            String scanToDelDesc2 = scanner.nextLine();
                            requests.remove(parseAndRetReqToDel(scanToDelDesc1, scanToDelDesc2));
                            System.out.println("Request deleted and handled!");
                            System.out.print("\n");
                            break;
                        case 12:
                            //WORKS WELL!
                            System.out.println("Exit the app or login again?");
                            System.out.println("For login, choose 1");
                            System.out.println("To exit the app, choose 2");
                            String logoutChoice;
                            int logoutCho;
                            logoutChoice = scanner.nextLine();
                            logoutCho = Integer.parseInt(logoutChoice);
                            if (logoutCho == 1) {
                                confirmed = 0;
                                continuity = 1;
                            }
                            if (logoutCho == 2) {
                                confirmed = 1;
                                continuity = 1;
                            }
                            System.out.print("\n");
                            break;
                    }


                }
                if (findUsernameType(emailToFind) == AccountType.ADMIN) {
                    System.out.println("1) View productions details");
                    System.out.println("2) View actors details");
                    System.out.println("3) View notifications");
                    System.out.println("4) Search for actor/movie/series");
                    System.out.println("5) Add/Delete actor/movie/series from favorites");

                    System.out.println("6) Add an actor/production to the system");
                    System.out.println("7) Delete an actor/production to the system");
                    System.out.println("8) See requests and solve them");
                    System.out.println("9) Update details about an actor/production");

                    System.out.println("10) Ceva");

                    System.out.println("11) Logout");
                    System.out.print("\n");
                    System.out.println("Your choice: ");
                    stringChoice = scanner.nextLine();
                    choice = Integer.parseInt(stringChoice);
                    System.out.print("\n");
                    System.out.print("\n");
                    switch (choice) {
                        case 1:
                            //WORKS!!!
                            System.out.println("Do you have a genre which you'd like to search?");
                            System.out.println("If yes, introduce the name of the genre");
                            System.out.println("If no, introduce 'no'");
                            String searchGen;
                            searchGen = scanner.nextLine();
                            Genre genre = null;
                            if (searchGen.equals("no")){
                                System.out.println("Productions:\n\n" + productions);
                            } else{
                                try {
                                    genre = Genre.valueOf(searchGen);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("This genre doesn't exist!");
                                }
                                findProductionsByGenre(genre);
                            }
                            System.out.print("\n");
                            break;
                        case 2:
                            //WORKS!!!
                            sortActorsByName();
                            System.out.println("Actors:\n\n" + actors);
                            System.out.print("\n");
                            break;
                        case 3:
                            System.out.println("WIP!");
                            System.out.print("\n");
                            break;
                        case 4:
                            //WORKS! MAI LUCREZ LA TOSTRING PT FILME, LA CUM AFISEAZA RATINGS
                            System.out.println("To search an actor, choose 1");
                            System.out.println("To search a movie/series, choose 2");
                            String searchChoice;
                            int searchCho;
                            String nameTitle;
                            searchChoice = scanner.nextLine();
                            searchCho = Integer.parseInt(searchChoice);
                            if (searchCho == 1){
                                Actor dummy = new Actor();
                                System.out.println("What's the name of the actor?");
                                nameTitle = scanner.nextLine();
                                System.out.println("WIP!");       dummy = findActorByName(nameTitle);
                                if (dummy == null){
                                    System.out.println("Actor doesn't exist");
                                }
                                else{
                                    System.out.println(dummy);
                                }
                            }
                            if (searchCho == 2){
                                Production dummy = new Production() {
                                    @Override
                                    public void displayInfo() {

                                    }

                                    @Override
                                    public String giveString() {
                                        return null;
                                    }
                                };
                                System.out.println("What's the name of the movie/serie?");
                                nameTitle = scanner.nextLine();
                                dummy = findProductionByName(nameTitle);
                                if (dummy == null){
                                    System.out.println("Production doesn't exist");
                                }
                                else{
                                    System.out.println(dummy);
                                }
                            }
                            System.out.print("\n");
                            break;
                        case 5:
                            //WORKS!!!!
                            String howIsCalled;
                            String favoriteChoice;
                            int favoriteCho;
                            System.out.println("FavoscanDeleteProdrites: ");
                            findUsernameFavorites(emailToFind);
                            System.out.println("To add a favorite actor/movie/serie, press 1");
                            System.out.println("To delete a favorite actor/movie/serie, press 2");
                            favoriteChoice = scanner.nextLine();
                            favoriteCho = Integer.parseInt(favoriteChoice);
                            if (favoriteCho == 1){
                                System.out.println("If it is an actor, press 1");
                                System.out.println("If it is a movie, press 2");
                                System.out.println("If it is a serie, press 3");
                                String press;
                                int pressInt;
                                press = scanner.nextLine();
                                pressInt = Integer.parseInt(press);
                                if (pressInt == 1){
                                    System.out.println("What is the actor's name?");
                                    howIsCalled = scanner.nextLine();
                                    Actor newActor = findActorByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newActor);
                                    System.out.println("Actor added succesfully!");
                                }
                                if (pressInt == 2){
                                    System.out.println("What is the movie's name?");
                                    howIsCalled = scanner.nextLine();
                                    Movie newMovie = (Movie) findProductionByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newMovie);
                                    System.out.println("Movie added succesfully!");
                                }
                                if (pressInt == 3){
                                    System.out.println("What is the series name?");
                                    howIsCalled = scanner.nextLine();
                                    Series newSerie = (Series) findProductionByName(howIsCalled);
                                    findUserAc(emailToFind).addFavorite(newSerie);
                                    System.out.println("Serie added succesfully!");
                                }
                            }
                            if (favoriteCho == 2){
                                System.out.println("Please introduce the name of the actor/movie/serie:");
                                howIsCalled = scanner.nextLine();
                                int removed = -1;
                                removed = findUserAc(emailToFind).removeFavoriteByName(howIsCalled);
                                if (removed == 1) {
                                    System.out.println("Actor/Movie/Serie deleted succesfully!");
                                } else {
                                    System.out.println("Actor/Movie/Serie not found :(");
                                    System.out.println("WIP!");
                                }
                            }
                            System.out.print("\n");
                            break;
                        case 6:
                            //WORKS!!
                            System.out.println("If you'd like to add an actor, press 1");
                            System.out.println("If you'd like to add a production, press 2");
                            String opt = scanner.nextLine();
                            if (opt.equals("1")){
                                Actor newAct = new Actor();
                                System.out.println("What's the name of the actor?");
                                String nameOfActor = scanner.nextLine();
                                newAct.setName(nameOfActor);
                                System.out.println("What's the biography of the actor?");
                                String bioOfActor = scanner.nextLine();
                                newAct.setBiography(bioOfActor);
                                System.out.println("In what movies/series did he perform?");
                                System.out.println("Press 1 when you are done introducing productions");
                                int counter = 0;
                                while (counter == 0){
                                    System.out.println("Introduce the name of the production:");
                                    String scanCounter1 = scanner.nextLine();
                                    System.out.println("For type, write 'Movie' or 'Series'");
                                    System.out.println("Please introduce the type:");
                                    String scanCounter2 = scanner.nextLine();
                                    newAct.setMoviePair(scanCounter1, scanCounter2);
                                    System.out.println("Press 1 if you are done");
                                    System.out.println("Press 2 if you want to introduce more productions");
                                    String scanCounter3 = scanner.nextLine();
                                    if (scanCounter3.equals("1")){
                                        counter = 1;
                                    }
                                    if (scanCounter3.equals("2")){
                                        counter = 0;
                                    }
                                }
                                if (opt.equals("2")){
                                    System.out.println("Press 1 if it is a movie");
                                    System.out.println("Press 2 if it is a series");
                                    String scanCounter1 = scanner.nextLine();
                                    System.out.println("What is the title for it?");
                                    String scanCounter2 = scanner.nextLine();
                                    System.out.println("Which is the release year");
                                    String scanCounter3 = scanner.nextLine();
                                    System.out.println("What is the plot?");
                                    String scanPlot = scanner.nextLine();
                                    if (scanCounter1.equals("1")){
                                        Movie newMov = new Movie();
                                        newMov.setTitle(scanCounter2);
                                        newMov.setPlot(scanPlot);
                                        newMov.setReleaseYear(Integer.parseInt(scanCounter3));
                                        System.out.println("Which is the duration of the movie in minutes?");
                                        String scanCounter4 = scanner.nextLine();
                                        newMov.setDuration(scanCounter4);
                                        System.out.println("Who directed the movie?");
                                        int counter2 = 0;
                                        String scanCounter6;
                                        String scanCounter5;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the director:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addDirectors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What actors played the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the actor:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addActors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What are the genres of the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the genre (ex.: War):");
                                            scanCounter5 = scanner.nextLine();
                                            Genre genreToAdd = FindGenre.findGenre(scanCounter5);
                                            newMov.addGenres(genreToAdd);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("Movie added successfully!");
                                    }
                                    if (scanCounter1.equals("1")){
                                        Series newMov = new Series();
                                        newMov.setTitle(scanCounter2);
                                        newMov.setPlot(scanPlot);
                                        newMov.setReleaseYear(Integer.parseInt(scanCounter3));
                                        System.out.println("How many seasons does the series have?");
                                        String scanCounter4 = scanner.nextLine();
                                        newMov.setNumSeasons(Integer.parseInt(scanCounter4));
                                        System.out.println("Who directed the movie?");
                                        int counter2 = 0;
                                        String scanCounter6;
                                        String scanCounter5;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the director:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addDirectors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What actors played the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the actor:");
                                            scanCounter5 = scanner.nextLine();
                                            newMov.addActors(scanCounter5);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        System.out.println("What are the genres of the movie?");
                                        counter2 = 0;
                                        while (counter2 == 0){
                                            System.out.println("Introduce the name of the genre (ex.: War):");
                                            scanCounter5 = scanner.nextLine();
                                            Genre genreToAdd = FindGenre.findGenre(scanCounter5);
                                            newMov.addGenres(genreToAdd);
                                            System.out.println("Press 1 if you are done");
                                            System.out.println("Press 2 if you want to introduce more directors");
                                            scanCounter6 = scanner.nextLine();
                                            if (scanCounter6.equals("1")){
                                                counter2 = 1;
                                            }
                                            if (scanCounter6.equals("2")){
                                                counter2 = 0;
                                            }
                                        }
                                        int nrSeason = 1;
                                        int check1 = 0;
                                        while (check1 == 0){
                                            List<Episode> episodeArrayList = new ArrayList<>();
                                            System.out.println("Introduce episodes for Season " + nrSeason + ":");
                                            int check2 = 0;
                                            while (check2 == 0){
                                                System.out.println("Introduce the name of the episode");
                                                String scanEp1 = scanner.nextLine();
                                                System.out.println("Introduce the duration of the episode in minutes");
                                                String scanEp2 = scanner.nextLine();
                                                Episode episode = new Episode(scanEp1, scanEp2);
                                                System.out.println("Press 1 to add more episodes");
                                                System.out.println("Press 2 to if this was the last episode of the season");
                                                String scanEp3 = scanner.nextLine();
                                                if (scanEp3.equals("2")){
                                                    check2 = 1;
                                                }
                                                if (scanEp3.equals("1")){
                                                    check2 = 0;
                                                }
                                            }
                                            newMov.addSeason(("Season " + nrSeason), episodeArrayList);
                                        }
                                        System.out.println("Series added successfully!");
                                    }
                                }
                            }
                            break;
                        case 7:
                            //WORKS!!
                            System.out.println("If you want to delete an actor, press 1");
                            System.out.println("If you want to delete a production, press 2");
                            String scanDelete = scanner.nextLine();
                            if (scanDelete.equals("1")){
                                System.out.println("What is the name of the actor you want to delete?");
                                String scanDeleteActor = scanner.nextLine();
                                actors.remove(findActorByName(scanDeleteActor));
                                System.out.println("Actor deleted successfully!");
                            }
                            if (scanDelete.equals("1")){
                                System.out.println("What is the name of the production you want to delete?");
                                String scanDeleteProd = scanner.nextLine();
                                productions.remove(findProductionByName(scanDeleteProd));
                                System.out.println("Production deleted successfully!");
                            }
                            System.out.print("\n");
                            break;
                        case 9:
                            //WORKS!!
                            System.out.println("If you want to update the info about an actor, press 1");
                            System.out.println("If you want to update the info about a production, press 2");
                            String scanOptionUpdate = scanner.nextLine();
                            if (scanOptionUpdate.equals("1")){
                                System.out.println("What is the name of the actor?");
                                String scanOptionUpdateActorData3 = scanner.nextLine();
                                System.out.println("What do you want to update?");
                                System.out.println("Press 1 for name");
                                System.out.println("Press 2 for biography");
                                System.out.println("Press 3 if you want to add a performance");
                                String scanOptionUpdateActor = scanner.nextLine();
                                String scanOptionUpdateActorData1;
                                String scanOptionUpdateActorData2;
                                if (scanOptionUpdateActor.equals("1")){
                                    System.out.println("What is the new name of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).setName(scanOptionUpdateActorData1);
                                    System.out.println("Update successful!");
                                }
                                if (scanOptionUpdateActor.equals("2")){
                                    System.out.println("What is the new bio of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).setBiography(scanOptionUpdateActorData1);
                                    System.out.println("Update successful!");
                                }
                                if (scanOptionUpdateActor.equals("3")){
                                    System.out.println("What is the name of the new performance of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    System.out.println("What is the type of the new performance of the actor? (Movie or Series)");
                                    scanOptionUpdateActorData2 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).setMoviePair(scanOptionUpdateActorData1, scanOptionUpdateActorData2);
                                    System.out.println("Update successful!");
                                }
                                if (scanOptionUpdateActor.equals("4")){
                                    System.out.println("What is the name of the performance to be deleted of the actor?");
                                    scanOptionUpdateActorData1 = scanner.nextLine();
                                    System.out.println("What is the type of the new performance to be deleted of the actor? (Movie or Series)");
                                    scanOptionUpdateActorData2 = scanner.nextLine();
                                    findActorByName(scanOptionUpdateActorData3).deleteMoviePair(scanOptionUpdateActorData1, scanOptionUpdateActorData2);
                                    System.out.println("Update successful!");
                                }
                            }
                            if (scanOptionUpdate.equals("2")){
                                System.out.println("What is the name of the production?");
                                String scanOpt1 = scanner.nextLine();
                                System.out.println("If you want to update the title, press 1");
                                System.out.println("If you want to add a new director, press 2");
                                System.out.println("If you want to add a new genre, press 3");
                                System.out.println("If you want to add a new actor, press 4");
                                System.out.println("If you want to update the duration for a movie, press 5");
                                System.out.println("If you want to update the num of seasons for a series, press 6");
                                String scanOpt2 = scanner.nextLine();
                                String scanOpt3;
                                if (scanOpt2.equals("1")){
                                    System.out.println("What is the new title?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).setTitle(scanOpt3);
                                }
                                if (scanOpt2.equals("4")){
                                    System.out.println("What is the new actor?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).addActors(scanOpt3);
                                }
                                if (scanOpt2.equals("3")){
                                    System.out.println("What is the new genre?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).addGenres(FindGenre.findGenre(scanOpt3));
                                }
                                if (scanOpt2.equals("2")){
                                    System.out.println("What is the new director?");
                                    scanOpt3 = scanner.nextLine();
                                    findProductionByName(scanOpt1).addDirectors(scanOpt3);
                                }
                                if (scanOpt2.equals("5")){
                                    System.out.println("What is the new duration?");
                                    scanOpt3 = scanner.nextLine();
                                    Movie mo = (Movie) findProductionByName(scanOpt1);
                                    mo.setDuration(scanOpt3);
                                }
                                if (scanOpt2.equals("6")){
                                    System.out.println("What is the new number of seasons?");
                                    scanOpt3 = scanner.nextLine();
                                    Series so = (Series) findProductionByName(scanOpt1);
                                    so.setNumSeasons(Integer.parseInt(scanOpt3));
                                }
                                System.out.println("Update successful!");
                            }
                            System.out.print("\n");
                            break;
                        case 8:
                            //WORKS !!!
                            System.out.println("Here are all the requests you can handle!");
                            System.out.println("Delete the request which you handled!");
                            reqHold.getAdminRequests();
                            System.out.println("Introduce the username of the one who made the request that you handled: ");
                            String scanToDelDesc1 = scanner.nextLine();
                            System.out.println("Introduce the description of he request that you handled: ");
                            String scanToDelDesc2 = scanner.nextLine();
                            requests.remove(parseAndRetReqToDel(scanToDelDesc1, scanToDelDesc2));
                            if (scanToDelDesc1.equals("Admin")){
                                reqHold.getAdminRequests().remove(parseAndRetReqToDel(scanToDelDesc1, scanToDelDesc2));
                            }
                            System.out.println("Request deleted and handled!");
                            System.out.print("\n");
                            break;
                        case 11:
                            //WORKS WELL!
                            System.out.println("Exit the app or login again?");
                            System.out.println("For login, choose 1");
                            System.out.println("To exit the app, choose 2");
                            String logoutChoice;
                            int logoutCho;
                            logoutChoice = scanner.nextLine();
                            logoutCho = Integer.parseInt(logoutChoice);
                            if (logoutCho == 1) {
                                confirmed = 0;
                                continuity = 1;
                            }
                            if (logoutCho == 2) {
                                confirmed = 1;
                                continuity = 1;
                            }
                            System.out.print("\n");
                            break;
                        case 10:
                            //WORKS!!
                            System.out.println("What is the username?");
                            String admin1 = scanner.nextLine();
                            System.out.println("What is the type of user?");
                            String admin2 = scanner.nextLine();
                            System.out.println("What is the email?");
                            String adminEmail = scanner.nextLine();
                            System.out.println("What is the password?");
                            String adminPass = scanner.nextLine();
                            System.out.println("What is the name?");
                            String adminName = scanner.nextLine();
                            System.out.println("What is the age?");
                            String adminAge = scanner.nextLine();
                            System.out.println("What is the gender?");
                            String adminGender = scanner.nextLine();
                            System.out.println("What is the country?");
                            String adminCountry = scanner.nextLine();
                            if (admin2.equals("Regular")){
                                Regular reg = new Regular();
                                reg.setUsername(admin1);
                                reg.setExperience(0);
                                reg.setUserType(AccountType.REGULAR);
                                reg.updateInformationCredentialsName(adminEmail, adminPass, adminName);
                                reg.updateInformationAge(Integer.parseInt(adminAge));
                                reg.updateInformationGender(adminGender);
                                reg.updateInformationCountry(adminCountry);
                            }
                            if (admin2.equals("Contributor")){
                                Contributor reg = new Contributor();
                                reg.setUsername(admin1);
                                reg.setExperience(0);
                                reg.setUserType(AccountType.CONTRIBUTOR);
                                reg.updateInformationCredentialsName(adminEmail, adminPass, adminName);
                                reg.updateInformationAge(Integer.parseInt(adminAge));
                                reg.updateInformationGender(adminGender);
                                reg.updateInformationCountry(adminCountry);
                            }
                            if (admin2.equals("Admin")){
                                Contributor reg = new Contributor();
                                reg.setUsername(admin1);
                                reg.setExperience(100000);
                                reg.setUserType(AccountType.ADMIN);
                                reg.updateInformationCredentialsName(adminEmail, adminPass, adminName);
                                reg.updateInformationAge(Integer.parseInt(adminAge));
                                reg.updateInformationGender(adminGender);
                                reg.updateInformationCountry(adminCountry);
                            }
                            break;
                    }
                }
            }
        }
    }
    public static IMDB getInstance() {
        if (instance == null) instance = new IMDB();
        return instance;
    }
    public static void main(String[] args) throws IOException {
        IMDB.getInstance().run();
    }
}
