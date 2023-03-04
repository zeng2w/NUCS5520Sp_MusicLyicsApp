package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

public class User {

    public String username, email;

    /**
     * Empty constructor for a User object.
     */
    public User() {
    }

    /**
     * Constructor for a User object.
     * @param username  username (default is email)
     * @param email     user's email
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
