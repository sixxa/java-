package models;

import java.util.ArrayList;
import java.util.List;

public class DataModel {

    public List<String> getLanguages() {
        return List.of("English", "French", "Spanish", "German", "Italian");
    }

    public List<Cast> getCast(String lang) {
        List<Cast> castList = new ArrayList<>();
        if (lang.equals("English")) {
            castList.add(new Cast("1", "Robert Downey Jr.", "Iron Man"));
            castList.add(new Cast("4", "Scarlett Johansson", "Black Widow"));
            castList.add(new Cast("5", "Chris Evans", "Captain America"));
        } else if (lang.equals("French")) {
            castList.add(new Cast("2", "Jean Reno", "Leon"));
            castList.add(new Cast("6", "Marion Cotillard", "Edith Piaf"));
            castList.add(new Cast("7", "Vincent Cassel", "Jacques Mesrine"));
        } else if (lang.equals("Spanish")) {
            castList.add(new Cast("3", "Penélope Cruz", "Maria"));
            castList.add(new Cast("8", "Javier Bardem", "Raoul Silva"));
            castList.add(new Cast("9", "Antonio Banderas", "El Mariachi"));
        } else if (lang.equals("German")) {
            castList.add(new Cast("10", "Daniel Brühl", "Niki Lauda"));
            castList.add(new Cast("11", "Diane Kruger", "Bridget von Hammersmark"));
            castList.add(new Cast("12", "Til Schweiger", "Hugo Stiglitz"));
        } else if (lang.equals("Italian")) {
            castList.add(new Cast("13", "Sophia Loren", "Cesira"));
            castList.add(new Cast("14", "Marcello Mastroianni", "Guido Anselmi"));
            castList.add(new Cast("15", "Monica Bellucci", "Persephone"));
        }
        return castList;
    }

    public List<Movie> getMovies(String idC) {
        List<Movie> movies = new ArrayList<>();
        if (idC.equals("1")) {
            movies.add(new Movie("1", "Iron Man", 2008));
            movies.add(new Movie("2", "Sherlock Holmes", 2009));
        } else if (idC.equals("2")) {
            movies.add(new Movie("3", "Léon: The Professional", 1994));
            movies.add(new Movie("4", "Crimson Rivers", 2000));
        } else if (idC.equals("3")) {
            movies.add(new Movie("5", "Volver", 2006));
            movies.add(new Movie("6", "Vanilla Sky", 2001));
        } else if (idC.equals("4")) {
            movies.add(new Movie("7", "Black Widow", 2021));
            movies.add(new Movie("8", "Marriage Story", 2019));
        } else if (idC.equals("5")) {
            movies.add(new Movie("9", "Captain America: The First Avenger", 2011));
            movies.add(new Movie("10", "Knives Out", 2019));
        } else if (idC.equals("6")) {
            movies.add(new Movie("11", "La Vie en Rose", 2007));
            movies.add(new Movie("12", "Inception", 2010));
        } else if (idC.equals("7")) {
            movies.add(new Movie("13", "Mesrine: Killer Instinct", 2008));
            movies.add(new Movie("14", "Black Swan", 2010));
        } else if (idC.equals("8")) {
            movies.add(new Movie("15", "No Country for Old Men", 2007));
            movies.add(new Movie("16", "Skyfall", 2012));
        } else if (idC.equals("9")) {
            movies.add(new Movie("17", "Desperado", 1995));
            movies.add(new Movie("18", "The Mask of Zorro", 1998));
        } else if (idC.equals("10")) {
            movies.add(new Movie("19", "Rush", 2013));
            movies.add(new Movie("20", "Inglourious Basterds", 2009));
        } else if (idC.equals("11")) {
            movies.add(new Movie("21", "National Treasure", 2004));
            movies.add(new Movie("22", "Troy", 2004));
        } else if (idC.equals("12")) {
            movies.add(new Movie("23", "Inglourious Basterds", 2009));
            movies.add(new Movie("24", "King Arthur", 2004));
        } else if (idC.equals("13")) {
            movies.add(new Movie("25", "Two Women", 1960));
            movies.add(new Movie("26", "Yesterday, Today and Tomorrow", 1963));
        } else if (idC.equals("14")) {
            movies.add(new Movie("27", "8½", 1963));
            movies.add(new Movie("28", "La Dolce Vita", 1960));
        } else if (idC.equals("15")) {
            movies.add(new Movie("29", "Malèna", 2000));
            movies.add(new Movie("30", "The Matrix Reloaded", 2003));
        }
        return movies;
    }
}
