package edu.northeastern.nucs5520sp_musiclyicsapp;

public class Sticker {
    private final String id;
    private final int resourceId;

    public Sticker(String id, int resourceId) {
        this.id = id;
        this.resourceId = resourceId;
    }

    public String getId() {
        return id;
    }

    public int getResourceId() {
        return resourceId;
    }
}

