package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

public class Chat  {

    private String senderUid;
    private String receiverUid;
    private String sticker;

    public Chat(String senderUid, String receiverUid, String sticker) {
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.sticker = sticker;
    }

    public String getSticker() {
        return sticker;
    }

    public void setSticker(String sticker) {
        this.sticker = sticker;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }
}
