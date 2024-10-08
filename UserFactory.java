package org.example;

public class UserFactory {
    public static User factory (String userType) {
        if (userType.equals("Regular"))
            return new Regular();
        if (userType.equals("Contributor"))
            return new Contributor();
        if (userType.equals("Admin"))
            return new Admin();
        return null;
    }
}