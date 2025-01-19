package p2;

public class Cast {
    private String id; // Actor/actress ID (idC)
    private String name; // Actor/actress name
    private String role; // Role in the movie (main, supporting, extra)

    public Cast(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    // Setters (optional, only if needed)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
