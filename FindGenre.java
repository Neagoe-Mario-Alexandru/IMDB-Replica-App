package org.example;

public class FindGenre {
    public static Genre findGenre(String genreToFind) {
        for (Genre genre : Genre.values()) {
            if (genre.name().equals(genreToFind)) {
                return genre;
            }
        }
        return null;
    }
}
