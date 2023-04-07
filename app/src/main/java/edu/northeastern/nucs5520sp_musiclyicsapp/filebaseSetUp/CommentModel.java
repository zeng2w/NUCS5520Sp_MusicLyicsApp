package edu.northeastern.nucs5520sp_musiclyicsapp.filebaseSetUp;

public class CommentModel {
    String commentId;
    String songId;
    String commentDate;
    String userID;


    public CommentModel() {
    }

    public CommentModel(String commentId, String songId, String commentDate, String userId) {
        this.commentId = commentId;
        this.songId = songId;
        this.commentDate = commentDate;
        this.userID = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
