package org.example;

import java.util.Comparator;

public class AllTypesComparator implements Comparator<AllTypes> {

    @Override
    public int compare(AllTypes obj1, AllTypes obj2) {
        String str1 = obj1.giveString();
        String str2 = obj2.giveString();
        return str1.compareTo(str2);
    }
}