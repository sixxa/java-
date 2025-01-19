package p2;

public class Movie {
    private String id; // Movie ID (idM)
    private String title; // Movie title
    private int year; // Year the movie was released

    public Movie(String id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    // Setters (optional, only if needed)
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
