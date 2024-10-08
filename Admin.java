package org.example;

import java.util.ArrayList;

public class Admin extends Staff{
    private ArrayList<AllTypes> contributions = new ArrayList<>();;

    public void addContribution(AllTypes something){
        contributions.add(something);
    }
}
