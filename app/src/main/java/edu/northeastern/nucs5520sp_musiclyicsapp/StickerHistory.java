package edu.northeastern.nucs5520sp_musiclyicsapp;

import com.google.firebase.database.DatabaseReference;

public class StickerHistory {
    private String senderUsername;
    private String receiverUsername;
    private long timestamp;
    private String stickerId;

    public StickerHistory(String sender, String receiver, long timestamp, DatabaseReference newStickerRef) {}

    public StickerHistory(String senderUsername, String receiverUsername, long timestamp, String stickerId) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.timestamp = timestamp;
        this.stickerId = stickerId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }
}
