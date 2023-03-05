package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

public class ImageModel {
    private String imageId;
    private String imageName;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String receiveDate;


    public ImageModel() {
    }

    public ImageModel(String imageId, String imageName, String senderId, String senderName,
                      String receiverId, String receiverName, String receiveDate) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiveDate = receiveDate;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

}
