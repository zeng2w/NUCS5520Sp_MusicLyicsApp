package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentModel {
    String songId;
    String commentId;

    String username;
    String userId;
    String context;
    int num_dislike;
    int num_like;
    String currentDate; // can support only 3 input parameter year/month/day

    public CommentModel(){

    }

    public CommentModel(String songId,String commentId, String username, String userId, String context, int num_dislike, int num_like, String currentDate) {
        this.songId = songId;
        this.commentId = commentId;
        this.username = username;
        this.userId = userId;
        this.context = context;
        this.num_dislike = num_dislike;
        this.num_like = num_like;
        this.currentDate = currentDate;
    }

    public CommentModel(String songId,String commentId, String username, String userId, String context) {
        this.songId = songId;
        this.commentId = commentId;
        this.username = username;
        this.userId = userId;
        this.context = context;
        num_dislike = 0;
        num_like = 0;
        currentDate = getCurrentDateTime();
    }

    private String getCurrentDateTime() {
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dft.format(now);
    }

    public String getCommentId(){
        return commentId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }


    public int getNum_dislike() {
        return num_dislike;
    }

    public void setNum_dislike(int num_dislike) {
        this.num_dislike = num_dislike;
    }

    public int getNum_like() {
        return num_like;
    }

    public void setNum_like(int num_like) {
        this.num_like = num_like;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
