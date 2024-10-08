package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Staff extends User implements StaffInterface{
    protected List<Request> staffRequests = new ArrayList<>();

    private SortedSet<AllTypes> staffCollection = new TreeSet<>();

    @Override
    public void addActorSystem(Actor a) {
        staffCollection.add(a);
    }

    @Override
    public void addProductionSystem(Production p) {

        staffCollection.add(p);
    }

    @Override
    public void removeActorSystem(String name) {
        Actor a = new Actor(name);
        staffCollection.remove(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        Production p = new Production(name) {
            @Override
            public String giveString() {
                return null;
            }

            @Override
            public void displayInfo() {

            }

        };
        staffCollection.remove(p);
    }

    @Override
    public void updateActor(Actor a) {
        Actor replace = new Actor(a.getName());
        staffCollection.remove(a);
        staffCollection.add(replace);
    }

    @Override
    public void updateProduction(Production p) {
        Production replace = new Production(p.getTitle()) {
            @Override
            public String giveString() {
                return null;
            }

            @Override
            public void displayInfo() {

            }
        };
        staffCollection.remove(p);
    }
}
