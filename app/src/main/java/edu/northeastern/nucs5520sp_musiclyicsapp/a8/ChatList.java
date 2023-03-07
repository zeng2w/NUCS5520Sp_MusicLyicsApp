package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

public class ChatList {

    public String senderUid;
    public String receiverUid;

    public ChatList() {

    }

    public ChatList(String senderUid, String receiverUid) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }
}
