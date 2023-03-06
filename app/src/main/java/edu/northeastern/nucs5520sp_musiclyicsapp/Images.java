package edu.northeastern.nucs5520sp_musiclyicsapp;

public class Images {
    private int count;
    private String url;
    private String username;
    private String friend;

    public Images(){}

    public Images(String url, String username,String friend,int count)
    {
        this.url = url;
        this.count = count;
        this.username = username;
        this.friend = friend;
    }

    public String getUsername(){return username;}
    private String getFriend(){return friend;}
    public int getCount()
    {
        return count;
    }
    public String getUrl()
    {
        return url;
    }


    public void setUsername(String name){username = name;}
    public void setFriend(String friends){friend = friends;}
    public void setCount(int num)
    {
        count = num;
    }
    public void setUrl(String newurl)
    {
        url = newurl;
    }


}
