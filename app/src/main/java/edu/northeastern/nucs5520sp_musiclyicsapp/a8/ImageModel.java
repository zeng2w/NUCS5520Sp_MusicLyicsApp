package edu.northeastern.nucs5520sp_musiclyicsapp.a8;

public class ImageModel {
    private String imageId;
    private String senderId;
    private String imageSrc;
    private String reciverId;

    public ImageModel(String imageId, String senderId, String imageSrc, String reciverId) {
        this.imageId = imageId;
        this.senderId = senderId;
        this.imageSrc = imageSrc;
        this.reciverId = reciverId;
    }

    public ImageModel() {
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getImageName() {
        return imageSrc;
    }

    public void setImageName(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }
}
