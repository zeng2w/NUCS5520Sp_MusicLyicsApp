package edu.northeastern.nucs5520sp_musiclyicsapp.a8.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    public String userDeviceID;
    public List<Data> data;

    public User(String userDeviceID) {
        this.userDeviceID = userDeviceID;
        this.data = new ArrayList<>();
    }

    public User(String userDeviceID, List<Data> data) {
        this.userDeviceID = userDeviceID;
        this.data = data;
    }

    @Override
    public String toString() {
        return "User{" +
                "userDeviceID='" + userDeviceID + '\'' +
                ", data=" + data +
                '}';
    }
}
