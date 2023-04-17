package edu.northeastern.nucs5520sp_musiclyicsapp.final_project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommentModel {
    String songId;
    String commentId;

    String username;
    String context;
    int num_thumb_up;
    int getNum_thumb_down;
    LocalDate currentDate; // can support only 3 input parameter year/month/day
    public CommentModel(){
    }

    public CommentModel(String username, String context) {
        this.username = username;
        this.context = context;
        currentDate = LocalDate.now(); // generate current day


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

    public String getNum_thumb_up() {
        return String.valueOf(num_thumb_up);
    }

    public void setNum_thumb_up(int num_thumb_up) {
        this.num_thumb_up = num_thumb_up;
    }

    public String getGetNum_thumb_down() {
        return String.valueOf(getNum_thumb_down);
    }

    public void setGetNum_thumb_down(int getNum_thumb_down) {
        this.getNum_thumb_down = getNum_thumb_down;
    }

    public String getCurrentDate() {
        // concert the date into string formation
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        String formattedDate = currentDate.format(formatter);
        //  test, print it for watch
//        System.out.println("Formatted date: " + formattedDate);
        return formattedDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public void addThumbUp(){
        this.num_thumb_up++;
    }

    public void addThumDown(){
        this.num_thumb_up++;
    }
}
