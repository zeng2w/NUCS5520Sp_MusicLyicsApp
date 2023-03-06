package edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Data implements Comparable<Data> {

    public boolean isReceive;
    public String otherUser;
    public String imageId;
    public String postTime;
    public Uri uri;

    public Data() {

    }

    public Data(boolean isReceive, String otherUser, String imageId) {
        this.isReceive = isReceive;
        this.otherUser = otherUser;
        this.imageId = imageId;
        this.postTime = Calendar.getInstance().getTime().toString();
        this.uri = null;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public String getImageId() {
        return imageId;
    }

    public String getPostTime() {
        return postTime;
    }

    public Uri getUri() {
        return uri;
    }

    @NonNull
    @Override
    public String toString() {
        return "Data{" +
                "isReceive=" + isReceive +
                ", otherUser='" + otherUser + '\'' +
                ", imageId='" + imageId + '\'' +
                ", postTime='" + postTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return isReceive == data.isReceive && Objects.equals(otherUser, data.otherUser) && Objects.equals(imageId, data.imageId) && Objects.equals(postTime, data.postTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isReceive, otherUser, imageId, postTime);
    }

    // use to sort the data by date to show message history
    @Override
    public int compareTo(Data data) {
        try {
            DateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy");
            Date date1 = (Date) formatter.parse(this.postTime);
            Date date2 = (Date) formatter.parse(data.postTime);
            assert date2 != null;
            return date2.compareTo(date1);
        } catch (ParseException e) {
            return data.postTime.compareTo(this.postTime);
        }
    }
}

