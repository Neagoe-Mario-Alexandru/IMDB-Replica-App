package org.example;

import java.util.ArrayList;

public class Contributor extends Staff implements RequestsManager {

    @Override
    public void createRequest(Request r) {
        staffRequests.add(r);
    }

    @Override
    public void removeRequest(Request r) {
        staffRequests.remove(r);
    }

    private ArrayList<AllTypes> contributions = new ArrayList<>();;

    public void addContribution(AllTypes something){
        contributions.add(something);
    }
}
